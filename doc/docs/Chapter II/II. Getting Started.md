> __如果你刚接触`Yggdrasil`，你可以通过阅读这个章节来了解它。这个章节介绍了基础的构建和使用。包括你如何在项目中引入、如何通过`Maven`、`Gradle`等构建工具来使用它。__

---

# 2.1. Introducing Yggdrasil

_`Yggdrasil`是一个包含众多功能的通用工具包。_

__它包含:__

1. JAVA原生类型、数据结构、多线程、反射等相关操作的工具
2. 常用第三方类库的二次封装
3. 通用基础数据结构的定义

__目标:__

1. 封装低侵入的工具使用和配置方式
2. 提供高性能的IO处理工具
3. 提供更加易用的中间件二次封装

---

# 2.2. System Requirements

| Build Tool | Version |
| :--- | :--- |
| Maven | 3.3+ |
| Gradle | 4.4+ |

| Language | Version |
| :--- | :--- |
| JAVA | 1.8 |
| Kotlin | 1.2+ |

| Lib | Version |
| :--- | :--- |
| Spring | 5.0+ |
| Spring Boot | 2.0+ |
| Mybatis | 3.3+ |

> Tips: 不是所有用到的Lib都列在这里，具体依赖的版本请以`yggdrasil-parent`项目中的`pom.xml`文件中指定的版本为准，使用前如版本不一致，请仔细查看相关Lib的兼容性说明。

---

# 2.3. Installing Yggdrasil

> _`Yggdrasil`可以在原生的JAVA环境中使用，但是如果你需要用到其中的高级功能或者中间件相关的功能，你还是需要引入相关的依赖。_

## 2.3.1 Installation Instructions for the Java Developer

__1. 使用前，请使用一下命令先确认`JDK`版本__
```bash
$ java -version
```
__2. 如需在IDE环境下查看和编译源码，请安装`lombok`插件__

 - [For Eclipse base IDE](https://projectlombok.org/setup/eclipse)
 - [For IntelliJ IDEA](https://projectlombok.org/setup/intellij)
 - [For Netbeans](https://projectlombok.org/setup/netbeans)
 - [For VS Code](https://projectlombok.org/setup/vscode)

### 2.3.1.1 Maven Installation

> _`Yggdrasil`源码可以在Maven 3.3或者以上版本中正常编译。如果你还未安装Maven，你可以从[Maven官方](https://maven.apache.org/)获取指导和帮助。_

__1. 安装Maven__

- 大多数操作系统，你可以在包管理器或者软件商店中获取Maven
    - __Ubuntu__: `sudo apt install maven`
    - __CentOS__: `sudo yum install maven`
    - __MacOS__: `brew install maven`
    - __Windows__: 根据官方的安装指引下载压缩包，并设置相应的环境变量

__2. pom.xml配置__

```xml
<!-- 项目中的parent pom文件 -->
<project xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.test</groupId>
    <artifactId>test-parent</artifactId>
    <version>1.0.0-RELEASE</version>
    <packaging>pom</packaging>

    <properties>
        <!-- yggdrasil版本 -->
        <yggdrasil.version>3.5.0-ddd-SNAPSHOT</yggdrasil.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <!-- Yggdrasil -->
            <dependency>
                <groupId>org.s3s3l.yggdrasil</groupId>    <!-- Yggdrasil parent pom -->
                <artifactId>yggdrasil-parent</artifactId>
                <version>${yggdrasil.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
</project>
```

```xml
<!-- 项目中具体使用Yggdrasil的模块的pom文件 -->
<project xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.test</groupId>
    <artifactId>test-module</artifactId>
    <version>1.0.0-RELEASE</version>
    <packaging>jar</packaging>

    <parent>
        <groupId>com.test</groupId>
        <artifactId>test-parent</artifactId>    <!-- 引入parent pom -->
        <version>1.0.0-RELEASE</version>
    </parent>

    <dependencies>
        <!-- yggdrasil -->
        <dependency>
            <groupId>org.s3s3l.yggdrasil</groupId>
            <artifactId>yggdrasil-utils</artifactId>
        </dependency>
    </dependencies>
</project>
```