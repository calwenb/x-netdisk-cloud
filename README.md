# X-NETDISK-CLOUD

### 介绍:

X 云盘是基于 Spring Cloud Alibaba、 Spring Boot + Vue 开发的云盘系统，采用前后端分离架构、微服务架构，且适配移动端布局。此部分为后端项目，需运行前端项目。

### 主要功能:

用户注册登录、个人文件存储、文件管理（上传、下载、查看以及分类展示、重命名、删除）等功能，文件夹管理（查看、新建、删除、重命名、收录文件），支持关键字搜索用户文件、支持在线浏览文件与分享/下载临时文件。

- 用户管理:用户的注册、登录。
- 个人文件存储。
- 文件的上传、下载。
- 文件以及文件夹：展示、增加、删除、移动、重命名、复制。
- 文件的分类展示、文件搜索。
- 在线浏览文件。
- 临时文件分享：文件生成文件码，或者二维码分享。（无需登录）

网站地址：http://pan.wenyo.top

### 技术栈:

#### 前端：

- Vue、Element-ui

#### 后端：

- Spring Boot
- Mybatis
- Redis缓存
- Elasticsearch 
- JWT

**微服务：** Spring Cloud Alibaba、Nacos、Gateway、Ribbon、OpenFeign、Sentinel、Seata

#### 持久化：

- MySQL 5.7

#### 中间件：

- ElasticSearch
- Nginx负载均衡
- Redis缓存

#### 部署：

- 阿里云服务器

#### 版本控制：

- Git

#### 其他：

- 阿里巴巴 Java 开发手册


#### 安装教程

1.  创建my_pan数据库，将sql文件夹下my_pan.sql执行。
2.  配置application.yml 文件，修改mysql、redis、ElasticSearch连接信息。
3.  修改netdisc.store.root-path 为文件系统根目录。
4.  启动Nacos、Sentinel。
5.  依次启动netdisc-gateway、netdisc-oauth、netdisc-service-user、netdisc-service-filesystem。
6.  待Nacos服务列表已全部注册，访问127.0.0.1:9500访问Gateway暴露的API接口。
