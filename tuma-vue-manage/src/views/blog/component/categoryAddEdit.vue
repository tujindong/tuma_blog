<template>
  <div class="tagAddEdit">
    <a-modal
      title="分类编辑"
      :width="800"
      :visible="visible"
      :destroyOnClose="true"
      @ok="handleOk"
      @cancel="handleCancel"
      cancelText="关闭"
      :maskClosable="false"
    >
      <a-spin :spinning="confirmLoading">
        <a-form :form="form">
          <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="分类名称">
            <a-input
              placeholder="请输入分类名称"
              v-decorator="[
                'name',
                {
                  rules: [{ required: true, message: '请输入30字以内的分类名称!', max: 30, whitespace: true }],
                },
              ]"
            />
          </a-form-item>
          <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="分类描述">
            <a-textarea
              placeholder="请输入分类描述"
              v-decorator="[
                'description',
                {
                  rules: [{ required: true, message: '请输入分类名称!', max: 30, whitespace: true }],
                },
              ]"
            />
          </a-form-item>
        </a-form>
      </a-spin>
    </a-modal>
  </div>
</template>

<script>
const labelCol = {
  xs: { span: 24 },
  sm: { span: 5 },
}
const wrapperCol = {
  xs: { span: 24 },
  sm: { span: 16 },
}
import pick from 'lodash.pick'
import { putAction, postAction } from '@/api/manage'

export default {
  name: 'tagAddEdit',

  data() {
    return {
      url: {
        add: '/blog/category/add',
        edit: '/blog/category/edit',
      },
      visible: false,
      confirmLoading: false,
      labelCol,
      wrapperCol,
      form: this.$form.createForm(this),
      record: {},
    }
  },

  props: {},

  components: {},

  computed: {},

  mounted() {},

  methods: {
    add() {
      this.edit({})
    },

    edit(record) {
      this.visible = true
      this.record = { ...{}, ...record }
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.record, ['name', 'description']))
      })
    },

    view(record) {},

    handleOk() {
      this.form.validateFields((err, values) => {
        if (!err) {
          const formData = { ...this.record, ...values }
          // console.log('formData', formData)
          // return
          this.confirmLoading = true
          let action = !this.record.id ? postAction(this.url.add, formData) : putAction(this.url.edit, formData)
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
            .finally(() => {
              this.confirmLoading = false
            })
        }
      })
    },

    handleCancel() {
      this.visible = false
    },
  },
}
</script>
<style lang='less' scoped>
</style>