package org.jeecg.modules.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.blog.entity.BlogTag;
import org.jeecg.modules.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 博客文章标签前端控制器
 *
 * @Author tujindong
 * @since 2021-06-28
 */
@RestController
@Api(tags="博客标签管理")
@RequestMapping("/blog/tag")
@Slf4j
public class TagController {

    @Autowired
    private TagService tagService;

    @AutoLog(value = "标签表-查询所有")
    @ApiOperation(value = "标签表-查询所有", notes = "标签表-查询所有")
    @GetMapping(value = "/list")
    public Result<BlogTag> list() {
        Result result = new Result<>();
        QueryWrapper<BlogTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag", 0);
        List<BlogTag> list = tagService.list(queryWrapper);
        result.setSuccess(true);
        result.setResult(list);
        return result;
    }

    @AutoLog(value = "标签表-分页查询")
    @ApiOperation(value = "标签表-分页查询", notes = "标签表-分页查询")
    @GetMapping(value = "/pageList")
    public Result<IPage<BlogTag>> pageList(
            BlogTag blogTag,
            @RequestParam(name="pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name="pageSize", defaultValue = "10") Integer pageSize
    ) {
        Result <IPage<BlogTag>> result = new Result<>();
        QueryWrapper<BlogTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag", 0);
        if(!StringUtils.isEmpty(blogTag.getName())) {
            queryWrapper.like("name", blogTag.getName());
        }
        Page<BlogTag> page = new Page<>(pageNo, pageSize);
        IPage<BlogTag> pageList = tagService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    @AutoLog(value = "标签表-添加")
    @ApiOperation(value = "标签表-添加", notes = "标签表-添加")
    @PostMapping(value = "/add")
    public Result<BlogTag> add(@RequestBody BlogTag blogTag) {
        Result<BlogTag> result = new Result<>();
        tagService.save(blogTag);
        try {
            result.success("添加成功");
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    @AutoLog(value = "标签表-编辑")
    @ApiOperation(value="标签表-编辑", notes="标签表-编辑")
    @PutMapping(value = "/edit")
    public Result<BlogTag> edit(@RequestBody BlogTag blogTag) {
        Result<BlogTag> result = new Result<>();
        BlogTag blogTagEntity = tagService.getById(blogTag.getId());
        if (blogTagEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = tagService.updateById(blogTag);
            if (ok) {
                result.success("修改成功");
            }
        }
        return result;
    }

    @AutoLog(value = "标签表-通过id删除")
    @ApiOperation(value = "标签表-通过id删除", notes = "标签表-通过id删除")
    @DeleteMapping(value="/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try{
            tagService.removeById(id);
        }catch(Exception e){
            log.error("删除失败", e.getMessage());
            return Result.error("删除失败");
        }
        return Result.OK("删除成功");
    }

    @AutoLog(value = "标签表-批量删除")
    @ApiOperation(value = "标签表-批量删除", notes = "标签表-批量删除")
    @DeleteMapping(value="/deleteBatch")
    public Result<BlogTag> deleteBatch(@RequestParam(name = "ids", required = true) String ids){
        Result<BlogTag> result = new Result<BlogTag>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别");
        } else {
            tagService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功");
        }
        return result;
    }
}
