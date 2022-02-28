<template>
  <div class="page_content">
    <div class="box_wrap">
      <div
        class="box shadow"
        v-for="(item, index) in tagList"
        :key="index"
        @click="handleTagChange(item)"
      >
        <a> {{ item.name }}</a>
        <div class="circle"></div>
      </div>
    </div>
  </div>
</template>

<script>
import { onMounted, reactive, toRefs } from "vue";
import { getAction } from "@/request/manage";
import { useRouter } from "vue-router";
export default {
  name: "tagsAll",
  components: {},
  setup() {
    const router = useRouter();

    const state = reactive({
      tagList: [],
    });

    onMounted(() => {
      getTagList();
    });

    const getTagList = async () => {
      const res = await getAction("/blog/tag/list", {});
      state.tagList = res.result;
    };

    const handleTagChange = (item) => {
      router.push({
        path: "/tags",
        query: { tagId: item.id, tagName: item.name },
      });
    };

    return {
      ...toRefs(state),
      handleTagChange,
    };
  },
};
</script>

<style lang="less">
.page_content {
  padding: 20px 5%;
  .box_wrap {
    display: flex;
    width: 90%;
    margin: 0px auto 0;
    flex-wrap: wrap;
    .box {
      flex: 1;
      width: 180px;
      display: block;
      height: 50px;
      position: relative;
      border-radius: 5px;
      background: linear-gradient(
        to right,
        rgba(46, 157, 210, 1) 35%,
        rgba(100, 205, 253, 1) 100%
      );
      margin: 10px;
      padding: 15px 25px 0 40px;
      color: darkslategray;
      box-shadow: 1px 2px 1px -1px #777;
      transition: background 200ms ease-in-out;
      text-align: right;
      &.shadow {
        position: relative;
        &:before {
          z-index: -1;
          position: absolute;
          content: "";
          bottom: 13px;
          right: 7px;
          width: 75%;
          top: 0;
          box-shadow: 0 15px 10px #777;
          -webkit-transform: rotate(4deg);
          transform: rotate(4deg);
          transition: all 150ms ease-in-out;
        }
        &:hover {
          background: linear-gradient(
            to right,
            rgba(46, 157, 210, 1) 0%,
            rgba(100, 205, 253, 1) 100%
          );
        }
        &:hover::before {
          -webkit-transform: rotate(0deg);
          transform: rotate(0deg);
          bottom: 20px;
          z-index: -10;
        }
      }
      > a {
        color: #fff;
        text-decoration: none;
      }
      .circle {
        position: absolute;
        top: 14px;
        left: 15px;
        border-radius: 50%;
        box-shadow: inset 1px 1px 1px 0px rgba(0, 0, 0, 0.5),
          inset 0 0 0 25px #ffffff;
        width: 20px;
        height: 20px;
        display: inline-block;
        text-align: right;
        padding: 0 20px 0 0;
      }
    }
  }
}
@media (max-width: 831px) {
  .page_content {
    .box_wrap {
      .box {
        width: 100%;
        min-width: 250px;
      }
    }
  }
}
</style>
