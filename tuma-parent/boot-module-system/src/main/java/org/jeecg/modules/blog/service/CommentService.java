package org.jeecg.modules.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.blog.entity.BlogComment;

public interface CommentService extends IService<BlogComment> {
    IPage<BlogComment> pageListWithArticle(Page<BlogComment> page, String articleTitle, String content);
}
