<template>
  <div class="page_content">
    <div class="title_wrap">
      <div class="label">
        <FolderOutlined style="font-size: 14px" />
      </div>
      <div class="value">{{ categoryName }}</div>
      <div class="num">{{ total }}</div>
    </div>
    <div class="list_wrap">
      <div class="list">
        <div class="item" v-for="(item, index) in articleList" :key="index">
          <div class="title" @click="handleGoArticle(item)">
            {{ item.title }}
          </div>
          <div class="dot"></div>
          <div class="time">
            {{ moment(item.createTime).format("YYYY-MM-DD") }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { reactive, toRefs, onMounted, watch } from "vue";
import { FolderOutlined } from "@ant-design/icons-vue";
import { useRoute, useRouter } from "vue-router";
import { getAction } from "@/request/manage";
import moment from "moment";
export default {
  name: "category",
  components: {
    FolderOutlined,
  },
  setup() {
    const route = useRoute();
    const router = useRouter();

    const state = reactive({
      categoryId: "",
      categoryName: "",
      total: 0,
      articleList: [],
    });

    onMounted(() => {
      initPageData();
    });

    watch(
      () => route.query.categoryId,
      () => {
        initPageData();
      }
    );

    const initPageData = () => {
      state.categoryName = route.query.categoryName;
      state.categoryId = route.query.categoryId;
      getArticleList();
    };

    //获取文章列表
    const getArticleList = async () => {
      const res = await getAction("/blog/article/pageListWithTags", {
        pageSize: 10,
        pageNo: 1,
        categoryId: state.categoryId,
        status: 1,
      });
      state.total = res.result.total;
      state.articleList = res.result.records;
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
};
</script>

<style lang="less" scoped>
.page_content {
  padding: 10px 5%;
  .title_wrap {
    display: flex;
    align-items: center;
    margin-bottom: 30px;
    font-size: 13px;
    .label {
      margin-right: 6px;
    }
    .value {
      margin-right: 6px;
    }
    .num {
      color: #2e9dd2;
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
