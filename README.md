# 数据容器2.0

## 1.项目介绍

以UdxSchema为导向，结合数据映射方、数据重构方法、以及在线生成工具，生成UdxData，并将其发布为数据服务。
根据服该服务，用户可动态的、按需的获取UdxData中的数据。
同时容器提供了可视化服务，来对UdxData进行可视化。

## 2.架构

前后端分离开发，分工明确，职责清晰。
前端关注界面展示，提供UI交互功能，利用AJAX调用后台接口。后端专注于业务逻辑，处理AJAX请求，返回JSON数据。

前端：Vue框架构建Spa。

后端：SpringBoot+Mongodb 提供相关支持。

![相关流程](https://raw.githubusercontent.com/sunlingzhiliber/imgstore/master/W9IGGB3HXEQRKJSIUR%5DK%7ELG.png)

## 3.存储解释

通过`web.upload-path`将存储路径和相关代码隔离开来，其中涉及的到文件夹包括

### geoserver_files

用于geoserver使用，发布矢量和栅格服务

```
- geotiffes
  - XXXX.tif
  - XXXX.tif
- shapefiles
  - XXX.shp
  - XXX.shp
  - XXX.shp
```

### data_process

文件夹为处理数据所用，以UUID建立一个文件夹，在文件夹中进行一些处理,目前设计到的处理包括

1.解压缩和压缩文件
2.在线调用service

```
- UUID1
- UUID2
- UUID3
...
```

### services

包括两种服务，及映射服务和重构服务

```
- map
  - uuid
    - XXX.zip
	- invoke
- refactor
  - uuid
    - XXX.zip
	- invoke
```

### store_data_Resource_files

其中数据存储的实际文件，以UUID为名，其文件名和文件后缀存储在数据库中

```
- UUID1
- UUID2
- UUID3
...
```

### 第三方工具

1.gdal 
- 下载并编译gdal
- application.properties 文件夹下配置 path.getGdal 路径，如"${web.lib-path}/gdal/bin/gdal/apps"
- 如果服务器报错："无法启动程序，计算机中丢失gdal111.dll……" 将 gdal/bin 目录下的 gdal111.dll 拷贝至exe所在目录（即gdal/bin/gdal/apps目录）

2.zip应用程序
- 使用 minizip 解压
- application.properties 文件夹下配置 path.getMiniZip 路径，如"${web.lib-path}/zip/"

### 注意
1. geoserver 额外配置（解决shapefile无法自动发布的问题）：
- 打开localhost:8080/geoserver (用户名/密码 admin/geoserver)
- 修改 “数据存储” shapefileList 条目中连接参数 shapefiles 文件的目录为\geoserver_files\shapefiles本地存储目录

# 编码问题

为了兼容geotools读取Meta，必须设置项目编码为UTF-8 设置虚拟机启动也为`-Dfile.encoding=UTF-8`


