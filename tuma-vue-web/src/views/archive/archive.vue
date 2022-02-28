<template>
  <div class="page_content">
    <a-timeline>
      <template v-for="(item, index) in articleList" :key="index">
        <a-timeline-item v-if="item.isParent">
          <template #dot>
            <ClockCircleOutlined style="font-size: 15px; color: #2e9dd2" />
          </template>
          <div class="year">{{ item.createYear }}</div>
        </a-timeline-item>
        <a-timeline-item class="child" v-else>
          <span class="article" @click="handleGoArticle(item)">
            <span>{{ item.createDate }}</span>
            <span>{{ item.title }}</span>
          </span>
        </a-timeline-item>
      </template>
    </a-timeline>
  </div>
</template>
<script>
import { defineComponent, reactive, toRefs, onMounted } from "vue";
import { getAction } from "@/request/manage";
import { cloneDeep } from "lodash";
import { ClockCircleOutlined } from "@ant-design/icons-vue";
import { useRouter } from "vue-router";
export default defineComponent({
  name: "archive",

  components: {
    ClockCircleOutlined,
  },

  setup() {
    const router = useRouter();

    const state = reactive({
      articleList: [],
    });

    onMounted(() => {
      getArticleList();
    });

    //获取文章列表 并分类
    const getArticleList = async () => {
      const res = await getAction("/blog/article/list", { status: 1 });
      const result = res.result;
      const sortList = [];
      result.forEach((item) => {
        let index = -1;
        item.createYear = item.createTime
          ? item.createTime.slice(0, 4)
          : "yyyy";
        item.createDate = item.createTime
          ? item.createTime.slice(5, 11)
          : "mm-dd";
        sortList.some((newItem, j) => {
          if (item.createYear == newItem.createYear) {
            index = j;
            return true;
          }
        })
          ? sortList[index].children.push(item)
          : sortList.push({
              createYear: item.createYear,
              isParent: 1,
              children: [item],
            });
      });
      state.articleList = transformTreeToFlat(sortList);
    };

    const transformTreeToFlat = (tree, result = []) => {
      cloneDeep(tree).forEach((node) => {
        result.push(node);
        node.children && transformTreeToFlat(node.children, result);
      });
      return result.map((item) => {
        item.children && delete item.children;
        return { ...item };
      });
    };

    const handleGoArticle = (item) => {
      router.push({ path: "/article", query: { id: item.id } });
    };

    return {
      ...toRefs(state),
      getArticleList,
      handleGoArticle,
    };
  },
});
</script>
<style lang="less" scoped>
.page_content {
  padding: 40px 5%;
  .child {
    :deep(.ant-timeline-item-head) {
      border: 1px solid #2e9dd2;
    }
    .article {
      transition: all 0.3s;
      font-size: 14px;
      cursor: pointer;
      &:hover {
        color: #2e9dd2;
        text-decoration: underline;
      }
      span {
        display: inline-block;
        &:first-child {
          margin-right: 10px;
          color: #999999;
          font-size: 13px;
        }
      }
    }
  }
  .year {
    font-weight: 600;
    font-size: 15px;
  }
}
</style>
