# ScanPackage
ScanPackage是一个可以在本地文件下**对SpringBoot项目进行扫描**的项目。目前支持对指定的本地文件目录进行扫描。

### 特点

- 基于JDK8和Maven构建开发, 使用SpringBoot框架进行快速开发

## 快速开始

### 安装

克隆本项目后, 使用mvn编译, 打包生成jar文件, 找到jar包生成的目录, 运行起来即可

```bash
git clone 本项目

mvn compile

mvn package

java -jar ScanPackage-0.0.1.jar
```

项目就在9091端口运行起来了, 可以使用API工具来进行访问

```bash
POST http://localhost:9081/scan1/interface
{
  "path" : "F:\\TEST"
}
```

就能看到返回的结果了, 会显示该path目录下的所有Java文件的接口。
