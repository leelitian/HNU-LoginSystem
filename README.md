### 项目名称

HNU登陆注册模块



### 开发工具

Android Studio		 【安卓前端】

Eclipse for JAVAEE	【网页前端、后台】

Tomcat 7.0



### 成品

**HNU登录系统 App**

包含登录、注册、短信验证码找回密码等功能

**登陆系统 网页版**

功能与App相仿



### 特色技术

后端：Druid连接池、HttpSession

安卓前端：SharedPrefrence记录本地信息、阿里云短信服务

服务器：腾讯云Ubuntu 18.04



### 实现简介

- 主要分为3大模块：安卓前端、网页前端、后端，其中两个前端共用一个后端。
- 安卓端增加键值`{ Client : "Android" }`来区分前端。
- 安卓前端与后台的通信采用AsyncHttpClient进行异步访问URL。

