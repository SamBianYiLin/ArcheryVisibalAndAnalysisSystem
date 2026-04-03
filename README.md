# Archery Visible And Analysis System

一个基于 **Spring Boot + Thymeleaf + MySQL + HTML/CSS/JavaScript** 的射箭运动员成绩记录、可视化展示与数据分析系统。

该项目主要面向射箭训练场景，支持运动员信息管理、射箭成绩录入、历史成绩分析、数据可视化展示等功能，可用于训练记录管理与基础辅助分析。

---

## 项目简介

本系统用于管理射箭运动员的基础信息及训练数据，并结合前端可视化页面，对运动员的历史成绩、命中情况和肌肉发力情况进行分析展示。

系统分为以下几个核心部分：

- 运动员管理
- 射箭成绩记录
- 历史数据分析
- 肌肉发力可视化展示
- 管理员 / 运动员权限区分（可继续扩展）

---

## 功能特性

### 1. 运动员管理
- 新增运动员
- 查看运动员列表
- 编辑运动员信息
- 删除运动员信息

### 2. 射箭记录管理
- 为指定运动员添加训练成绩
- 记录单次射箭分数、环数等信息
- 支持按运动员查询历史训练记录
- (注：当前为随机生成数据，数据格式为json格式，后期可接入传感器信息和LLMs，其中传感器信息可转换为json格式，使用Langchain4j可调用LLM用以分析训练数据)

### 3. 数据分析
- 统计总训练次数
- 计算平均成绩
- 统计最高成绩
- 计算十环命中率
- 历史成绩趋势分析

### 4. 可视化展示
- 训练成绩图表展示
- 历史记录动态渲染
- 肌肉发力热区示意图展示
- 前端页面美化与交互优化

### 5. 登录与权限控制（规划 / 可扩展）
- 管理员登录
- 运动员登录
- 管理员查看全部数据
- 运动员仅查看个人数据
- 运动员注册与密码修改

---

## 技术栈

### 后端
- Java
- Spring Boot
- Spring MVC
- Spring Data JPA / Repository
- Thymeleaf

### 前端
- HTML5
- CSS3
- JavaScript
- Chart.js（如项目中已使用，可保留）
- 自定义页面样式与交互逻辑

### 数据库
- MySQL

### 开发工具
- IntelliJ IDEA
- Maven
- Git / GitHub

---

## 项目结构

```bash
archery-system/
├── src/
│   ├── main/
│   │   ├── java/com/archery/
│   │   │   ├── controller/      # 控制层
│   │   │   ├── service/         # 业务逻辑层
│   │   │   ├── repository/      # 数据访问层
│   │   │   ├── entity/          # 实体类
│   │   │   └── ArcheryApplication.java
│   │   └── resources/
│   │       ├── templates/       # Thymeleaf 页面
│   │       ├── static/
│   │       │   ├── css/         # 样式文件
│   │       │   ├── js/          # 脚本文件
│   │       │   └── images/      # 图片资源
│   │       └── application.yml / application.properties
├── pom.xml
└── README.md
```

---

## 使用及环境变量配置

1. 使用maven的package打为jar包，传输到服务器指定目录
2. 在服务器jar包目录下，创建环境变量文件
```bash
touch /your/jar/root/.env
```
3. 创建好环境变量文件后，使用vim或其他文本编辑器打开.env文件
```bash
vim /your/jar/root/.env
```
4. 分别配置如下三项内容(需要将url、用户名与密码改为对应数据库的真实信息)：
```bash
DB_URL:"jdbc:mysql://your.databse.url:port/databasename?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&useSSL=false"
DB_USERNAME:"username"
DB_PASSWORD:"password"  
```
5. 配置好环境变量后，即可启动项目
```bash
set -a
source /your/jar/root/.env
set +a
java -jar nameoffile.jar
```
6. 观察有无报错信息，若无报错，即可正常运行