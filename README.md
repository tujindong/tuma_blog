# vue3.0个人博客

博客项目参考借鉴了一些优秀的开源项目和个人博主文章，后台管理部分是在优秀的开源项目[jeecgboot](http://doc.jeecg.com)基础上编写，博客界面样式借鉴了[IOS大佬王巍onevcat](https://onevcat.com)的博客样式实现了自适应布局和部分功能。

项目链接地址，欢迎star ^_^：

[博客前端界面](http://120.76.216.29/)

[博客后台管理系统](http://120.76.216.29/admin)

[博客项目源码](https://github.com/tujindong/tuma_blog)

项目目录下主要分为三个部分分别对应：

> - tuma-parent：项目后端；
> - tuma-vue-manage：项目前端后台管理系统；
> - tuma-vue-web：项目前端博客界面；

## 前端项目

前端项目分为两个部分，博客前端项目：vue3.0（ant-design-vue@2.2.8、axios@0.21.1、vue-router@4.0.12、vuex@4.0.0-beta.4）；后台管理项目：jeecgboot开源框架vue2.0（ant-design-vue@1.7.2、vue-router@3.0.1、vuex@3.1.0）。

1.博客前端项目主要实现的功能有：

- 自适应PC和移动端的展示界面。
- 用户登录、注册。
- 首页文章列表、浏览量、点赞量、评论量的数据展示。
- 文章详情查看、锚点定位、搜索、点赞、评论。
- 文章分类、标签的筛选查看。
- 文章归档的时间轴展示。

2.博客后台管理主要实现的功能有：

- 文章的新增、编辑、暂存、发布、撤回、删除、批量删除、搜索。
- 分类的新增、编辑、删除、批量删除、搜索。
- 标签的新增、编辑、删除、批量删除、搜索。
- 评论的查询、删除、批量删除。

## 后端项目

后端项目也是采用了jeecgboot开源框架（mave@3.3.9、java@1.8、 springboot@2.3.5、redis@3.1.0、mybatis-plus@3.4.1、mysql@5.6），开发的接口也是对应前端项目和管理后台对应页面接口。

接口文档采用swagger，配置之后代码编写完毕也是可以快速生成在线文档方便查看和调试。

## 服务器部署
服务器使用的是阿里云服务器，装的系统是Alibaba Cloud Linux 64。为了支持项目运行，服务器配置了java环境、安装了mysql数据库以及nginx、node、redis、ftp等。安装部署过程中也是遇到不少问题，每个问题之后有时间也会出几期详细教程。

# 项目启动
clone 博客的后端、前端、后台管理项目目录到本地并安装各自的依赖。

## 后端项目

1. 本机安装配置好java1.8、maven3.3.9、数据库mysql5.7、redis、navicat、nginx等工具和环境。
2. 本地数据库运行tuma-parent/db/jeecgboot-mysql-5.7-20220228.sql文件配置数据环境。
3. 启动mysql、redis服务。
4. IntelliJ IDEA导入tuma-parent的springboot项目，配置数据库连接、redis端口等相关配置文件。
5. 启动后台服务。
## 前端项目

1. 后台管理项目、前端博客展示项目分别安装依赖包。
2. 改写request的接口请求地址为本地服务接口。
3. 启动前端项目。

至此，项目应该是可以正常启动。


****
	
|Author|涂小图|
|---|---
|E-mail|1965382331@qq.com

****
