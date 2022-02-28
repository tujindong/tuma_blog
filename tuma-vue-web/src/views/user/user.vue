<template>
  <div class="page_content">
    <a-card title="简历" style="width: 100%" :bordered="false" size="small">
      我叫凃锦东，一名开发者，现在专注于前端部分内容开发。
    </a-card>
    <div class="white_space"></div>
    <a-card
      title="文章分类解释"
      style="width: 100%"
      :bordered="false"
      size="small"
    >
      <a-list
        item-layout="horizontal"
        :dataSource="categoryList"
        :split="false"
      >
        <template #renderItem="{ item }">
          <a-list-item>
            <a-list-item-meta :description="item.description">
              <template #title>
                <span>{{ item.name }}</span>
              </template>
            </a-list-item-meta>
          </a-list-item>
        </template>
      </a-list>
    </a-card>
  </div>
</template>

<script>
import { reactive, toRefs, onMounted } from "vue";
import { getAction } from "@/request/manage";
export default {
  name: "user",

  components: {},

  setup() {
    const state = reactive({
      categoryList: [],
    });

    onMounted(() => {
      getCategoryList();
    });

    //获取分类列表
    const getCategoryList = async () => {
      const res = await getAction("/blog/category/list", {});
      state.categoryList = res.result;
    };

    return {
      ...toRefs(state),
    };
  },
};
</script>

<style lang="less" scoped>
.page_content {
  padding: 20px 5%;
  :deep(.ant-card-head-title) {
    font-weight: bold;
    color: #555555;
  }
  .white_space {
    height: 40px;
  }
}
</style>
