package org.jeecg.modules.blog.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.blog.entity.BlogLike;
import org.jeecg.modules.blog.service.ArticleService;
import org.jeecg.modules.blog.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 博客点赞前端控制器
 *
 * @Author tujindong
 * @since 2021-06-28
 */
@RestController
@Api(tags="博客点赞管理")
@RequestMapping("/blog/like")
@Slf4j
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private ArticleService articleService;

    @AutoLog(value = "点赞表-添加")
    @ApiOperation(value = "点赞表-添加", notes = "点赞表-添加")
    @PostMapping(value = "/add")
    public Result<BlogLike> add(@RequestBody JSONObject jsonObject) {
        Result<BlogLike> result = new Result<>();
        BlogLike blogLike = new BlogLike();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        String objId = jsonObject.getString("objId");
        BlogLike blogLikeEntity = likeService.queryOneByObjIdAndUserId(objId, userId);
        if(blogLikeEntity == null) {
            //step.1 点赞保存
            blogLike.setObjId(objId);
            blogLike.setUserId(userId);
            likeService.save(blogLike);
            //step.2 文章点赞数量 + 1
            articleService.updateLikeCountPlus(objId);
        } else {
            result.error500("您已经点过赞了");
        }
        return result;
    }

    @AutoLog(value = "点赞表-取消点赞")
    @ApiOperation(value = "点赞表-取消点赞", notes = "点赞表-取消点赞")
    @PostMapping(value = "/unAdd")
    public Result<BlogLike> unAdd(@RequestBody BlogLike blogLike) {
        Result<BlogLike> result = new Result<>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        BlogLike blogLikeEntity = likeService.queryOneByObjIdAndUserId(blogLike.getObjId(), userId);
        if (blogLikeEntity != null) {
            try {
                //step.1 文章点赞数量 -1
                articleService.updateLikeCountMinus(blogLikeEntity.getObjId());
                //step.2 取消点赞
                likeService.removeById(blogLikeEntity.getId());
                Result.OK("取消点赞成功");
            } catch(Exception e) {
                log.error("取消点赞失败", e.getMessage());
                Result.error("取消点赞失败");
            }
        }
        return result;
    }
}
