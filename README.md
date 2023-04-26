# ScanPackage

[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
![](https://img.shields.io/badge/version-0.0.2-brightgreen.svg)
![](https://img.shields.io/badge/Java-8-green.svg)

ScanPackage是一个可以在本地文件下**对SpringBoot项目进行扫描**的项目。目前支持对指定的**本地文件目录**、**GitHub文件**
和和**Gitee文件**进行扫描。

### 特点

- 基于JDK8和Maven构建, 使用SpringBoot框架进行快速开发
- 可以在线扫描GitHub、Gitee项目的接口, 只需传入项目的路径

## 快速开始

### 安装

克隆本项目后, 使用mvn编译, 打包生成jar文件, 找到jar包生成的目录, 运行起来即可

```bash
git clone https://github.com/HUANYUESS/ScanPackage.git

mvn compile

mvn package

java -jar ScanPackage-0.0.1.jar
```

项目就在9091端口运行起来了, 可以使用API工具来进行访问

```http request
POST http://localhost:9081//scan/project

{
    "path" : "F:\\TEST",
    "type": "LOCAL"
}
```

就能看到返回的结果了, 会显示该path目录下的所有Java文件的接口


## 使用说明

### API

#### 接口地址

```http request
POST /scan/project
```

#### 参数

| 参数        | 类型     | 是否必须 | 描述               |
|-----------|--------|------|------------------|
| path      | string | 是    | 文件路径             |
| type      | string | 是    | 文件类型(LOCAL, GIT) |
| fileName  | string | 否    | 在上述路径下的文件名字      |
| recursive | string | 否    | 是否迭代子目录          |

> 说明:
> 当fileName不提供时, 会查询给定path路径下的所有文件; 当提供fileName时, 则只会在path路径下的基础上, 查找指定的fileName文件。
> recursive默认值是false, 默认不会迭代查询, 当使用GIT进行迭代查询时, 将很可能出现403 API REQUEST请求受限问题,
> 尽量下载到本地之后在本地查询

#### 案例1
```http request
POST /scan/project

{
    "path" : "F:\\TEST",
    "type": "LOCAL"
}
```

**成功返回的响应如下**

```json
{
	"code": 200,
	"msg": "OK",
	"data": [
		"PUT /system/dept/update",
		"GET /system/dept/list",
		"GET /system/dept/list-all-simple",
		"GET /system/dept/get",
		"POST /system/dept/create",
		"DELETE /system/dept/delete",
		"POST /system/dept/example0",
		"PUT /system/post/update",
		"GET /system/post/get",
		"GET /system/post/list-all-simple",
		"GET /system/post/page",
		"GET /system/post/export",
		"POST /system/post/create",
		"DELETE /system/post/delete"
	],
	"timestamp": "2023-04-21T11:35:40.24"
}
```

#### 案例2
```http request
POST /scan/project

{
    "path" : "F:\\TEST",
    "fileName": "DeptController.java",
    "type": "LOCAL"
}
```

**成功返回的响应如下**

```json
{
  "code": 200,
  "msg": "OK",
  "data": [
    "PUT /system/dept/update",
    "GET /system/dept/list",
    "GET /system/dept/list-all-simple",
    "GET /system/dept/get",
    "POST /system/dept/create",
    "DELETE /system/dept/delete",
    "POST /system/dept/example0"
  ],
  "timestamp": "2023-04-21T11:36:02.064"
}
```

#### 案例3
```http request
POST /scan/project

{
    "path" : "https://github.com/qiwenshare/qiwen-file/tree/master/src/main/java/com/qiwenshare/file/controller",
    "type": "GIT"
}
```

**成功返回的响应如下**

```json
{
  "code": 200,
  "msg": "OK",
  "data": [
    "GET /common/commonfileuser",
    "POST /common/commonfile",
    "GET /common/getCommonFileByUser",
    "GET /common/commonFileList",
    "GET /file/search",
    "POST /file/createFile",
    "POST /file/createFold",
    "POST /file/renamefile",
    "GET /file/getfilelist",
    "POST /file/batchdeletefile",
    "POST /file/deletefile",
    "POST /file/unzipfile",
    "POST /file/copyfile",
    "POST /file/movefile",
    "POST /file/batchmovefile",
    "GET /file/getfiletree",
    "POST /file/update",
    "GET /file/detail",
    "GET /filetransfer/preview",
    "GET /filetransfer/uploadfile",
    "POST /filetransfer/uploadfile",
    "GET /filetransfer/downloadfile",
    "GET /filetransfer/batchDownloadFile",
    "GET /filetransfer/getstorage",
    "GET /notice/list",
    "GET /notice/detail",
    "POST /recoveryfile/deleterecoveryfile",
    "POST /recoveryfile/batchdelete",
    "GET /recoveryfile/list",
    "POST /recoveryfile/restorefile",
    "GET /share/shareList",
    "GET /share/sharefileList",
    "GET /share/sharetype",
    "GET /share/checkextractioncode",
    "GET /share/checkendtime",
    "POST /share/sharefile",
    "POST /share/savesharefile",
    "GET /param/grouplist",
    "POST /user/register",
    "GET /user/login",
    "GET /user/checkuserlogininfo",
    "GET /user/checkWxAuth"
  ],
  "timestamp": "2023-04-21T14:16:38.447"
}
```

#### 案例4
```http request
POST /scan/project

{
    "path" : "https://github.com/qiwenshare/qiwen-file/tree/master/src/main/java/com/qiwenshare/file/controller",
    "type": "GIT",
    "fileName": "CommonFileController.java",
}
```

**成功返回的响应如下**

```json
{
  "code": 200,
  "msg": "OK",
  "data": [
    "GET /common/commonfileuser",
    "POST /common/commonfile",
    "GET /common/getCommonFileByUser",
    "GET /common/commonFileList"
  ],
  "timestamp": "2023-04-21T14:17:21.638"
}
```

#### 案例5

```http request
POST /scan/project

{
    "path" : "https://gitee.com/huanyuess/qiwen-file/tree/master/src/main/java/com/qiwenshare/file/controller",
    "type": "GIT"
}
```

**成功返回的响应如下**

```json
{
  "code": 200,
  "msg": "OK",
  "data": [
    "GET /common/commonfileuser",
    "POST /common/commonfile",
    "GET /common/getCommonFileByUser",
    "GET /common/commonFileList",
    "GET /file/search",
    "POST /file/createfile",
    "POST /file/renamefile",
    "GET /file/getfilelist",
    "POST /file/batchdeletefile",
    "POST /file/deletefile",
    "POST /file/unzipfile",
    "POST /file/copyfile",
    "POST /file/movefile",
    "POST /file/batchmovefile",
    "GET /file/selectfilebyfiletype",
    "GET /file/getfiletree",
    "POST /file/update",
    "GET /file/detail",
    "GET /filetransfer/preview",
    "GET /filetransfer/uploadfile",
    "POST /filetransfer/uploadfile",
    "GET /filetransfer/downloadfile",
    "GET /filetransfer/batchDownloadFile",
    "GET /filetransfer/getstorage",
    "GET /notice/list",
    "GET /notice/detail",
    "POST /recoveryfile/deleterecoveryfile",
    "POST /recoveryfile/batchdelete",
    "POST /recoveryfile/list",
    "POST /recoveryfile/restorefile",
    "GET /share/shareList",
    "GET /share/sharefileList",
    "GET /share/sharetype",
    "GET /share/checkextractioncode",
    "GET /share/checkendtime",
    "POST /share/sharefile",
    "POST /share/savesharefile",
    "GET /param/grouplist",
    "POST /user/register",
    "GET /user/login",
    "GET /user/checkuserlogininfo"
  ],
  "timestamp": "2023-04-21T15:38:56.045"
}
```

## 参与贡献

1. Fork 本仓库
2. 新建 Feat_xxx 分支
3. 提交代码
4. 新建 Pull Request

