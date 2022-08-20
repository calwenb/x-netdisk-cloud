# x-netdisk-cloud

### 介绍:

X 云盘是基于 **Spring Cloud Alibaba**、 Spring Boot 、 Vue 开发的云盘系统，采用**前后端分离架构**、**微服务架构**。

此部分为后端项目，需运行前端项目。

### 网站地址：

http://pan.wenyo.top

### 主要功能:

- 用户注册登录（手机号登录注册、自动登录），JWT机制验证。
- 用户修改个人信息、上传头像、密码找回。
- 个人文件在线存储。
- 文件的下载、上传（支持断点续传，大文件秒传，多线程分片上传）。
- 文件以及文件夹：展示、增加、删除、重命、目录树。
- 文件的分类展示、文件搜索（基于Elasticsearch）。
- 图片缩略图展示。
- 在线浏览，编辑，修改文件（自带web编辑器，可在线编辑、提交文件）。
- 临时文件分享：文件生成文件码，二维码分享（基于Redis）。
- 回收站（基于Redis，zset）。



### 技术栈:

#### 前端：

- Vue、Element-ui

#### 后端：

- Spring Boot
- Mybatis
- Redis
- Elasticsearch 
- JWT
- releasedao（ 本人开发的**ORM框架**：https://github.com/calwenb/releasedao.git ）

**微服务：**

 **Spring Cloud Alibaba**、Nacos、Gateway、Ribbon、OpenFeign、Sentinel

#### 持久化：

- MySQL 5.7

#### 中间件：

- ElasticSearch
- Nginx负载均衡
- Redis缓存

#### 部署：

- 阿里云服务器、腾讯云服务器

#### 版本控制：

- Git

#### 其他：

- 阿里巴巴 Java 开发手册


### 安装教程

1.  创建my_pan_test数据库，将sql文件夹下my_pan_test.sql执行。
2.  配置application.yml 文件，修改mysql、redis、ElasticSearch连接信息。
3.  修改netdisc.store.root-path 为文件系统根目录。
4.  启动Nacos、Sentinel。
5.  依次启动项目中的nacos，sentinel，seata组件。
6.  依次启动netdisc-gateway、netdisc-oauth、netdisc-service-user、netdisc-service-filesystem。
7.  待Nacos服务列表已全部注册，访问127.0.0.1:9500访问Gateway暴露的API接口。
