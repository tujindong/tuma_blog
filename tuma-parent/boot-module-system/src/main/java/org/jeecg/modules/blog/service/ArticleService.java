package org.jeecg.modules.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.blog.entity.BlogArticle;
import org.jeecg.modules.blog.entity.dto.BlogArticleInfoDto;

/**
 * 文章Service层
 */
public interface ArticleService extends IService<BlogArticle> {
    void saveArticle(BlogArticle blogArticle);

    void deleteArticle(String id);

    BlogArticleInfoDto getArticleById(String id, String userId);

    IPage<BlogArticle> pageListWithTags(Page<BlogArticle> page, String title, String categoryId, String tagId, Integer status);

    void editArticle(BlogArticle blogArticle);

    void updateCommentCountPlus(String articleId);

    void updateCommentCountMinus(String id);

    void updateViewCountPlus(String id);

    void updateLikeCountPlus(String objId);

    void updateLikeCountMinus(String objId);
}
