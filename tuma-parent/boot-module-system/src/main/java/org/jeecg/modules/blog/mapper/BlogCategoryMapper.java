package org.jeecg.modules.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.blog.entity.BlogCategory;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;

@MapperScan
public interface BlogCategoryMapper extends BaseMapper<BlogCategory> {
    List<BlogCategory> queryCategoryList();
}
