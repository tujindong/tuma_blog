<template>
  <div class="panel">
    <div class="category_wrapper">
      <a-card title="分类" style="width: 100%" :bordered="false" size="small">
        <template #extra>
          <router-link class="more" :to="`/categoryAll`"> 更多 </router-link>
        </template>
        <p
          class="category"
          v-for="(item, index) in categoryList"
          :key="index"
          @click="handleCategoryClick(item)"
        >
          {{ item.name }}
        </p>
      </a-card>
    </div>
    <div class="tag_wrapper">
      <a-card
        title="热门标签"
        style="width: 100%"
        :bordered="false"
        size="small"
      >
        <template #extra>
          <router-link class="more" :to="`/tagsAll`"> 更多 </router-link>
        </template>
        <div class="tag_content">
          <a-checkable-tag
            v-for="(item, index) in tagList"
            :key="index"
            @change="(checked) => handleTagChange(item, checked)"
          >
            {{ item.name }}
          </a-checkable-tag>
        </div>
      </a-card>
    </div>
    <div
      class="anchor_wrapper"
      v-if="routePath == '/article' && articleTitles.length"
    >
      <a-card title="目录" style="width: 100%" :bordered="false" size="small">
        <a-anchor :offsetTop="60" :bounds="0">
          <a-anchor-link
            v-for="item in articleTitles"
            :href="`#${item.id}`"
            :title="item.title"
            :key="item.id"
          >
            <template v-if="item.children.length">
              <a-anchor-link
                v-for="subItem in item.children"
                :href="`#${subItem.id}`"
                :title="subItem.title"
                :key="subItem.id"
              >
                <template v-if="subItem.children.length">
                  <a-anchor-link
                    v-for="thirdItem in subItem.children"
                    :href="`#${thirdItem.id}`"
                    :title="thirdItem.title"
                    :key="thirdItem.id"
                  >
                  </a-anchor-link>
                </template>
              </a-anchor-link>
            </template>
          </a-anchor-link>
        </a-anchor>
      </a-card>
    </div>
  </div>
</template>

<script>
import { computed, onMounted, reactive, toRefs, watch } from "vue";
import { getAction } from "@/request/manage";
import { useRouter, useRoute } from "vue-router";
import { useStore } from "vuex";

export default {
  name: "panel",
  components: {},
  setup() {
    const router = useRouter();
    const route = useRoute();
    const store = useStore();

    const state = reactive({
      tagList: [],
      categoryList: [],
      routePath: "",
    });

    const selectionTag = computed(() => store.state.search.selectionTag);
    const articleTitles = computed(() => store.state.article.articleTitles);

    watch(
      () => route.path,
      () => {
        init();
      }
    );

    onMounted(() => {
      getCategoryList();
      getTagList();
      init();
    });

    const init = () => {
      state.routePath = route.path;
    };

    //获取分类列表
    const getCategoryList = async () => {
      const res = await getAction("/blog/category/pageList", {
        pageNo: 1,
        pageSize: 5,
      });
      state.categoryList = res.result.records;
    };

    //获取标签列表
    const getTagList = async () => {
      const res = await getAction("/blog/tag/pageList", {
        pageNo: 1,
        pageSize: 6,
      });
      state.tagList = res.result.records;
    };

    //选择热门分类
    const handleCategoryClick = (item) => {
      const pageRoute = {
        path: "/category",
        query: { categoryId: item.id, categoryName: item.name },
      };
      route.path == "/category"
        ? router.replace(pageRoute)
        : router.push(pageRoute);
    };

    //选择热门标签
    const handleTagChange = (item) => {
      const pageRoute = {
        path: "/tags",
        query: { tagId: item.id, tagName: item.name },
      };
      route.path == "/tags"
        ? router.replace(pageRoute)
        : router.push(pageRoute);
    };

    return {
      ...toRefs(state),
      selectionTag,
      articleTitles,
      handleCategoryClick,
      handleTagChange,
    };
  },
};
</script>

<style lang="less" scoped>
.panel {
  padding: 0 10px;
  height: 100%;
  :deep(.ant-card-head-title) {
    font-weight: bold;
    color: #555555;
  }
  .more {
    color: rgba(0, 0, 0, 0.45);
    font-size: 12px;
  }
  .category_wrapper {
    margin-top: 10px;
    .category {
      cursor: pointer;
      transition: all 0.3s;
      &:hover {
        opacity: 0.8;
      }
    }
    .more {
      transition: all 0.3s;
      &:hover {
        opacity: 0.9;
      }
    }
  }
  .tag_wrapper {
    margin-top: 20px;
    .tag_content {
      .ant-tag {
        margin-bottom: 5px;
        transition: all 0.3s;
      }
      .ant-tag-checkable-checked,
      .ant-tag-checkable:not(.ant-tag-checkable-checked):hover {
        background: #2e9dd2;
        color: #ffffff;
        opacity: 0.8;
      }
    }
    .more {
      transition: all 0.3s;
      &:hover {
        opacity: 0.9;
      }
    }
  }
  .anchor_wrapper {
    margin-top: 20px;
    position: -webkit-sticky;
    position: sticky;
    top: 55px;
    transition: top 0.2s ease-in-out;
    animation: FadeUp 0.8s;
    :deep(.ant-card-head) {
      border-bottom: none;
    }
    :deep(.ant-anchor-ink::before) {
      width: 1px;
    }
    :deep(.ant-anchor-ink-ball) {
      border-color: #2e9dd2;
    }
    :deep(.ant-anchor-link-active > .ant-anchor-link-title) {
      color: #2e9dd2;
    }
    :deep(.ant-card-body) {
      padding: 12px 0;
    }
  }
  @keyframes FadeUp {
    from {
      opacity: 0;
      position: relative;
      top: 1.5rem;
    }

    to {
      opacity: 1;
      position: relative;
      top: 0;
    }
  }
}
</style>
