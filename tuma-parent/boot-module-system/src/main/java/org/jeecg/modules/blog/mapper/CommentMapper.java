package org.jeecg.modules.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.blog.entity.BlogComment;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface CommentMapper extends BaseMapper<BlogComment> {
    Page<BlogComment> pageListWithArticle(Page<BlogComment> page, String articleTitle, String content);
}
