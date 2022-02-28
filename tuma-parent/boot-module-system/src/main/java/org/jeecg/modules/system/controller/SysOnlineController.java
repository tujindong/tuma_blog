package org.jeecg.modules.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.system.vo.SysOnlineVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @Description: 在线用户
 * @Author: chenli
 * @Date: 2020-06-07
 * @Version: V1.0
 */
@RestController
@RequestMapping("/sys/online")
@Slf4j
public class SysOnlineController {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    public RedisTemplate redisTemplate;

    @Autowired
    public ISysUserService userService;

    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Resource
    private BaseCommonService baseCommonService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<Page<SysOnlineVO>> list(@RequestParam(name="username", required=false) String username, @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                          @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
        Collection<String> keys = redisTemplate.keys(CommonConstant.PREFIX_USER_TOKEN + "*");
        SysOnlineVO online;
        List<SysOnlineVO> onlineList = new ArrayList<SysOnlineVO>();
        for (String key : keys) {
            online = new SysOnlineVO();
            String token = (String) redisUtil.get(key);
            if (!StringUtils.isEmpty(token)){
                online.setToken(token);
                LoginUser loginUser = sysBaseAPI.getUserByName(JwtUtil.getUsername(token));
                BeanUtils.copyProperties(loginUser, online);
                if (StringUtils.isNotEmpty(username)) {
                    if (StringUtils.equals(username, online.getUsername())) {
                        onlineList.add(online);
                    }
                } else {
                    onlineList.add(online);
                }
            }
        }

        Page<SysOnlineVO> page = new Page<SysOnlineVO>(pageNo, pageSize);
        int count = onlineList.size();
        List<SysOnlineVO> pages = new ArrayList<>();
        //计算当前页第一条数据的下标
        int currId = pageNo>1 ? (pageNo-1)*pageSize:0;
        for (int i=0; i<pageSize && i<count - currId;i++){
            pages.add(onlineList.get(currId+i));
        }
        page.setSize(pageSize);
        page.setCurrent(pageNo);
        page.setTotal(count);
        //计算分页总页数
        page.setPages(count %10 == 0 ? count/10 :count/10+1);
        page.setRecords(pages);

        Collections.reverse(onlineList);
        onlineList.removeAll(Collections.singleton(null));
        Result<Page<SysOnlineVO>> result = new Result<Page<SysOnlineVO>>();
        result.setSuccess(true);
        result.setResult(page);
        return result;
    }

    /**
     * 强退用户
     */
    @RequestMapping(value = "/forceLogout",method = RequestMethod.POST)
    public Result<Object> forceLogout(@RequestBody SysOnlineVO online) {
        //用户退出逻辑
        if(oConvertUtils.isEmpty(online.getToken())) {
            return Result.error("退出登录失败！");
        }
        String username = JwtUtil.getUsername(online.getToken());
        LoginUser sysUser = sysBaseAPI.getUserByName(username);
        if(sysUser!=null) {
            baseCommonService.addLog("强制: "+sysUser.getRealname()+"退出成功！", CommonConstant.LOG_TYPE_1, null,sysUser);
            log.info(" 强制  "+sysUser.getRealname()+"退出成功！ ");
            //清空用户登录Token缓存
            redisUtil.del(CommonConstant.PREFIX_USER_TOKEN + online.getToken());
            //清空用户登录Shiro权限缓存
            redisUtil.del(CommonConstant.PREFIX_USER_SHIRO_CACHE + sysUser.getId());
            //清空用户的缓存信息（包括部门信息），例如sys:cache:user::<username>
            redisUtil.del(String.format("%s::%s", CacheConstant.SYS_USERS_CACHE, sysUser.getUsername()));
            //调用shiro的logout
            SecurityUtils.getSubject().logout();
            return Result.ok("退出登录成功！");
        }else {
            return Result.error("Token无效!");
        }
    }
}
