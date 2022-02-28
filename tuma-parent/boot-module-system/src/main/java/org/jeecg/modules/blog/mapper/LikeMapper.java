package org.jeecg.modules.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.blog.entity.BlogLike;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface LikeMapper extends BaseMapper<BlogLike> {
    BlogLike queryOneByObjIdAndUserId(String objId, String userId);
}
