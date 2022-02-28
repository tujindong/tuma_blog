<template>
  <div class="page_content">
    <a-collapse ghost accordion :bordered="false" v-model:activeKey="activeKey">
      <a-collapse-panel
        v-for="(category, index) in categoryList"
        :key="index"
        :header="`${category.name}（${category.articleCount}）`"
      >
        <template v-if="category.loading" #extra
          ><LoadingOutlined style="transition: all 0.3s; color: #555555"
        /></template>
        <div
          class="list_wrap"
          v-if="category.articleList && category.articleList.length"
        >
          <div class="list">
            <div
              class="item"
              v-for="(article, subIndex) in category.articleList"
              :key="subIndex"
            >
              <div class="title" @click="handleGoArticle(article)">
                {{ article.title }}
              </div>
              <div class="dot"></div>
              <div class="time">
                {{ moment(article.createTime).format("YYYY-MM-DD") }}
              </div>
            </div>
          </div>
        </div>
      </a-collapse-panel>
    </a-collapse>
  </div>
</template>
<script>
import { defineComponent, reactive, onMounted, toRefs, watch } from "vue";
import { getAction } from "@/request/manage";
import { useRouter } from "vue-router";
import moment from "moment";
import { LoadingOutlined } from "@ant-design/icons-vue";
export default defineComponent({
  components: {
    LoadingOutlined,
  },

  setup() {
    const router = useRouter();

    const state = reactive({
      activeKey: -1,
      categoryList: [],
    });

    onMounted(() => {
      getCategoryList();
    });

    watch(
      () => state.activeKey,
      (val) => {
        const categoryId = state.categoryList[val]?.id;
        getArticleList(categoryId);
      }
    );

    //获取分类列表
    const getCategoryList = async () => {
      const res = await getAction("/blog/category/list", {});
      state.categoryList = res.result;
    };

    //获取文章列表
    const getArticleList = async (categoryId) => {
      const targetCategory = state.categoryList.find(
        (item) => item.id == categoryId
      );
      targetCategory.loading = true;
      const res = await getAction("/blog/article/list", {
        categoryId,
        status: 1,
      });
      targetCategory.articleList = res.result;
      targetCategory.loading = false;
    };

    const handleGoArticle = (item) => {
      router.push({ path: "/article", query: { id: item.id } });
    };

    return {
      ...toRefs(state),
      moment,
      handleGoArticle,
    };
  },
});
</script>
<style lang="less" scoped>
.page_content {
  padding: 20px 5%;
  .ant-collapse {
    background: none;
  }
  :deep(.ant-collapse-borderless) {
    background: none;
    .ant-collapse-item {
      border: none;
      .ant-collapse-header {
        font-weight: 600;
        color: #444444;
      }
      .ant-collapse-content > .ant-collapse-content-box {
        padding: 0 40px;
      }
    }
  }
  .list_wrap {
    .list {
      .item {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 20px;
        .title {
          transition: all 0.3s;
          font-size: 14px;
          cursor: pointer;
          color: #2e9dd2;
          &:hover {
            text-shadow: 0 0 10px rgba(46, 157, 210, 0.5);
          }
        }
        .dot {
          margin: 0 2%;
          flex-grow: 1;
          border-bottom: 1px dotted #dee2e6;
        }
        .time {
          font-size: 12px;
          color: #999999;
        }
      }
    }
  }
}
</style>