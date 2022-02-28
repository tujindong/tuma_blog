package org.jeecg.modules.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.blog.entity.BlogArticle;
import org.jeecg.modules.blog.entity.dto.BlogArticleInfoDto;
import org.jeecg.modules.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * 博客文章列表前端控制器
 *
 * @Author tujindong
 * @since 2021-06-28
 */
@RestController
@Api(tags="博客文章管理")
@RequestMapping("/blog/article")
@Slf4j
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @AutoLog(value = "文章表-查询所有")
    @ApiOperation(value = "文章表-查询所有", notes = "文章表-查询所有")
    @GetMapping(value = "/list")
    public Result<BlogArticle> list(
            @RequestParam(name="categoryId", defaultValue = "") String categoryId,
            @RequestParam(name="status") Integer status
    ) {
        Result result = new Result<>();
        QueryWrapper<BlogArticle> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag", 0);
        queryWrapper.orderByDesc("create_time");
        if(!StringUtils.isEmpty(categoryId)) {
            queryWrapper.eq("category_id", categoryId);
        }
        if(!StringUtils.isEmpty(status)) {
            queryWrapper.eq("status", status);
        }
        List<BlogArticle> list = articleService.list(queryWrapper);
        result.setSuccess(true);
        result.setResult(list);
        return result;
    }

    @AutoLog(value = "文章表-分页列表查询后台管理")
    @ApiOperation(value = "文章表-分页列表查询后台管理", notes = "文章表-分页列表查询后台管理")
    @GetMapping(value = "/pageList")
    public Result<IPage<BlogArticle>> pageList(
            BlogArticle blogArticle,
            @RequestParam(name="pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name="pageSize", defaultValue = "10") Integer pageSize,
            HttpServletRequest req
    ) {
        Result <IPage<BlogArticle>> result = new Result<>();
        QueryWrapper<BlogArticle> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag", 0);
        queryWrapper.select(BlogArticle.class, info -> !info.getColumn().equals("content"));
        if(!StringUtils.isEmpty(blogArticle.getTitle())) {
            queryWrapper.like("title", blogArticle.getTitle());
        }
        if(!StringUtils.isEmpty(blogArticle.getStatus())) {
            queryWrapper.eq("status", blogArticle.getStatus());
        }
        Page<BlogArticle> page = new Page<>(pageNo, pageSize);
        IPage<BlogArticle> pageList = articleService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }


    @AutoLog(value = "文章表-分页列表查询前台(过滤掉删除状态类型的)")
    @ApiOperation(value = "文章表-分页列表查询前台", notes = "文章表-分页列表查询前台")
    @GetMapping(value = "/pageListFront")
    public Result<IPage<BlogArticle>> pageListFront(
            BlogArticle blogArticle,
            @RequestParam(name="pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name="pageSize", defaultValue = "10") Integer pageSize
    ) {
        Result <IPage<BlogArticle>> result = new Result<>();
        QueryWrapper<BlogArticle> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag", 0);
        queryWrapper.select(BlogArticle.class, info -> !info.getColumn().equals("content"));
        if(!StringUtils.isEmpty(blogArticle.getTitle())) {
            queryWrapper.like("title", blogArticle.getTitle());
        }
        Page<BlogArticle> page = new Page<>(pageNo, pageSize);
        IPage<BlogArticle> pageList = articleService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    @AutoLog(value = "文章表- 分页列表查询(查询出携带标签名称)")
    @ApiOperation(value = "文章表-分页列表查询携带标签", notes = "文章表-分页列表查询携带标签")
    @GetMapping(value = "/pageListWithTags")
    public Result<IPage<BlogArticle>> pageListWithTags(
            BlogArticle blogArticle,
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize,
            String tagId
    ) {
        Page<BlogArticle> page = new Page<>(pageNo, pageSize);
        IPage<BlogArticle> pageList = articleService.pageListWithTags(
                page,
                blogArticle.getTitle(),
                blogArticle.getCategoryId(),
                tagId,
                blogArticle.getStatus()
        );
        return Result.OK(pageList);
    }

    @AutoLog(value = "文章表-添加")
    @ApiOperation(value = "文章表-添加", notes = "文章表-添加")
    @PostMapping(value="/add")
    public Result<BlogArticle> add(@RequestBody BlogArticle blogArticle) {
        Result<BlogArticle> result = new Result<>();
        try {
            if(StringUtils.isEmpty(blogArticle.getTagIds())) {
                result.error500("标签不能为空");
                return result;
            }
            if(StringUtils.isEmpty(blogArticle.getCategoryId())) {
                result.error500("分类不能为空");
                return result;
            }
            articleService.saveArticle(blogArticle);
            result.success("添加成功");
        } catch (Exception e){
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    @AutoLog(value = "文章表-编辑")
    @ApiOperation(value="文章表-编辑", notes="文章表-编辑")
    @PutMapping(value = "/edit")
    public Result<BlogArticle> edit(@RequestBody BlogArticle blogArticle) {
        Result<BlogArticle> result = new Result<>();
        BlogArticle blogArticleEntity = articleService.getById(blogArticle.getId());
        try{
            if(StringUtils.isEmpty(blogArticle.getTagIds())) {
                result.error500("标签不能为空");
                return result;
            }
            if (blogArticleEntity == null) {
                result.error500("未找到对应实体");
                return result;
            }
            articleService.editArticle(blogArticle);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    @AutoLog(value = "文章表-通过id查询")
    @ApiOperation(value = "文章表-通过id查询", notes = "文章表-通过id查询")
    @GetMapping(value="/queryById")
    public Result<BlogArticleInfoDto> queryById(
            @RequestParam(name = "id", required = true) String id,
            @RequestParam(name = "userId", required = false) String userId
    ) {
        Result<BlogArticleInfoDto> result = new Result<>();
        //获取文章详情
        BlogArticleInfoDto blogArticleInfoDto = articleService.getArticleById(id, userId);
        //阅读量 + 1
        articleService.updateViewCountPlus(id);
        if (blogArticleInfoDto == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(blogArticleInfoDto);
            result.setSuccess(true);
        }
        return result;
    }

    @AutoLog(value = "文章表-通过id删除")
    @ApiOperation(value = "文章表-通过id删除", notes = "文章表-通过id删除")
    @DeleteMapping(value="/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try{
            articleService.deleteArticle(id);
        }catch(Exception e){
            log.error("删除失败", e.getMessage());
            return Result.error("删除失败");
        }
        return Result.ok("删除成功");
    }

    @AutoLog(value = "文章表-批量删除")
    @ApiOperation(value = "文章表-批量删除", notes = "文章表-批量删除")
    @DeleteMapping(value="/deleteBatch")
    public Result<BlogArticle> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<BlogArticle> result = new Result<>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别");
        } else {
            articleService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功");
        }
        return result;
    }

    @AutoLog(value = "文章表-发布")
    @ApiOperation(value = "文章表-发布", notes = "文章表-发布")
    @PostMapping(value="/release")
    public Result<BlogArticle> release(@RequestBody JSONObject jsonObject) {
        Result<BlogArticle> result = new Result<>();
        try {
            String id = jsonObject.getString("id");
            BlogArticle blogArticle = articleService.getById(id);
            if (blogArticle == null) {
                result.error500("未找到对应实体");
            } else {
                blogArticle.setStatus(1);
                boolean isOk = articleService.updateById(blogArticle);
                if (isOk) {
                    result.success("发布成功");
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("发布失败");
        }
        return result;
    }

    @AutoLog(value = "文章表-撤回")
    @ApiOperation(value = "文章表-撤回", notes = "文章表-撤回")
    @PostMapping(value="/withdraw")
    public Result<BlogArticle> withdraw(@RequestBody JSONObject jsonObject) {
        Result<BlogArticle> result = new Result<>();
        try {
            String id = jsonObject.getString("id");
            BlogArticle blogArticle = articleService.getById(id);
            if (blogArticle == null) {
                result.error500("未找到对应实体");
            } else {
                blogArticle.setStatus(0);
                boolean isOk = articleService.updateById(blogArticle);
                if (isOk) {
                    result.success("撤回成功");
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("撤回失败");
        }
        return result;
    }
}
