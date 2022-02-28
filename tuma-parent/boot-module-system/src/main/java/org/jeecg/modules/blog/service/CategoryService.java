package org.jeecg.modules.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.blog.entity.BlogCategory;

import java.util.List;

/**
 * 分类Service层
 */
public interface CategoryService extends IService<BlogCategory> {
    List<BlogCategory> queryCategoryList();
}
