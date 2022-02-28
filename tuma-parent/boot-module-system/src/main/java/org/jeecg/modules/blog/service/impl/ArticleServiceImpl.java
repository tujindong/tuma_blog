package org.jeecg.modules.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.blog.entity.*;
import org.jeecg.modules.blog.entity.dto.BlogArticleInfoDto;
import org.jeecg.modules.blog.mapper.ArticleMapper;
import org.jeecg.modules.blog.mapper.ArticleTagMapper;
import org.jeecg.modules.blog.service.ArticleService;
import org.jeecg.modules.blog.service.CategoryService;
import org.jeecg.modules.blog.service.LikeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 文章列表实现层
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, BlogArticle> implements ArticleService {

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private LikeService likeService;

    /**
     * 添加文章
     * @param blogArticle
     */
    @Override
    public void saveArticle(BlogArticle blogArticle) {
        //step.1 保存文章
        this.save(blogArticle);
        //step.2 保存标签
        String[] tagArr = blogArticle.getTagIds().split(",");
        for (String tagId : tagArr) {
            BlogArticleTag blogArticleTag = new BlogArticleTag(blogArticle.getId(), tagId);
            articleTagMapper.insert(blogArticleTag);
        }
    }

    /**
     * 删除文章
     * @param id
     */
    @Override
    public void deleteArticle(String id) {
        //step.1 删除关系表
        QueryWrapper<BlogArticleTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id", id);
        articleTagMapper.delete(queryWrapper);
        //step.2 删除文章
        this.removeById(id);
    }

    /**
     * 根据文章id查询详情
     * @param id
     * @return
     */
    @Override
    public BlogArticleInfoDto getArticleById(String id, String userId) {
        BlogArticleInfoDto blogArticleInfoDto = new BlogArticleInfoDto();
        //step.1 查询文章表
        BlogArticle blogArticle = this.getById(id);
        //step.2 查询关联标签
        List<BlogTag> blogTagList = articleTagMapper.getTagsByArticleId(id);
        //step.3 查询分类值
        BlogCategory blogCategory = categoryService.getById(blogArticle.getCategoryId());
        String categoryName = blogCategory.getName();
        //step.4 查询是否点赞
        if (!StringUtils.isEmpty(userId)) {
            BlogLike blogLike = likeService.queryOneByObjIdAndUserId(id, userId);
            if (blogLike != null) {
                blogArticleInfoDto.setIsLiked(blogLike.getLikeStatus());
            }
        } else {
            blogArticleInfoDto.setIsLiked(0);
        }
        //step.4 复制字段值
        BeanUtils.copyProperties(blogArticle, blogArticleInfoDto);
        blogArticleInfoDto.setTagList(blogTagList);
        blogArticleInfoDto.setCategoryName(categoryName);
        return blogArticleInfoDto;
    }

    /**
     * 查询文章分页列表 携带 Tags标签列表
     * @param page
     * @param
     * @return
     */
    @Override
    public IPage<BlogArticle> pageListWithTags(Page<BlogArticle> page, String title, String categoryId, String tagId, Integer status) {
        Page<BlogArticle> blogArticlePage = baseMapper.pageListWithTags(page, title, categoryId, tagId, status);
        return blogArticlePage;
    }

    /**
     * 编辑文章
     * @param blogArticle
     * @return
     */
    @Override
    @Transactional
    public void editArticle(BlogArticle blogArticle) {
        //step.1 编辑文章表
        int effectArticleRows = baseMapper.updateById(blogArticle);
        //step.2 删除关系表
        QueryWrapper<BlogArticleTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id", blogArticle.getId());
        articleTagMapper.delete(queryWrapper);
        //step.3 保存关系表
        String[] tagArr = blogArticle.getTagIds().split(",");
        for (String tagId : tagArr) {
            BlogArticleTag blogArticleTag = new BlogArticleTag(blogArticle.getId(), tagId);
            int effectTagRows = articleTagMapper.insert(blogArticleTag);
        }
    }

    /**
     * 评论数量 + 1
     * @param articleId
     */
    @Override
    public void updateCommentCountPlus(String articleId) {
        baseMapper.updateCommentCountPlus(articleId);
    }

    /**
     *
     * 评论数量 -1
     * @param id
     */
    @Override
    public void updateCommentCountMinus(String id) {
        baseMapper.updateCommentCountMinus(id);
    }

    /**
     * 阅读量 +1
     * @param id
     */
    @Override
    public void updateViewCountPlus(String id) {
        baseMapper.updateViewCountPlus(id);
    }

    /**
     * 点赞 + 1
     * @param objId
     */
    @Override
    public void updateLikeCountPlus(String objId) {
        baseMapper.updateLikeCountPlus(objId);
    }

    @Override
    public void updateLikeCountMinus(String objId) {
        baseMapper.updateLikeCountMinus(objId);
    }
}
