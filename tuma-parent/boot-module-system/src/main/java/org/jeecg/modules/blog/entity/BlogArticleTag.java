package org.jeecg.modules.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 文章标签表
 * </p>
 *
 * @Author
 * @since 2018-12-21
 */
@Data
@TableName("blog_article_tag")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BlogArticleTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 文章id
     */
    private String articleId;

    /**
     * 标签id
     */
    private String tagId;

	public BlogArticleTag() {
	}

	public BlogArticleTag(String articleId, String tagId) {
		this.articleId = articleId;
		this.tagId = tagId;
	}

    

}
