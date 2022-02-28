<template>
  <div class="page_content">
    <a-row style="width: 100%; height: 100%">
      <a-col :xs="0" :sm="0" :md="6" :lg="8"></a-col>
      <a-col :xs="24" :sm="24" :md="12" :lg="8">
        <div class="form_wrap">
          <div class="title">注册</div>
          <a-form
            :model="formState"
            :rules="rules"
            name="basic"
            autocomplete="off"
            @finish="handleRegister"
          >
            <a-form-item name="username">
              <a-input
                placeholder="请输入用户名"
                v-model:value="formState.username"
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
            <a-form-item name="passwordConfirm">
              <a-input-password
                autocomplete="off"
                placeholder="请确认密码"
                v-model:value="formState.passwordConfirm"
                allowClear
              >
                <template #prefix>
                  <lock-outlined style="color: #cccccc" />
                </template>
              </a-input-password>
            </a-form-item>
            <div class="link">
              <router-link class="link_register" to="/login" replace
                >已有账号登录</router-link
              >
            </div>
            <a-form-item>
              <a-button
                shape="round"
                type="primary"
                html-type="submit"
                style="width: 100%"
                >注册</a-button
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
import { postAction } from "@/request/manage";
import { Form, message } from "ant-design-vue";
import { useRouter } from "vue-router";
export default {
  name: "user",
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
      passwordConfirm: "",
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
      passwordConfirm: [
        {
          validator: async (_rule, value) => {
            if (value !== formState.password) {
              return Promise.reject("两次输入密码不一致!");
            } else {
              return Promise.resolve();
            }
          },
          trigger: "change",
        },
      ],
    };

    const handleRegister = (values) => {
      validate().then(async () => {
        const params = {
          username: values.username,
          password: values.password,
        };
        const res = await postAction("/sys/user/register", params);
        if (res.code == 200) {
          router.replace({
            path: "/registerResult",
            query: { username: values.username },
          });
        } else {
          message.warning(res.message);
        }
      });
    };

    return {
      formState,
      rules,
      handleRegister,
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
    box-shadow: 0 0 5px #ffffff;
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
