<template>
  <a-breadcrumb>
    <a-breadcrumb-item v-for="(item, index) in breadcrumbList" :key="index">
      <router-link
        class="breadcrumb_pointer"
        v-if="breadcrumbList.length > index + 1"
        :to="item.path"
      >
        {{ item.meta.title }}
      </router-link>
      <span v-else>{{ item.meta.title }}</span>
    </a-breadcrumb-item>
  </a-breadcrumb>
</template>

<script>
export default {
  name: "breadcrumb",
  components: {},
  props: {
    breadcrumbList: {
      type: Array,
      default() {
        return [];
      },
    },
  },
  setup() {},
};
</script>

<style lang="less" scoped>
:deep(.breadcrumb_pointer) {
  color: rgba(0, 0, 0, 0.45);
  &:hover {
    color: #2e9dd2;
  }
}
</style>
