package org.jeecg.modules.blog.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.blog.entity.BlogComment;
import org.jeecg.modules.blog.service.ArticleService;
import org.jeecg.modules.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 博客评论前端控制器
 *
 * @Author tujindong
 * @since 2021-09-22
 */
@RestController
@Api(tags="博客评论管理")
@RequestMapping("/blog/comment")
@Slf4j
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ArticleService articleService;

    @AutoLog(value = "评论表-根据文章id查询所有")
    @ApiOperation(value = "评论表-根据文章id查询所有", notes = "评论表-根据文章id查询所有")
    @GetMapping(value = "/list")
    public Result<BlogComment> List(BlogComment blogComment) {
        Result result = new Result();
        QueryWrapper<BlogComment> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(blogComment.getContent())) {
            queryWrapper.like("content", blogComment.getContent());
        }
        if(!StringUtils.isEmpty(blogComment.getArticleId())) {
            queryWrapper.eq("article_id", blogComment.getArticleId());
        }
        queryWrapper.select().orderByDesc("create_time");
        List<BlogComment> list = commentService.list(queryWrapper);
        result.setSuccess(true);
        result.setResult(list);
        return result;
    }

    @AutoLog(value = "评论表- 分页列表")
    @ApiOperation(value = "评论表-分页列表", notes = "评论表-分页列表")
    @GetMapping(value = "/pageListWithArticle")
    public Result<IPage<BlogComment>> pageList(
            BlogComment blogComment,
            @RequestParam(name="pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name="pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<BlogComment> page = new Page<>(pageNo, pageSize);
        IPage<BlogComment> pageList = commentService.pageListWithArticle(page, blogComment.getArticleTitle(), blogComment.getContent());
        return Result.OK(pageList);
    }

    @AutoLog(value="评论表-添加")
    @ApiOperation(value = "评论表-添加", notes = "评论表-添加")
    @PostMapping(value= "/add")
    public Result<BlogComment> add(@RequestBody BlogComment blogComment) {
        Result<BlogComment> result = new Result<>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        if (StringUtils.isEmpty(blogComment.getArticleId())) {
            result.error500("articleId不能为空");
            return result;
        }
        if (StringUtils.isEmpty(blogComment.getContent())) {
            result.error500("评论不能为空");
            return result;
        }
        //保存评论内容
        blogComment.setUserId(userId);
        commentService.save(blogComment);
        //评论数量 + 1
        articleService.updateCommentCountPlus(blogComment.getArticleId());
        try {
            result.success("添加成功");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    @AutoLog(value="评论表-通过id删除")
    @ApiOperation(value = "评论表-通过id删除", notes = "评论表-通过id删除")
    @DeleteMapping(value= "/delete")
    public Result<BlogComment> delete(@RequestParam(name="id", required = true) String id) {
        Result<BlogComment> result = new Result<>();
        try{
            //查找评论文章
            BlogComment blogCommentEntity = commentService.getById(id);
            if (blogCommentEntity == null) {
                result.error500("未找到对应实体");
                return result;
            }
            //评论数量 -1
            articleService.updateCommentCountMinus(blogCommentEntity.getArticleId());
            //删除评论
            commentService.removeById(id);
            result.ok("删除成功");
        } catch (Exception e) {
            log.error("删除失败", e.getMessage());
            result.error("删除失败");
        }
        return result;
    }

    @AutoLog(value = "评论表-批量删除")
    @ApiOperation(value = "评论表-批量删除", notes = "评论表-批量删除")
    @DeleteMapping(value="/deleteBatch")
    public Result<BlogComment> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<BlogComment> result = new Result<BlogComment>();
        if (ids == null || "".equals(ids.trim())){
            result.error500("参数不识别");
        } else {
            //批量删除 文章评论数量 -1
            List<String> idList = Arrays.asList(ids.split(","));
            for (String id : idList) {
                BlogComment blogCommentEntity = commentService.getById(id);
                articleService.updateCommentCountMinus(blogCommentEntity.getArticleId());
            }
            //删除多个评论
            commentService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功");
        }
        return result;
    }
}
