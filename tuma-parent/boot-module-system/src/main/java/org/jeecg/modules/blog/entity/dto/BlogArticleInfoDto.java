package org.jeecg.modules.blog.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.jeecg.modules.blog.entity.BlogTag;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class BlogArticleInfoDto {
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "文章标题")
    private String title;

    @ApiModelProperty(value = "文章简介")
    private String intro;

    @ApiModelProperty(value = "文章内容")
    private String content;

    @ApiModelProperty(value = "标签id")
    @TableField(exist = false)
    private String tagIds;

    @ApiModelProperty(value = "0 暂存 1已发布")
    private Integer status;

    @ApiModelProperty(value = "评论数量")
    private Integer commentCount;

    @ApiModelProperty(value = "点赞数量")
    private Integer likeCount;

    @ApiModelProperty(value = "浏览数量")
    private Integer viewCount;

    @ApiModelProperty(value = "是否点赞 0未点赞 1已点赞")
    private Integer isLiked;

    @ApiModelProperty(value = "文章标签")
    private List<BlogTag> tagList = new ArrayList<>();

    @ApiModelProperty(value = "文章分类名称")
    private String categoryName;

    @ApiModelProperty(value = "文章分类Id")
    private String categoryId;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

}
