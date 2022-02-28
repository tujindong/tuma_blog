<template>
  <div class="page_content">
    <a-card :bordered="false">
      <!-- 表单搜索-->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="6" :sm="8">
              <a-form-item label="文章标题">
                <a-input placeholder="请输入文章标题" v-model="queryParam.title"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="发布状态">
                <a-select placeholder="请选择发布状态" v-model="queryParam.status">
                  <a-select-option value="0">暂存</a-select-option>
                  <a-select-option value="1">已发布</a-select-option>
                </a-select>
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
        <a-button @click="handleArticleAdd" type="primary" icon="plus">新增</a-button>
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
          <template slot="articleTitle" slot-scope="articleTitle">
            <Ellipsis :length="40" tooltip>
              {{ articleTitle }}
            </Ellipsis>
          </template>

          <template slot="intro" slot-scope="intro">
            <Ellipsis :length="40" tooltip>
              {{ intro }}
            </Ellipsis>
          </template>

          <template slot="status" slot-scope="status">
            <a-badge :status="status | statusTypeFilter" :text="status | statusTextFilter" />
          </template>

          <template slot="tagNames" slot-scope="tagNames">
            <span v-if="tagNames">
              <a-tag v-for="(item, index) in tagNames.split(',')" :key="index" color="green">{{ item }}</a-tag>
            </span>
            <span v-else>-</span>
          </template>

          <span slot="action" slot-scope="text, record">
            <a @click="handleArticleEdit(record)">编辑</a>

            <a-divider type="vertical" />

            <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
              <a>删除</a>
            </a-popconfirm>

            <template v-if="record.status == '0'">
              <a-divider type="vertical" />

              <a-popconfirm title="确定发布吗?" @confirm="() => handleRelease(record.id)">
                <a>发布</a>
              </a-popconfirm>
            </template>

            <template v-if="record.status == '1'">
              <a-divider type="vertical" />

              <a-popconfirm title="确定撤回吗?" @confirm="() => handleWithdraw(record.id)">
                <a>撤回</a>
              </a-popconfirm>
            </template>
          </span>
        </a-table>
      </div>
    </a-card>
  </div>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import Ellipsis from '@/components/Ellipsis/Ellipsis'
import { postAction } from '@/api/manage'

const columns = [
  {
    title: '标题',
    dataIndex: 'title',
    key: 'title',
    width: 150,
    scopedSlots: { customRender: 'articleTitle' },
  },
  {
    title: '简介',
    dataIndex: 'intro',
    key: 'intro',
    width: 150,
    scopedSlots: { customRender: 'intro' },
  },
  {
    title: '标签',
    dataIndex: 'tagNames',
    key: 'tagNames',
    width: 150,
    scopedSlots: { customRender: 'tagNames' },
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    key: 'createTime',
    width: 150,
  },
  {
    title: '修改时间',
    dataIndex: 'updateTime',
    key: 'updateTime',
    width: 150,
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    width: 80,
    scopedSlots: { customRender: 'status' },
  },
  {
    title: '操作',
    width: 220,
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
        list: '/blog/article/pageListWithTags',
        delete: '/blog/article/delete',
        release: '/blog/article/release',
        withdraw: '/blog/article/withdraw',
      },
      queryParam: {
        title: '',
        status: undefined,
      },
    }
  },

  components: {
    Ellipsis,
  },

  filters: {
    statusTypeFilter(value) {
      const statusMap = {
        0: 'default',
        1: 'success',
      }
      return statusMap[value]
    },

    statusTextFilter(value) {
      const statusMap = {
        0: '暂存',
        1: '已发布',
      }
      return statusMap[value]
    },
  },

  computed: {},

  mounted() {},

  methods: {
    handleArticleAdd() {
      this.$router.push({ path: '/blog/articleAdd' })
    },

    handleArticleEdit(record) {
      this.$router.push({ path: '/blog/articleAdd', query: { articleId: record.id } })
    },

    handleRelease(id) {
      postAction(this.url.release, { id }).then((res) => {
        if (res.code == 200) {
          this.$message.success(res.message)
          this.loadData()
        } else {
          this.$message.error(res.message)
        }
      })
    },

    handleWithdraw(id) {
      postAction(this.url.withdraw, { id }).then((res) => {
        if (res.code == 200) {
          this.$message.success(res.message)
          this.loadData()
        } else {
          this.$message.error(res.message)
        }
      })
    },
  },
}
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>