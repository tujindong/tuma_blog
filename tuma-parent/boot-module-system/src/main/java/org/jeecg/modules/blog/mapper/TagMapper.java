package org.jeecg.modules.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.blog.entity.BlogTag;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface TagMapper extends BaseMapper<BlogTag> {
}
