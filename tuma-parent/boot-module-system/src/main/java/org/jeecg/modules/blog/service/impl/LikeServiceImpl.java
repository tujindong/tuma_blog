package org.jeecg.modules.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.blog.entity.BlogLike;
import org.jeecg.modules.blog.mapper.LikeMapper;
import org.jeecg.modules.blog.service.LikeService;
import org.springframework.stereotype.Service;

/**
 * 点赞实现层
 */
@Service
public class LikeServiceImpl extends ServiceImpl<LikeMapper, BlogLike> implements LikeService {
    @Override
    public BlogLike queryOneByObjIdAndUserId(String objId, String userId) {
        BlogLike blogLike = baseMapper.queryOneByObjIdAndUserId(objId, userId);
        return blogLike;
    }
}
