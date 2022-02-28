package org.jeecg.modules.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.blog.entity.BlogArticleTag;
import org.jeecg.modules.blog.entity.BlogTag;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;

@MapperScan
public interface ArticleTagMapper extends BaseMapper<BlogArticleTag> {

    List<BlogTag> getTagsByArticleId(String id);
}
