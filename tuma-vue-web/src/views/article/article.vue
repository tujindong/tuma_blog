<template>
  <div class="page_content">
    <a-spin size="large" :spinning="isLoadingByArticle">
      <div class="title_wrap">
        <div class="title">{{ articleDetail.title }}</div>
      </div>
      <div class="desc_wrap">
        <div class="item">
          <div><user-outlined /></div>
          <div>{{ articleDetail.createBy }}</div>
        </div>
        <div class="item">
          <div><clock-circle-outlined /></div>
          <div>{{ articleDetail.createTime }}</div>
        </div>
      </div>
      <div class="content_wrap">
        <div
          class="content"
          ref="articleRef"
          v-html="articleDetail.content"
        ></div>
      </div>
    </a-spin>

    <div class="attached_wrap">
      <div class="tags">
        <div class="label">
          <tag-outlined style="font-size: 15px" />
        </div>
        <div class="value">
          <div
            class="tag"
            v-for="(item, index) in articleDetail.tagList"
            :key="index"
            @click="handleTagClick(item)"
          >
            {{ item.name }}
          </div>
        </div>
      </div>
      <div class="categorys">
        <div class="label">
          <folder-outlined style="font-size: 15px" />
        </div>
        <div class="value">
          <div class="category" @click="handleCategoryClick">
            {{ articleDetail.categoryName }}
          </div>
        </div>
      </div>
    </div>

    <div class="like_wrap">
      <div
        v-if="articleDetail.isLiked"
        class="like active"
        @click="handleLikeUnAdd"
      >
        <LikeOutlined style="color: #ffffff; font-size: 14px" />
        <div class="text">{{ articleDetail.likeCount }}</div>
      </div>
      <div v-else class="like" @click="handleLikeAdd">
        <LikeOutlined style="color: #cccccc; font-size: 14px" />
        <div class="text">{{ articleDetail.likeCount }}</div>
      </div>
    </div>

    <div class="comment_wrap">
      <div class="comment_list" v-if="commentList.length">
        <a-list :data-source="commentList">
          <template #renderItem="{ item }">
            <a-list-item>
              <a-comment>
                <template #author
                  ><a>{{ item.createBy }}</a></template
                >
                <template #avatar>
                  <a-avatar
                    style="
                      color: #ffffff;
                      background-color: rgb(0, 0, 0, 0.1);
                      font-size: 12px;
                    "
                    src=""
                    alt="头像"
                  >
                    {{ item?.createBy ? item.createBy.slice(-2) : "匿名" }}
                  </a-avatar>
                </template>
                <template #content> {{ item.content }} </template>
                <template #datetime>
                  <a-tooltip :title="item.createTime">
                    <span>{{ getTimer(item.createTime) }}</span>
                  </a-tooltip>
                </template>
              </a-comment>
            </a-list-item>
          </template>
        </a-list>
      </div>
      <div class="comment_add">
        <a-comment>
          <template #content>
            <a-form-item>
              <a-textarea
                placeholder="吐槽一下~"
                :rows="3"
                :value="commentValue"
                @change="(e) => (commentValue = e.target.value)"
              />
            </a-form-item>
            <a-form-item>
              <a-button type="primary" shape="round" @click="handleCommentAdd">
                评论
              </a-button>
            </a-form-item>
          </template>
        </a-comment>
      </div>
    </div>
  </div>
</template>

<script>
import { nextTick, onMounted, reactive, toRefs, watch, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { getAction, postAction } from "@/request/manage";
import { message } from "ant-design-vue";
import { getTimer } from "@/utils/util";
import { Local } from "@/utils/storage";
import { useStore } from "vuex";
import {
  LikeOutlined,
  UserOutlined,
  ClockCircleOutlined,
  TagOutlined,
  FolderOutlined,
} from "@ant-design/icons-vue";
export default {
  components: {
    LikeOutlined,
    UserOutlined,
    ClockCircleOutlined,
    TagOutlined,
    FolderOutlined,
  },

  setup() {
    const route = useRoute();
    const router = useRouter();
    const store = useStore();
    const articleRef = ref();

    const state = reactive({
      articleId: route.query.id,
      articleDetail: {
        id: "",
        title: "",
        content: "",
        createBy: "",
        createTime: "",
        categoryName: "",
        categoryId: "",
        likeCount: 0,
        isLiked: 0,
        tagList: [],
      },
      isLoadingByArticle: false,
      commentList: [],
      commentValue: "",
      userInfo: Local.get("userInfo"),
    });

    watch(
      () => route.path,
      () => {
        state.articleId = route.query.id;
      }
    );

    onMounted(() => {
      state.isLoadingByArticle = true;
      initPageData();
    });

    const initPageData = () => {
      state.articleId = route.query.id;
      getArticleDetail();
      getCommentList();
    };

    //获取文章详情
    const getArticleDetail = async () => {
      const params = {
        id: state.articleId,
        userId: state.userInfo?.id,
      };
      const res = await getAction("/blog/article/queryById", params);
      state.articleDetail = res.result;
      state.isLoadingByArticle = false;
      nextTick(() => {
        store.dispatch("setArticleTitles", articleRef.value);
      });
    };

    //获取评论列表
    const getCommentList = async () => {
      const params = { articleId: state.articleId };
      const res = await getAction("/blog/comment/list", params);
      state.commentList = res.result;
    };

    //点击标签 跳往标签页面
    const handleTagClick = (item) => {
      router.push({
        path: "/tags",
        query: { tagId: item.id, tagName: item.name },
      });
    };

    //点击分类 跳往分类页面
    const handleCategoryClick = () => {
      const { categoryId, categoryName } = state.articleDetail;
      router.push({
        path: "/category",
        query: { categoryId, categoryName },
      });
    };

    //文章点赞
    const handleLikeAdd = async () => {
      const params = {
        objId: state.articleDetail.id,
      };
      const res = await postAction("/blog/like/add", params);
      if (res?.code == 0) {
        getArticleDetail();
      }
    };

    //文章取消点赞
    const handleLikeUnAdd = async () => {
      const params = {
        objId: state.articleDetail.id,
      };
      const res = await postAction("/blog/like/unAdd", params);
      if (res?.code == 0) {
        getArticleDetail();
      }
    };

    const handleCommentAdd = async () => {
      const params = {
        articleId: state.articleDetail.id,
        content: state.commentValue,
      };
      const res = await postAction("/blog/comment/add", params);
      if (res.code == 200) {
        getCommentList();
        state.commentValue = "";
        message.success(res.message);
      }
    };

    return {
      ...toRefs(state),
      articleRef,
      getTimer,
      handleTagClick,
      handleCategoryClick,
      handleLikeAdd,
      handleLikeUnAdd,
      handleCommentAdd,
    };
  },
};
</script>

<style lang="less" scoped>
.page_content {
  padding: 10px 2% 80px;
  .title_wrap {
    .title {
      font-size: 16px;
      color: #333333;
      font-weight: bold;
    }
  }
  .desc_wrap {
    margin-top: 10px;
    display: flex;
    align-items: center;
    font-size: 11px;
    color: #999999;
    .item {
      display: flex;
      margin-right: 20px;
      div {
        &:last-child {
          margin-left: 4px;
        }
      }
    }
  }
  .content_wrap {
    margin-top: 30px;
    .content {
      line-height: 26px;
      font-size: 14px;
      h1 {
        font-size: 18px;
        padding-bottom: 15px;
        font-weight: bold;
        color: #333333;
      }
      h2 {
        font-size: 16px;
        padding-bottom: 8px;
        border-bottom: 1px solid #ececec;
        font-weight: bold;
        color: #333333;
        margin-bottom: 8px;
      }
      h3 {
        font-size: 15px;
        padding-bottom: 6px;
        color: #333333;
      }
      :deep(pre) {
        background-color: #f8f8f8;
        border-radius: 3px;
        padding: 5px 10px;
        margin: 20px 3px;
        font-family: sans-serif;
        code {
          xmp {
            padding: 0;
            margin: 0;
            font-size: 13px;
            line-height: 20px;
          }
        }
      }
    }
  }
  .attached_wrap {
    padding: 20px 0;
    .tags {
      display: flex;
      margin-top: 15px;
      .label {
        display: flex;
        align-items: center;
        width: 20px;
        margin-right: 4px;
      }
      .value {
        display: flex;
        font-size: 13px;
        .tag {
          margin: 0 10px 0 0;
          display: inline-block;
          padding: 1px 6px;
          background: rgba(0, 0, 0, 0.03);
          border-radius: 2px;
          transition: all 0.3s;
          &:hover {
            cursor: pointer;
            display: inline-block;
            color: #2e9dd2;
          }
        }
      }
    }
    .categorys {
      display: flex;
      margin-top: 15px;
      .label {
        display: flex;
        align-items: center;
        width: 20px;
        margin-right: 4px;
      }
      .value {
        font-size: 13px;
        .category {
          text-decoration: underline;
          cursor: pointer;
          transition: all 0.3s;
          &:hover {
            opacity: 0.9;
          }
        }
      }
    }
  }
  .like_wrap {
    padding: 10px 0 20px;
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    .like {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 100px;
      height: 40px;
      border-radius: 25px;
      border: 1px solid #cccccc;
      color: #cccccc;
      cursor: pointer;
      &.active {
        background: rgba(46, 157, 210, 0.8);
        border-color: rgba(46, 157, 210, 0.8);
        .text {
          color: #ffffff;
        }
      }
      .text {
        display: flex;
        align-items: center;
        height: 100%;
        margin-left: 2px;
        font-size: 14px;
      }
    }
  }
  .comment_wrap {
    margin-top: 20px;
    .ant-list-item:hover {
      background: none;
      box-shadow: none;
    }
    .comment_add {
      .ant-comment {
        .ant-comment-inner {
          padding: 0 0;
        }
      }
    }
  }
}
</style>
