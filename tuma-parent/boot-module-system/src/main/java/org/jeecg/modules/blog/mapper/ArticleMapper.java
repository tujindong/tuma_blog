package org.jeecg.modules.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.blog.entity.BlogArticle;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface ArticleMapper extends BaseMapper<BlogArticle> {

    Page<BlogArticle> pageListWithTags(Page<BlogArticle> page, String title, String categoryId, String tagId, Integer status);

    void updateCommentCountPlus(String articleId);

    void updateCommentCountMinus(String id);

    void updateViewCountPlus(String id);

    void updateLikeCountPlus(String objId);

    void updateLikeCountMinus(String objId);
}
