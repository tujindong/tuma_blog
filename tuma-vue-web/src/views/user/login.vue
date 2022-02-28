<template>
  <div class="page_content">
    <a-row style="width: 100%; height: 100%">
      <a-col :xs="0" :sm="0" :md="6" :lg="8"></a-col>
      <a-col :xs="24" :sm="24" :md="12" :lg="8">
        <div class="form_wrap">
          <div class="title">登录</div>
          <a-form
            :model="formState"
            :rules="rules"
            name="basic"
            @finish="handleLogin"
          >
            <a-form-item name="username">
              <a-input
                placeholder="请输入用户名"
                v-model:value="formState.username"
                allowClear
              >
                <template #prefix>
                  <user-outlined style="color: #cccccc" />
                </template>
              </a-input>
            </a-form-item>
            <a-form-item name="password">
              <a-input-password
                autocomplete="off"
                placeholder="请输入密码"
                v-model:value="formState.password"
                allowClear
              >
                <template #prefix>
                  <lock-outlined style="color: #cccccc" />
                </template>
              </a-input-password>
            </a-form-item>
            <div class="link">
              <router-link class="link_register" to="/register" replace
                >注册账号</router-link
              >
            </div>
            <a-form-item>
              <a-button
                shape="round"
                type="primary"
                html-type="submit"
                style="width: 100%"
                >登录</a-button
              >
            </a-form-item>
          </a-form>
        </div>
      </a-col>
      <a-col :xs="0" :sm="0" :md="6" :lg="8"></a-col>
    </a-row>
  </div>
</template>

<script>
import { reactive } from "vue";
import { UserOutlined, LockOutlined } from "@ant-design/icons-vue";
import { Form } from "ant-design-vue";
import { postAction } from "@/request/manage";
import { Local } from "@/utils/storage";
import { useRouter } from "vue-router";
export default {
  name: "login",
  components: {
    UserOutlined,
    LockOutlined,
  },
  setup() {
    const router = useRouter();
    const useForm = Form.useForm;
    const { validate } = useForm();

    const formState = reactive({
      username: "",
      password: "",
    });

    const rules = {
      username: [
        {
          required: true,
          trigger: "change",
          message: "请输入用户名",
        },
      ],
      password: [
        {
          required: true,
          trigger: "change",
          message: "请输入密码",
        },
      ],
    };

    const handleLogin = (values) => {
      validate().then(async () => {
        const params = values;
        const res = await postAction("/sys/login", params);
        Local.set("token", res.result.token);
        Local.set("userInfo", res.result.userInfo);
        router.back();
      });
    };

    return {
      formState,
      rules,
      handleLogin,
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
  .form_wrap {
    width: 80%;
    margin: 150px auto 0;
    padding: 10px 20px 20px;
    background: #ffffff;
    border-radius: 5px;
    .title {
      font-size: 15px;
      padding: 16px 0;
      text-align: center;
      font-weight: bold;
    }
    .link {
      display: flex;
      margin-bottom: 16px;
      .link_register {
        font-size: 13px;
        margin-left: auto;
        color: #2e9dd2;
      }
    }
  }
}
</style>
