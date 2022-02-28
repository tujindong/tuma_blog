package org.jeecg.modules.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.blog.entity.BlogCategory;
import org.jeecg.modules.blog.entity.BlogTag;
import org.jeecg.modules.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 博客文章分类前端控制器
 *
 * @Author tujindong
 * @since 2021-06-28
 */
@RestController
@Api(tags="博客分类管理")
@RequestMapping("/blog/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @AutoLog(value = "分类表-查询所有")
    @ApiOperation(value = "分类表-查询所有", notes = "分类表-查询所有")
    @GetMapping(value = "/list")
    public Result<BlogCategory> list() {
        Result result = new Result<>();
        List<BlogCategory> list = categoryService.queryCategoryList();
        result.setSuccess(true);
        result.setResult(list);
        return result;
    }

    @AutoLog(value = "分类表-分页查询")
    @ApiOperation(value = "分类表-分页查询", notes = "分类表-分页查询")
    @GetMapping(value = "/pageList")
    public Result<IPage<BlogCategory>> pageList(
            BlogCategory blogCategory,
            @RequestParam(name="pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name="pageSize", defaultValue = "10") Integer pageSize
    ) {
        Result <IPage<BlogCategory>> result = new Result<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("del_flag", 0);
        if(!StringUtils.isEmpty(blogCategory.getName())){
            queryWrapper.like("name", blogCategory.getName());
        }
        Page<BlogCategory> page = new Page<>(pageNo, pageSize);
        IPage<BlogCategory> pageList = categoryService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }


    @AutoLog(value = "分类表-添加")
    @ApiOperation(value = "分类表-添加", notes = "分类表-添加")
    @PostMapping(value = "/add")
    public Result<BlogCategory> add(@RequestBody BlogCategory blogCategory) {
        Result<BlogCategory> result = new Result<>();
        categoryService.save(blogCategory);
        try {
            result.success("添加成功");
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    @AutoLog(value = "分类表-编辑")
    @ApiOperation(value="分类表-编辑", notes="分类表-编辑")
    @PutMapping(value = "/edit")
    public Result<BlogCategory> edit(@RequestBody BlogCategory blogCategory) {
        Result<BlogCategory> result = new Result<>();
        BlogCategory blogCategoryEntity = categoryService.getById(blogCategory.getId());
        if (blogCategoryEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = categoryService.updateById(blogCategory);
            if (ok) {
                result.success("修改成功");
            }
        }
        return result;
    }

    @AutoLog(value = "分类表-删除")
    @ApiOperation(value="分类表-删除", notes="分类表-删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try{
            categoryService.removeById(id);
        }catch (Exception e) {
            log.error("删除失败", e.getMessage());
            return Result.error("删除失败");
        }
        return Result.OK("删除成功");
    }

    @AutoLog(value = "分类表-批量删除")
    @ApiOperation(value = "分类表-批量删除", notes = "分类表-批量删除")
    @DeleteMapping(value="/deleteBatch")
    public Result<BlogCategory> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<BlogCategory> result = new Result<>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别");
        } else {
            categoryService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功");
        }
        return result;
    }
}
