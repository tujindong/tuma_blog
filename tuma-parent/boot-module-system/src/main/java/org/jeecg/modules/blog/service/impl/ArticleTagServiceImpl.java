package org.jeecg.modules.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.blog.entity.BlogArticleTag;
import org.jeecg.modules.blog.mapper.ArticleTagMapper;
import org.jeecg.modules.blog.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * 文章标签关系表实现层
 */
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, BlogArticleTag> implements ArticleTagService {
}
