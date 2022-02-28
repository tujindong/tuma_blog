<template>
  <div class="component_name">
    <a-card :bordered="false">
      <a-form layout="vertical" :form="form">
        <a-form-item label="文章标题">
          <a-input
            placeholder="请输入文章标题"
            v-decorator="[
              'title',
              {
                rules: [{ required: true, message: '请输入30字以内的文章标题!', max: 30, whitespace: true }],
              },
            ]"
          />
        </a-form-item>
        <a-form-item label="文章简介">
          <a-textarea placeholder="请输入文章简介" v-decorator="['intro', { rules: [] }]" />
        </a-form-item>
        <a-form-item label="文章分类">
          <a-select style="width: 100%" placeholder="选择文章分类" v-decorator="['categoryId', { rules: [] }]">
            <a-select-option v-for="item in categoryList" :key="item.id">
              {{ item.name }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="文章标签">
          <a-select
            mode="multiple"
            style="width: 100%"
            placeholder="选择文章标签"
            v-decorator="['tagIds', { rules: [] }]"
          >
            <a-select-option v-for="item in tagList" :key="item.id">
              {{ item.name }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="文章内容">
          <JWangEditor v-decorator="['content', { rules: [], initialValue: '' }]"></JWangEditor>
        </a-form-item>
      </a-form>

      <div class="btn_wrap">
        <a-button style="margin-right: 20px" @click="handleCancel">取消</a-button>
        <a-button style="margin-right: 20px" @click="handleSave">保存</a-button>
        <a-button type="primary" @click="handleOk">确定</a-button>
      </div>
    </a-card>
  </div>
</template>

<script>
import JWangEditor from '@/components/jeecg/JWangEditor'
import pick from 'lodash.pick'
import { getAction, postAction, putAction } from '@/api/manage'
export default {
  name: 'component_name',
  components: {
    JWangEditor,
  },
  data() {
    return {
      url: {
        tagList: '/blog/tag/list',
        categoryList: '/blog/category/list',
        add: '/blog/article/add',
        edit: '/blog/article/edit',
        queryById: '/blog/article/queryById',
      },
      form: this.$form.createForm(this),
      model: {},
      tagList: [],
      categoryList: [],
      articleId: '',
    }
  },

  computed: {},

  watch: {
    $route: 'initFormData',
  },

  created() {
    this.getTagList()
    this.getCategoryList()
  },

  mounted() {
    this.initFormData()
  },

  methods: {
    initFormData() {
      this.articleId = this.$route.query.articleId
      getAction(this.url.queryById, { id: this.articleId }).then((res) => {
        const result = res.result
        this.model = { ...this.model, ...result }
        this.model.tagIds = result.tagList.map((item) => item.id)
        const fieldList = ['title', 'tagIds', 'intro', 'categoryId', 'content']
        this.form.setFieldsValue(pick(this.model, fieldList))
      })
    },

    //获取标签列表
    getTagList() {
      getAction(this.url.tagList, {})
        .then((res) => {
          const result = res.result
          this.tagList = result
        })
        .finally(() => {})
    },

    //获取分类列表
    getCategoryList() {
      getAction(this.url.categoryList, {})
        .then((res) => {
          const result = res.result
          this.categoryList = result
        })
        .finally(() => {})
    },

    handleSave() {
      this.form.validateFields((err, values) => {
        if (!err) {
          if (values.tagIds) values.tagIds = values.tagIds.join(',')
          const formData = { ...this.model, ...values }
          putAction(this.url.edit, formData)
            .then((res) => {
              if (res.success) {
                this.$message.success(res.message)
              } else {
                this.$message.warning(res.message)
              }
            })
            .finally(() => {})
        }
      })
    },

    handleOk() {
      this.form.validateFields((err, values) => {
        if (!err) {
          if (values.tagIds) values.tagIds = values.tagIds.join(',')
          const formData = { ...this.model, ...values }
          // console.log('formData', formData)
          // return
          let action = !this.model.id ? postAction(this.url.add, formData) : putAction(this.url.edit, formData)
          action
            .then((res) => {
              if (res.success) {
                this.$message.success(res.message)
                this.$emit('ok')
                this.handleCancel()
              } else {
                this.$message.warning(res.message)
              }
            })
            .finally(() => {})
        }
      })
    },

    handleCancel() {
      this.$router.go(-1)
    },
  },
}
</script>
<style lang='less' scoped>
.btn_wrap {
  padding-bottom: 50px;
}
/deep/.w-e-text-container {
  height: 800px !important;
}
</style>