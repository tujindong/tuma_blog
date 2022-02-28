package org.jeecg.modules.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.blog.entity.BlogLike;

/**
 * 点赞Service层
 */
public interface LikeService extends IService<BlogLike> {
    BlogLike queryOneByObjIdAndUserId(String objId, String userId);
}
