<template>
  <div class="basic_layout" :class="isSideShow ? 'sider_show' : 'sider_hidden'">
    <div class="topbar_wrap">
      <!-- pc端 面包屑导航 -->
      <Breadcrumb class="bread_crumb_web" :breadcrumbList="breadcrumbList" />

      <!-- 移动端 切换菜单-->
      <menu-outlined
        class="menu_trigger_mobile"
        @click="isSideShow = !isSideShow"
      />

      <!-- 移动端 搜索-->
      <div v-show="isSearchBoxShow" class="search_container_mobile">
        <a-input
          class="input_search"
          placeholder="输入关键字搜索"
          :value="searchValue"
          @pressEnter="handleSearchConfirm"
          @change="handleSearchChange"
        >
          <template #prefix>
            <search-outlined
              style="color: #333333; cursor: pointer"
              @click="handleSearchConfirm"
            />
          </template>
        </a-input>
        <a-button type="link" @click="handleSearchBoxHide">取消</a-button>
      </div>

      <!-- 移动端 页面标题-->
      <div class="title_mobile">{{ curNavTitle }}</div>

      <!-- 移动端 切换搜索-->
      <div v-show="!isSearchBoxShow" class="search_trigger_mobile">
        <search-outlined
          style="color: #555555; cursor: pointer; font-size: 18px"
          @click="handleSearchBoxShow"
        />
      </div>

      <!-- pc端 -->
      <a-input
        class="input_search_web"
        style="width: 18%"
        placeholder="输入关键字搜索"
        allowClear
        :value="searchValue"
        @pressEnter="handleSearchConfirm"
        @change="handleSearchChange"
      >
        <template #prefix>
          <search-outlined
            style="color: #333333; cursor: pointer"
            @click="handleSearchConfirm"
          />
        </template>
      </a-input>
    </div>

    <Sider
      :menuList="menuList"
      :selectedKey="selectedKey"
      @onClick="handleMenuClick"
    />

    <div class="main_wrapper">
      <a-row style="width: 100%; height: 100%">
        <a-col :xs="24" :sm="24" :md="18" :lg="18">
          <div class="page">
            <transition name="fade">
              <router-view />
            </transition>
          </div>
        </a-col>
        <a-col :xs="0" :sm="0" :md="6" :lg="6"><Panel /></a-col>
      </a-row>
    </div>

    <div class="mask" @click="isSideShow = false"></div>

    <a-back-top>
      <div class="backTop_wrapper">
        <arrow-up-outlined
          style="color: #ffffff; font-size: 20px; font-weight: bolder"
        />
      </div>
    </a-back-top>
  </div>
</template>
<script>
import {
  defineComponent,
  reactive,
  toRefs,
  watch,
  computed,
  onMounted,
} from "vue";
import { useRouter, useRoute } from "vue-router";
import { useStore } from "vuex";
import {
  ArrowUpOutlined,
  MenuOutlined,
  SearchOutlined,
} from "@ant-design/icons-vue";
import Sider from "./sider.vue";
import Panel from "./panel";
import Breadcrumb from "./breadcrumb.vue";

export default defineComponent({
  components: {
    ArrowUpOutlined,
    MenuOutlined,
    SearchOutlined,
    Sider,
    Panel,
    Breadcrumb,
  },

  setup() {
    const router = useRouter();
    const route = useRoute();
    const store = useStore();
    const menuList = router.getRoutes().filter((item) => item.meta.isMenu);
    const state = reactive({
      menuList,
      breadcrumbList: [],
      selectedKey: "/home",
      isSideShow: false,
      searchValue: "",
      curNavTitle: "",
    });

    const isSearchBoxShow = computed(() => store.state.search.isSearchBoxShow);

    onMounted(() => {
      initBreadcrumbList();
    });

    watch(
      () => route.path,
      () => {
        initBreadcrumbList();
      }
    );

    const initBreadcrumbList = () => {
      //监听路由变化，渲染面包屑导航
      const breadcrumbList = router
        .getRoutes()
        .filter((item) => item.meta.isBreadcrumb)
        .filter((item) => item.path == route.path);
      if (breadcrumbList[0].path !== "/home") {
        breadcrumbList.unshift(menuList.find((item) => item.path == "/home"));
      }
      if (route.path !== "/search") {
        store.dispatch("setIsSearchBoxShow", false);
      }
      state.selectedKey = route.path;
      state.breadcrumbList = breadcrumbList;
      state.curNavTitle = route.meta.title;
    };

    const handleMenuClick = (param) => {
      state.selectedKey = param.path;
      router.push({ path: param.path });
    };

    //mobile 显示搜索框
    const handleSearchBoxShow = () => {
      store.dispatch("setIsSearchBoxShow", true);
      state.searchValue = "";
      router.push({ path: "/search" });
    };

    //mobile 隐藏搜索框
    const handleSearchBoxHide = () => {
      store.dispatch("setIsSearchBoxShow", false);
      state.searchValue = "";
      router.go(-1);
    };

    const handleSearchChange = (e) => {
      const searchValue = e.target.value;
      state.searchValue = searchValue;
      if (!searchValue && route.path == "/search") {
        router.go(-1);
      }
    };

    //导航搜索框确认搜索
    const handleSearchConfirm = () => {
      if (route.path == "/search") {
        router.replace({
          path: "/search",
          query: { searchKey: state.searchValue },
        });
      } else {
        router.push({
          path: "/search",
          query: { searchKey: state.searchValue },
        });
      }
    };

    return {
      ...toRefs(state),
      isSearchBoxShow,
      handleMenuClick,
      handleSearchBoxShow,
      handleSearchBoxHide,
      handleSearchChange,
      handleSearchConfirm,
    };
  },
});
</script>
<style lang="less">
.basic_layout {
  display: flex;
  width: 100%;
  height: 100%;
  &.sider_show {
    .siderBar {
      transform: translateX(0px);
      -webkit-transform: translateX(0px);
    }
    .topbar_wrap,
    .main_wrapper {
      transform: translateX(260px);
      -webkit-transform: translateX(260px);
    }
    .mask {
      display: block;
    }
  }
  .topbar_wrap {
    position: relative;
    display: flex;
    width: 100%;
    height: 50px;
    padding: 0 20px 0 10px;
    align-items: center;
    position: fixed;
    top: 0;
    left: 0;
    z-index: 50;
    border-bottom: 1px solid rgba(0, 0, 0, 0.07);
    background: #ffffff;
    transition: transform 0.4s ease;
    -webkit-transition: transform 0.4s ease;
    justify-content: space-between;
    .bread_crumb_web {
      display: none;
    }
    .menu_trigger_mobile {
      display: block;
      cursor: pointer;
    }
    .title_mobile {
      display: block;
    }
    .search_container_mobile {
      display: flex;
      align-items: center;
      justify-content: space-between;
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      padding: 0 10px;
      background: #ffffff;
      .input_search {
        border-radius: 16px;
        width: 85%;
      }
      .ant-btn-link {
        color: #2e9dd2;
      }
    }
    .input_search_web {
      margin-left: auto;
      border-radius: 16px;
      display: none;
    }
    .search_wrap_mobile {
      width: 100%;
    }
  }
  .main_wrapper {
    width: 100%;
    padding-top: 50px;
    transition: transform 0.4s ease;
    -webkit-transition: transform 0.4s ease;
    .page {
      padding: 10px 5px;
    }
  }
  .mask {
    display: none;
    position: fixed;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    height: 100%;
    width: 100%;
    z-index: 1;
  }
  .backTop_wrapper {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 42px;
    height: 42px;
    box-shadow: 0 0 1px #2e9dd2;
    opacity: 0.35;
    border-radius: 50%;
    background: #2e9dd2;
  }
}
@media (min-width: 831px) {
  .basic_layout {
    .topbar_wrap,
    .main_wrapper {
      width: calc(100% - 260px);
      transform: translateX(260px);
      -webkit-transform: translateX(260px);
    }
    .topbar_wrap {
      .bread_crumb_web {
        display: block;
      }
      .menu_trigger_mobile {
        display: none;
      }
      .title_mobile {
        display: none;
      }
      .input_search_web {
        display: inline-flex;
      }
      .search_trigger_mobile {
        display: none;
      }
    }
  }
}
@media (max-width: 831px) {
  .basic_layout {
    overflow-x: hidden;
  }
}
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s ease 0s;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.fade-leave-from,
.fade-enter-to {
  opacity: 1;
}
</style>
