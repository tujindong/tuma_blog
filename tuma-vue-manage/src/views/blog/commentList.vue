<template>
  <div class="page_content">
    <a-card :bordered="false">
      <!-- 表单搜索-->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="6" :sm="8">
              <a-form-item label="评论内容">
                <a-input placeholder="请输入评论内容" v-model="queryParam.content"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="评论文章">
                <a-input placeholder="请输入文章标题" v-model="queryParam.articleTitle"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
                <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              </span>
            </a-col>
          </a-row>
        </a-form>
      </div>

      <!-- 操作按钮区域 -->
      <div class="table-operator">
        <a-dropdown v-if="selectedRowKeys.length > 0">
          <a-menu slot="overlay">
            <a-menu-item key="1" @click="batchDel">
              <a-icon type="delete" />
              删除
            </a-menu-item>
          </a-menu>
          <a-button style="margin-left: 8px">
            批量操作
            <a-icon type="down" />
          </a-button>
        </a-dropdown>
      </div>

      <div>
        <div class="ant-alert ant-alert-info" style="margin-bottom: 16px">
          <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择
          <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
          >项
          <a style="margin-left: 24px" @click="onClearSelected">清空</a>
        </div>

        <a-table
          ref="table"
          size="middle"
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          :loading="loading"
          :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
          @change="handleTableChange"
        >
          <span slot="action" slot-scope="text, record">
            <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
              <a>删除</a>
            </a-popconfirm>
          </span>
        </a-table>
      </div>
    </a-card>
  </div>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin'

const columns = [
  {
    title: '评论内容',
    dataIndex: 'content',
    key: 'content',
    width: 200,
  },
  {
    title: '文章',
    dataIndex: 'articleTitle',
    key: 'articleTitle',
    width: 200,
  },
  {
    title: '评论用户',
    dataIndex: 'createBy',
    key: 'createBy',
    width: 100,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    key: 'createTime',
    width: 180,
  },
  {
    title: '修改时间',
    dataIndex: 'updateTime',
    key: 'updateTime',
    width: 180,
  },
  {
    title: '操作',
    width: 200,
    dataIndex: 'action',
    align: 'center',
    scopedSlots: { customRender: 'action' },
  },
]
export default {
  name: 'voteListManage',

  mixins: [JeecgListMixin],

  data() {
    return {
      columns,
      url: {
        list: '/blog/comment/pageListWithArticle',
        delete: '/blog/comment/delete',
        deleteBatch: '/blog/comment/deleteBatch',
      },
      queryParam: {
        title: '',
        status: undefined,
      },
    }
  },

  components: {},

  computed: {},

  mounted() {},

  methods: {},
}
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>