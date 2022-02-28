<template>
  <div class="page_content">
    <div class="tag_wrap" v-if="isSearchBoxShow && !dataSource.length">
      <div class="title">热门标签</div>
      <div class="content">
        <div
          class="tag"
          v-for="(item, index) in tagList"
          :key="index"
          @click="handleTagClick(item)"
        >
          <span>#</span>
          <span>{{ item.name }}</span>
        </div>
      </div>
    </div>
    <div class="list_wrap" v-else>
      <a-spin size="large" :spinning="isLoading">
        <a-list
          item-layout="vertical"
          size="large"
          :pagination="
            pagination.total > pagination.pageSize ? pagination : false
          "
          :data-source="dataSource"
          :locale="{ emptyText: '暂无内容' }"
        >
          <template #renderItem="{ item }">
            <a-list-item key="item.title" @click="handleGoArticle(item)">
              <a-list-item-meta :description="item.createTime">
                <template #title>
                  <a :href="item.href">{{ item.title }}</a>
                </template>
              </a-list-item-meta>
              <div class="content">{{ item.intro }}</div>
            </a-list-item>
          </template>
        </a-list>
      </a-spin>
    </div>
  </div>
</template>

<script>
import { getAction } from "@/request/manage";
import { onMounted, reactive, toRefs, watch, computed } from "vue";
import { useRouter, useRoute } from "vue-router";
import { useStore } from "vuex";
export default {
  name: "search",

  components: {},

  setup() {
    const router = useRouter();
    const route = useRoute();
    const store = useStore();

    const state = reactive({
      isLoading: false,
      dataSource: [],
      tagList: [],
      searchKey: "",
      pagination: {
        onChange: (page) => {
          handlePageChange(page);
        },
        pageSize: 10,
        pageNo: 1,
        total: 0,
        showSizeChanger: false,
      },
    });

    const isSearchBoxShow = computed(() => store.state.search.isSearchBoxShow);

    onMounted(() => {
      state.pagination.pageNo = 1;
      state.searchKey = route.query.searchKey;
      getTagList();
      if (route.query.searchKey) {
        getArticleList();
      }
    });

    watch(
      () => route.query.searchKey,
      () => {
        const searchKey = route.query.searchKey;
        state.pagination.pageNo = 1;
        state.searchKey = searchKey;
        getArticleList();
      }
    );

    //获取标签列表
    const getTagList = async () => {
      const res = await getAction("/blog/tag/list", {});
      state.tagList = res.result;
    };

    //获取文章列表
    const getArticleList = async () => {
      state.isLoading = true;
      const res = await getAction("/blog/article/pageListWithTags", {
        pageSize: state.pagination.pageSize,
        pageNo: state.pagination.pageNo,
        title: state.searchKey,
      });
      state.dataSource = res.result.records;
      state.pagination.total = res.result.total;
      state.isLoading = false;
    };

    const handlePageChange = (page) => {
      state.pagination.pageNo = page;
      getArticleList();
    };

    const handleTagClick = (item) => {
      if (route.path == "/tags") {
        router.replace({
          path: "/tags",
          query: { tagId: item.id, tagName: item.name },
        });
      } else {
        router.push({
          path: "/tags",
          query: { tagId: item.id, tagName: item.name },
        });
      }
    };

    const handleGoArticle = (item) => {
      router.push({ path: "/article", query: { id: item.id } });
    };

    return {
      ...toRefs(state),
      isSearchBoxShow,
      handleTagClick,
      handleGoArticle,
    };
  },
};
</script>

<style lang="less">
.ant-list-item {
  cursor: pointer;
  transition: all 0.3s;
  &:hover {
    background-color: rgba(206, 226, 246, 0.2);
    box-shadow: 0 0 15px rgba(206, 226, 246, 0.2);
  }
  .ant-list-item-meta {
    margin-bottom: 5px;
    .ant-list-item-meta-title {
      margin-bottom: 2px;
      a {
        color: #2e9dd2;
      }
    }
  }
}
.ant-list-empty-text {
  padding: 40px 0;
}
.ant-list-pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
.page_content {
  .tag_wrap {
    display: block;
    padding: 10px 20px;
    .title {
      font-size: 14px;
      margin-bottom: 20px;
    }
    .content {
      .tag {
        margin: 0 10px 10px 0;
        display: inline-block;
        padding: 1px 6px;
        background: rgba(0, 0, 0, 0.03);
        border-radius: 2px;
        transition: all 0.3s;
        > span {
          &:last-child {
            cursor: pointer;
            margin-left: 2px;
            display: inline-block;
            color: #2e9dd2;
          }
        }
        &:hover {
          opacity: 0.8;
        }
      }
    }
  }
}
@media (min-width: 831px) {
  .page_content {
    .tag_wrap {
      display: none;
    }
  }
}
</style>
