package org.jeecg.modules.blog.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.blog.entity.BlogComment;
import org.jeecg.modules.blog.mapper.CommentMapper;
import org.jeecg.modules.blog.service.CommentService;
import org.springframework.stereotype.Service;

/**
 * 评论实现层
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, BlogComment> implements CommentService {
    @Override
    public IPage<BlogComment> pageListWithArticle(Page<BlogComment> page, String articleTitle, String content) {
        Page<BlogComment> blogCommentPage = baseMapper.pageListWithArticle(page, articleTitle, content);
        return blogCommentPage;
    }
}
