package org.jeecg.modules.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.blog.entity.BlogTag;
import org.jeecg.modules.blog.mapper.TagMapper;
import org.jeecg.modules.blog.service.TagService;
import org.springframework.stereotype.Service;

/**
 * 标签实现层
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, BlogTag> implements TagService {
}
