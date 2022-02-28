package org.jeecg.modules.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.blog.entity.BlogCategory;
import org.jeecg.modules.blog.mapper.BlogCategoryMapper;
import org.jeecg.modules.blog.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分类列表实现层
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<BlogCategoryMapper, BlogCategory> implements CategoryService {
    @Override
    public List<BlogCategory> queryCategoryList() {
        List<BlogCategory> list = baseMapper.queryCategoryList();
        return list;
    }
}
