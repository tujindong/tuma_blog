<template>
  <div class="siderBar">
    <div class="sider_avatar">
      <div class="avatar">
        <img src="@/assets/avatar.jpg" alt="头像" />
      </div>
      <div class="name">Tuu's Den</div>
    </div>
    <div class="user_wrapper">
      <div class="user">{{ userInfo?.username }}</div>
      <div class="btn" v-if="userInfo?.username">
        <a-tooltip>
          <template #title>退出登录</template>
          <LogoutOutlined
            @click="handleLogout"
            style="font-size: 13px; color: 'rgba(255,255,255)'; cursor: pointer"
          />
        </a-tooltip>
      </div>
    </div>
    <div class="sider_menu">
      <div class="menu">
        <div
          class="munu_item"
          :class="item.path == selectedKey ? 'active' : null"
          v-for="(item, index) in menuList"
          :key="index"
          @click="handleMenuClick(item)"
        >
          <div></div>
          <div class="menu_item_text">{{ item.meta.title }}</div>
        </div>
      </div>
    </div>
    <div class="sider_info">
      <div class="list">
        <div class="item" v-for="(item, index) in links" :key="index">
          <component
            :is="item.type"
            style="font-size: 22px; color: #ffffff; cursor: pointer"
            @click="handleGoLink(item)"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { defineComponent, toRefs, reactive } from "vue";
import {
  GithubOutlined,
  ZhihuOutlined,
  WeiboOutlined,
  WechatOutlined,
  QqOutlined,
  LogoutOutlined,
} from "@ant-design/icons-vue";
import { message, notification } from "ant-design-vue";
import { Local } from "@/utils/storage";
import { postAction } from "@/request/manage";
export default defineComponent({
  name: "sider",

  components: {
    GithubOutlined,
    ZhihuOutlined,
    WeiboOutlined,
    WechatOutlined,
    QqOutlined,
    LogoutOutlined,
  },

  props: {
    menuList: {
      type: Array,
      default() {
        return [];
      },
    },
    selectedKey: {
      type: String,
      default() {
        return "";
      },
    },
  },

  setup(props, context) {
    const state = reactive({
      userInfo: Local.get("userInfo"),
    });

    const links = [
      {
        type: "GithubOutlined",
        url: "https://github.com/tujindong",
        title: "github",
        desc: "github",
      },
      {
        type: "ZhihuOutlined",
        url: "https://www.zhihu.com/people/tujindong",
        title: "知乎",
        desc: "知乎",
      },
      {
        type: "WechatOutlined",
        url: "",
        title: "微信",
        desc: "tu_jindong",
      },
      {
        type: "QqOutlined",
        url: "",
        title: "QQ",
        desc: "1965382331",
      },
      {
        type: "WeiboOutlined",
        url: "https://weibo.com/u/2394837023",
        title: "微博",
        desc: "微博",
      },
    ];

    const handleMenuClick = (param) => {
      context.emit("onClick", param);
    };

    const handleGoLink = (item) => {
      if (item.url) {
        window.open(item.url);
      } else {
        notification.open({
          message: item.title,
          description: item.desc,
          placement: "bottomLeft",
          duration: 10,
        });
      }
    };

    const handleLogout = () => {
      postAction("/sys/logout", {}).then(() => {
        Local.remove("token");
        Local.remove("userInfo");
        message.success("退出登录成功！");
        setTimeout(() => {
          window.location.reload();
        }, 1500);
      });
    };

    return {
      ...toRefs(state),
      handleMenuClick,
      handleGoLink,
      handleLogout,
      links,
    };
  },
});
</script>

<style lang="less">
@media (max-width: 830px) {
  .siderBar {
    transform: translateX(-260px);
    -webkit-transform: translateX(-260px);
  }
}
.siderBar {
  position: fixed;
  top: 0;
  left: 0;
  width: 260px;
  z-index: 100;
  height: 100%;
  background: linear-gradient(
    -72deg,
    rgb(48, 138, 151) 0%,
    rgb(6, 90, 163) 100%
  );
  display: flex;
  flex-direction: column;
  transition: transform 0.4s ease;
  -webkit-transition: transform 0.4s ease;
  .sider_avatar {
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 40px 0 0;
    flex-direction: column;
    .avatar {
      width: 80px;
      height: 80px;
      border: 2px solid rgba(255, 255, 255, 0.8);
      border-radius: 50%;
      overflow: hidden;
      img {
        width: 80px;
        height: 80px;
      }
    }
    .name {
      font-size: 20px;
      padding: 5px 0;
      color: #ffffff;
      opacity: 0.6;
    }
  }
  .user_wrapper {
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 0 10px;
    height: 20px;
    margin-bottom: 30px;
    .user {
      font-size: 11px;
      color: rgba(255, 255, 255, 0.3);
    }
    .btn {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 15px;
      height: 20px;
      border-radius: 50%;
      opacity: 0.3;
      transition: all 0.3s;
      margin-left: 5px;
      margin-top: -1px;
      color: rgba(255, 255, 255);
      &:hover {
        opacity: 0.8;
      }
    }
  }
  .sider_menu {
    .menu {
      .munu_item {
        cursor: pointer;
        font-size: 14px;
        line-height: 21px;
        color: #fff;
        padding: 12px 18px;
        display: block;
        text-decoration: none;
        transition: all 0.3s;
        &.active {
          background: linear-gradient(-72deg, #4fbecf 0%, #1466ae 100%);
        }
        &:hover {
          text-shadow: 0 0 12px #ffffff;
        }
      }
    }
  }
  .sider_info {
    position: absolute;
    bottom: 20px;
    width: 260px;
    .list {
      display: flex;
      width: 80%;
      margin: 0 auto;
      .item {
        flex: 1;
        .anticon {
          transition: all 0.3s;
          opacity: 0.6;
        }
        &:hover {
          .anticon {
            opacity: 1;
          }
        }
      }
    }
  }
}
</style>
