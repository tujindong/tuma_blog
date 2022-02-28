<template>
  <div class="page_content">
    <a-result
      title="注册成功"
      :sub-title="`用户名：${username}；注册日期：${moment(new Date()).format(
        'YYYY/MM/DD'
      )}`"
    >
      <template #icon>
        <smile-twoTone />
      </template>
      <template #extra>
        <a-button @click="router.push({ path: '/home' })">回首页</a-button>
        <a-button
          type="primary"
          style="margin-left: 15px"
          @click="router.replace({ path: '/login' })"
          >去登录</a-button
        >
      </template>
    </a-result>
  </div>
</template>

<script>
import { SmileTwoTone } from "@ant-design/icons-vue";
import { reactive, toRefs, onMounted, watch } from "vue";
import { useRouter, useRoute } from "vue-router";
import moment from "moment";
export default {
  name: "user",
  components: {
    SmileTwoTone,
  },
  setup() {
    const router = useRouter();
    const route = useRoute();

    const state = reactive({
      username: "",
    });

    onMounted(() => {
      initPageData();
    });

    watch(
      () => route.query.username,
      () => {
        initPageData();
      }
    );

    const initPageData = () => {
      state.username = route.query.username;
    };

    return {
      ...toRefs(state),
      moment,
      router,
    };
  },
};
</script>

<style lang="less" scoped>
.page_content {
  width: 100%;
  height: 100%;
  background-color: #f8f8f8;
  background: linear-gradient(47deg, #f5f0c5, #f5d5eb, #bbe0f5, #ffdad3);
  background-size: 800% 800%;
  animation: AnimationName 12s ease infinite;
  @-webkit-keyframes AnimationName {
    0% {
      background-position: 0% 45%;
    }
    50% {
      background-position: 100% 56%;
    }
    100% {
      background-position: 0% 45%;
    }
  }
  @-moz-keyframes AnimationName {
    0% {
      background-position: 0% 45%;
    }
    50% {
      background-position: 100% 56%;
    }
    100% {
      background-position: 0% 45%;
    }
  }
  @keyframes AnimationName {
    0% {
      background-position: 0% 45%;
    }
    50% {
      background-position: 100% 56%;
    }
    100% {
      background-position: 0% 45%;
    }
  }
}
</style>
