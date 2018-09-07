# 微服务项目最佳实践-项目搭建﻿
#### 项目结构
```
best-cloud-ssm
--base-eureka
--ssm-order
-----ssm-order-api
-----ssm-order-provide
-----ssm-order-consumer
--ssm-user
-----ssm-user-api
-----ssm-user-provide
-----ssm-user-web
```

* 项目模块说明，项目数据访问，项目技术要点可参照 11-2 微服务项目最佳实践-项目说明
## 一、项目搭建
* 基础项目主要是为了对项目中依赖库版本管理
### 1、pom文件
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
​
    <groupId>cloud.best</groupId>
    <artifactId>best-cloud-ssm</artifactId>
    <packaging>pom</packaging>
    <version>1.0-SNAPSHOT</version>
​
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.5.3.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
​
    <modules>
        <module>base-eureka</module>
        <module>ssm-order</module>
        <module>ssm-user</module>
    </modules>
​
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
        <spring-cloud.version>Dalston.RELEASE</spring-cloud.version>
    </properties>
​
​
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
​
</project>
```
##### 说明：
* spring-boot-starter-parent：指定spring-boot的基础版本

### 二、base-eureka
* base-eureka是一个单机版的注册中心，主要用于服务管理，服务之间通过注册中心进行数据交互
* 一般项目开发不需要一直创建注册中心，在写demo为了方便不同demo使用不同注册中心方便测试
#### 2.1、添加pom依赖
```
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka-server</artifactId>
        </dependency>
​
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter</artifactId>
        </dependency>
    </dependencies>

```

#### 2.1、创建启动类
```
package com.ssm;
​
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
​
/**
 * @author brusion
 * @date 2018/9/4
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {
​
    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class, args);
    }
}
```
#### 2.3、创建配置文件
```
spring.application.name=best-cloud-eureka
​
server.port=2000
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
​
eureka.client.serviceUrl.defaultZone=http://localhost:${server.port}/eureka/

```
## 三、ssm-order订单模块
### 3.1、ssm-order-api模块
* ssm-order-api模块主要存放java实体对象和访问接口方法
#### 3.1.1、pom依赖

* order模块没有依赖其他服务所以api不需要依赖其他模块
```
    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-web</artifactId>
        </dependency>
    </dependencies>
 
```
##### 说明：
* spring-web：为了在接口类中注解使用
#### 3.1.2、java实体对象
* 省略了get，set方法
```
package com.ssm.order.bean;
​
public class Order {
    private Integer orderId;
​
    private String orderTitle;
​
    private String orderMark;
}
```
#### 3.1.3、服务接口类
```
package com.ssm.order.service;
​
import com.ssm.order.bean.Order;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
​
import java.util.List;
​
/**
 * 注意：如果服务中有消费者模块，服务提供者的路径直接写在service中（这样可以保证消费者和提供者的路径一致）
 *
 * @author brusion
 * @date 2018/9/4
 */
public interface OrderService {
​
    @RequestMapping("/order/delete/{orderId}")
    int delete(@PathVariable("orderId") Integer orderId);
​
    @RequestMapping(value = "/order/insert", method = RequestMethod.POST)
    int insert(@RequestBody Order order);
​
    @RequestMapping("/order/list")
    List<Order> getList();
​
    @RequestMapping("/order/getone/{orderId}")
    Order getById(@PathVariable("orderId") Integer orderId);
​
    @RequestMapping(value = "/order/update", method = RequestMethod.POST)
    int update(@RequestBody Order order);
​
}
```
##### 说明：
* order模块有消费模块，为了方便消费者client接口和服务者Controller访问地址一致，直接将访问地址写在service接口类中

#### 3.2、ssm-order-provide模块
* ssm-order-provide模块是order模块的数据来源，通过数据库读取获取数据，通过接口给外部调用
##### 模块代码层级
```
com.ssm.OrderApplication
com.ssm.order.controller.OrderController
com.ssm.order.mapper.OrderMapper
​
resource/application.properties
resource/generatorConfig.xml
resource/mybatis/OrderMapper.xml
```
#### 3.2.1、pom文件
* java实体封装采用了mybatis-generator-maven-plugin插件处理，通过插件生成了java对象，mapper接口，mapper.xml文件
```
<dependencies>
        <dependency>
            <groupId>cloud.best</groupId>
            <artifactId>ssm-order-api</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
​
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka-server</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter</artifactId>
        </dependency>
​
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
​
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>1.1.1</version>
        </dependency>
​
    </dependencies>
​
    <build>
        <plugins>
            <!-- mybatis逆向工程 -->
            <plugin>
                <groupId>org.mybatis.generator</groupId>
                <artifactId>mybatis-generator-maven-plugin</artifactId>
                <version>1.3.2</version>
                <configuration>
                    <!--配置文件的位置-->
                    <configurationFile>src/main/resources/generatorConfig.xml</configurationFile>
                    <verbose>true</verbose>
                    <overwrite>true</overwrite>
                </configuration>
            </plugin>
        </plugins>
​
        <resources>
            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.xml</include>
                    <include>**/*.properties</include>
                </includes>
            </resource>
            <resource>
                <directory>src/main/resources</directory>
                <filtering>true</filtering>
            </resource>
        </resources>
​
    </build>

```
##### 说明：
* mybatis-generator-maven-plugin：mybatis插件依赖，也可以手写mapper，java实体，mapper.xml文件

#### 3.2.2、插件生成配置文件：generatorConfig.xml
* 关于插件的使用可以参照博客 [08 mybatis的generator-plugin插件使用](https://blog.csdn.net/qq_34231253/article/details/80389676)
```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
​
    <!-- 需要配置成自己的库地址 -->
    <classPathEntry location="repository/mysql/mysql-connector-java/5.1.38/mysql-connector-java-5.1.38.jar" />
​
    <context id="context1" targetRuntime="MyBatis3">
​
        <commentGenerator>
            <!-- 去除自动生成的注释 -->
            <property name="suppressAllComments" value="true" />
        </commentGenerator>
​
        <!-- 数据库连接配置 -->
        <jdbcConnection driverClass="com.mysql.jdbc.Driver"
                        connectionURL="jdbc:mysql://localhost:3306/demo_cloud"
                        userId="root"
                        password="123123"/>
​
        <!-- 默认false，把JDBC DECIMAL 和 NUMERIC 类型解析为 Integer，为 true时把JDBC DECIMAL 和
            NUMERIC 类型解析为java.math.BigDecimal -->
        <javaTypeResolver>
            <property name="forceBigDecimals" value="false" />
        </javaTypeResolver>
​
        <!-- targetProject:生成PO类的位置 -->
        <javaModelGenerator targetPackage="com.ssm.order.bean"
                            targetProject="src/main/java">
            <!-- enableSubPackages:是否让schema作为包的后缀 -->
            <property name="enableSubPackages" value="false" />
            <!-- 从数据库返回的值被清理前后的空格 -->
            <property name="trimStrings" value="true" />
        </javaModelGenerator>
​
        <!-- targetProject:mapper映射文件生成的位置 -->
        <sqlMapGenerator  targetPackage="com.ssm.order.mapper"
            targetProject="src/main/java">
            <!-- enableSubPackages:是否让schema作为包的后缀 -->
            <property name="enableSubPackages" value="false" />
        </sqlMapGenerator>
​
        <!-- targetPackage：mapper接口生成的位置 -->
        <javaClientGenerator type="XMLMAPPER"
                             targetPackage="com.ssm.order.mapper"
                             targetProject="src/main/java">
            <!-- enableSubPackages:是否让schema作为包的后缀 -->
            <property name="enableSubPackages" value="false" />
        </javaClientGenerator>
​
​
        <table tableName="ssm_order" schema=""/>
​
    </context>
</generatorConfiguration>

```
##### 注意：
* classPathEntry：需要替换成开发者自己的地址
* tableName="ssm_order"：

#### 3.2.3、mapper接口
* mapper接口主要是定义了数据库操作的接口类

##### 注意：此mapper文件在自动生成的文件进行了修改
```
package com.ssm.order.mapper;
​
import com.ssm.order.bean.Order;
import org.apache.ibatis.annotations.Mapper;
​
import java.util.List;
​
@Mapper
public interface OrderMapper {
​
    int delete(Integer orderId);
​
    int insert(Order record);
​
    List<Order> getList();
​
    Order getById(Integer orderId);
​
    int update(Order record);
}
```
##### 说明：
* @Mapper注解：声明此接口为mapper接口如果没有配置此接口必须在启动类配置当前类的包扫描路径，否则在Controller会注入mapper失败。

#### 3.2.3、mapper配置文件

* mapper配置文件主要作用是mapper接口的具体实现
* 本mapper配置文件在自动配置文件进行了修改
* mapper文件存放路径：resource/mybatis/OrderMapper.xml
```
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.ssm.order.mapper.OrderMapper">
​
    <resultMap id="BaseResultMap" type="com.ssm.order.bean.Order">
        <id column="order_id" property="orderId" jdbcType="INTEGER"/>
        <result column="order_title" property="orderTitle" jdbcType="VARCHAR"/>
        <result column="order_mark" property="orderMark" jdbcType="VARCHAR"/>
    </resultMap>
​
    <sql id="Base_Column_List">
    order_id, order_title, order_mark
  </sql>
​
    <select id="getList" resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List"/>
        from ssm_order
    </select>
​
    <select id="getById" resultMap="BaseResultMap" parameterType="java.lang.Integer">
        select
        <include refid="Base_Column_List"/>
        from ssm_order
        where order_id = #{orderId,jdbcType=INTEGER}
    </select>
​
    <delete id="delete" parameterType="java.lang.Integer">
    delete from ssm_order
    where order_id = #{orderId,jdbcType=INTEGER}
  </delete>
​
    <insert id="insert" parameterType="com.ssm.order.bean.Order">
    insert into ssm_order (order_id, order_title, order_mark )
    values (#{orderId,jdbcType=INTEGER}, #{orderTitle,jdbcType=VARCHAR}, #{orderMark,jdbcType=VARCHAR} )
  </insert>
​
    <update id="update" parameterType="com.ssm.order.bean.Order">
    update ssm_order
    set order_title = #{orderTitle,jdbcType=VARCHAR},
      order_mark = #{orderMark,jdbcType=VARCHAR}
    where order_id = #{orderId,jdbcType=INTEGER}
  </update>
</mapper>
```

#### 3.2.4、访问接口类
* 接口类主要定义了外部访问房接口，定义了url，参数，请求方式
```
package com.ssm.order.controller;
​
import com.ssm.order.bean.Order;
import com.ssm.order.mapper.OrderMapper;
import com.ssm.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
​
import java.util.List;
​
/**
 * @author brusion
 * @date 2018/9/4
 */
@CrossOrigin
@RestController
public class OrderController implements OrderService {
​
    @Autowired
    private OrderMapper orderMapper;
​
​
    @Override
    @RequestMapping("/order/delete/{orderId}")
    public int delete(@PathVariable("orderId") Integer orderId) {
        return orderMapper.delete(orderId);
    }
​
    @Override
    @RequestMapping(value = "/order/insert", method = RequestMethod.POST)
    public int insert(@RequestBody Order order) {
        return orderMapper.insert(order);
    }
​
    @Override
    @RequestMapping("/order/list")
    public List<Order> getList() {
        return orderMapper.getList();
    }
​
    @Override
    @RequestMapping("/order/getone/{orderId}")
    public Order getById(@PathVariable("orderId") Integer orderId) {
        return orderMapper.getById(orderId);
    }
​
    @Override
    @RequestMapping(value = "/order/update", method = RequestMethod.POST)
    public int update(@RequestBody Order order) {
        return orderMapper.update(order);
    }
    
}

```
##### 说明：
* @CrossOrigin注解：是为了本模块接口允许跨域访问

#### 3.2.5、启动类
* 启动类一般在其他类的路径下面，方便spring进行包的扫描
```
package com.ssm;
​
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
​
/**
 * @author brusion
 * @date 2018/9/4
 */
@SpringBootApplication
@EnableEurekaServer
public class OrderApplication {
​
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
```

#### 3.2.6、项目配置文件

* 定义了mapper文件地址，数据库访问参数，当前模块对应的端口及注册到注册中心
```
mybatis.mapper-locations=classpath:mybatis/*.xml
mybatis.type-aliases-package=com.ssm.order.bean
​
​
spring.datasource.driverClassName = com.mysql.jdbc.Driver
spring.datasource.url = jdbc:mysql://localhost:3306/demo_cloud?useUnicode=true&characterEncoding=utf-8&ssl=false
spring.datasource.username = root
spring.datasource.password = 123123
​
​
spring.application.name=order-provide
server.port=2111
eureka.client.serviceUrl.defaultZone=http://localhost:2000/eureka/
​
```

## 四、ssm-user模块
* ssm-user模块从ssm-order-consumer获取数据，并从数据库获取user数据，将user列表，order中id为的数据展示到前端界面
### 4.1、ssm-user-api模块
#### 4.1.1、pom文件
* 因为api模块中的接口关联了订单数据，需要导入ssm-order-api模块
```
  <dependencies>
        <dependency>
            <groupId>cloud.best</groupId>
            <artifactId>ssm-order-api</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
```
##### 说明：
* ssm-order-api中有关联spring-web模块，当前模块不需要再次关联spring-web模块也有对应库

#### 4.1.2、java实体对象
* 省略get，set方法
```
package com.ssm.user.bean;
​
public class User {
    private Integer userId;
​
    private String userName;
​
    private String userDesc;
​
    private Integer orderId;
}
```
#### 4.1.3、定义访问接口
* user模块没有消费者模块，地址不需要全部写在接口类中

```
package com.ssm.user.service;
​
import com.ssm.order.bean.Order;
import com.ssm.user.bean.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
​
import java.util.List;
​
/**
 * @author brusion
 * @date 2018/9/4
 */
public interface UserService {
​
    @RequestMapping("/delete/{userId}")
    public int delete(@PathVariable("userId") Integer userId);
​
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public int insert(@RequestBody User user);
​
    @RequestMapping("/list")
    public List<User> getList();
​
    @RequestMapping("/getone/{userId}")
    public User getById(@PathVariable("userId") Integer userId);
​
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public int update(@RequestBody User user);
​
    @RequestMapping("/order/{OrderId}")
    public Order getOrder(@PathVariable("OrderId") Integer orderId);
}

```

#### 4.2、ssm-user-provide模块
* ssm-user-provide模块与ssm-order-provide功能一致，代码结构也一致

##### 代码结构
```
com.ssm.UserApplication
com.ssm.user.mapper.UserMapper
com.ssm.user.controller.UserController
​
resource/application.properties
resource/generatorConfig.xml
resource/mybatis/UserMapper.xml
```

#### 4.2.1、pom依赖
* user模块需要通过ssm-order-consumer访问order模块数据
```
    <dependencies>
        <dependency>
            <groupId>cloud.best</groupId>
            <artifactId>ssm-user-api</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
        <dependency>
            <groupId>cloud.best</groupId>
            <artifactId>ssm-order-consumer</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
​
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka-server</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-feign</artifactId>
        </dependency>
​
​
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
​
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>1.1.1</version>
        </dependency>
        <dependency>
            <groupId>com.netflix.hystrix</groupId>
            <artifactId>hystrix-javanica</artifactId>
            <version>1.5.10</version>
        </dependency>
    </dependencies>
​
    <build>
        <plugins>
            <!-- mybatis逆向工程 -->
            <plugin>
                <groupId>org.mybatis.generator</groupId>
                <artifactId>mybatis-generator-maven-plugin</artifactId>
                <version>1.3.2</version>
                <configuration>
                    <!--配置文件的位置-->
                    <configurationFile>src/main/resources/generatorConfig.xml</configurationFile>
                    <verbose>true</verbose>
                    <overwrite>true</overwrite>
                </configuration>
            </plugin>
        </plugins>
​
        <resources>
            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.xml</include>
                    <include>**/*.properties</include>
                </includes>
            </resource>
            <resource>
                <directory>src/main/resources</directory>
                <filtering>true</filtering>
            </resource>
        </resources>
​
    </build>
```

#### 4.2.2、mapper接口类
* 此maper接口在自动生成的基础上做了修改

```
package com.ssm.user.mapper;
​
import com.ssm.user.bean.User;
import org.apache.ibatis.annotations.Mapper;
​
import java.util.List;
​
@Mapper
public interface UserMapper {
​
    int delete(Integer userId);
​
    int insert(User user);
​
    List<User> getList();
​
    User getById(Integer userId);
​
    int update(User user);
}
```

#### 4.2.3、mapper配置文件
```
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.ssm.user.mapper.UserMapper">
​
    <resultMap id="BaseResultMap" type="com.ssm.user.bean.User">
        <id column="user_id" property="userId" jdbcType="INTEGER"/>
        <result column="user_name" property="userName" jdbcType="VARCHAR"/>
        <result column="user_desc" property="userDesc" jdbcType="VARCHAR"/>
        <result column="order_id" property="orderId" jdbcType="INTEGER"/>
    </resultMap>
​
    <sql id="Base_Column_List">
    user_id, user_name, user_desc, order_id
  </sql>
​
    <select id="getList" resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List"/>
        from ssm_user
    </select>
​
    <select id="getById" resultMap="BaseResultMap" parameterType="java.lang.Integer">
        select
        <include refid="Base_Column_List"/>
        from ssm_user
        where user_id = #{userId,jdbcType=INTEGER}
    </select>
​
    <delete id="delete" parameterType="java.lang.Integer">
    delete from ssm_user
    where user_id = #{userId,jdbcType=INTEGER}
  </delete>
​
​
    <insert id="insert" parameterType="com.ssm.user.bean.User">
    insert into ssm_user (user_id, user_name, user_desc, 
      order_id)
    values (#{userId,jdbcType=INTEGER}, #{userName,jdbcType=VARCHAR}, #{userDesc,jdbcType=VARCHAR}, 
      #{orderId,jdbcType=INTEGER})
  </insert>
​
    <update id="update" parameterType="com.ssm.user.bean.User">
    update ssm_user
    set user_name = #{userName,jdbcType=VARCHAR},
      user_desc = #{userDesc,jdbcType=VARCHAR},
      order_id = #{orderId,jdbcType=INTEGER}
    where user_id = #{userId,jdbcType=INTEGER}
  </update>
​
</mapper>
```

#### 4.2.4、访问接口类
* 通过注入OrderClient对象远程调用方式获取到order模块的数据
```
package com.ssm.user.controller;
​
import com.ssm.order.bean.Order;
import com.ssm.order.client.OrderClient;
import com.ssm.user.bean.User;
import com.ssm.user.mapper.UserMapper;
import com.ssm.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
​
import java.util.List;
​
/**
 * @author brusion
 * @date 2018/9/4
 */
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController implements UserService {
​
    @Autowired
    private OrderClient orderClient;
    @Autowired
    private UserMapper userMapper;
​
    @Override
    @RequestMapping("/delete/{userId}")
    public int delete(@PathVariable("userId") Integer userId) {
        return userMapper.delete(userId);
    }
​
    @Override
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public int insert(@RequestBody User user) {
        return userMapper.insert(user);
    }
​
    @Override
    @RequestMapping("/list")
    public List<User> getList() {
        return userMapper.getList();
    }
​
    @Override
    @RequestMapping("/getone/{userId}")
    public User getById(@PathVariable("userId") Integer userId) {
        return userMapper.getById(userId);
    }
​
    @Override
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public int update(@RequestBody User user) {
        return userMapper.update(user);
    }
​
    @Override
    @RequestMapping("/order/{orderId}")
    public Order getOrder(@PathVariable("orderId") Integer orderId) {
        return orderClient.getById(orderId);
    }
}
​
```

#### 4.2.5、启动类
```
package com.ssm;
​
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
​
/**
 * @author brusion
 * @date 2018/9/4
 */
​
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
@RefreshScope
@ComponentScan(basePackages = {"com.ssm.order.client","com.ssm.user.controller"})
public class UserApplication {
​
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
​
```

##### 说明:
* ComponentScan:通过扫描的方式导入消费者和消费者使用者类

#### 4.2.6、配置文件

* 基本与ssm-order-provide一致
```
mybatis.mapper-locations=classpath:mybatis/*.xml
mybatis.type-aliases-package=com.ssm.user.bean
​
​
spring.datasource.driverClassName = com.mysql.jdbc.Driver
spring.datasource.url = jdbc:mysql://localhost:3306/demo_cloud?useUnicode=true&characterEncoding=utf-8&ssl=false
spring.datasource.username = root
spring.datasource.password = 123123
​
​
spring.application.name=user-provide
feign.hystrix.enabled=true
server.port=2333
eureka.client.serviceUrl.defaultZone=http://localhost:2000/eureka/

```

#### 4.3、ssm-user-web模块
* 采用angularJs 来获取接口数据
* 采用http方式获取接口数据（实现前后端分离）

##### 代码结构
```
com.ssm.WebApplication
com.ssm.controller.PageController
​
resource/static/test.png
resource/remplates/domain/home.html
resource/remplates/user/main.html
resource/application.properties
resource/static：主要用于存放静态资源：js，css，图片
resource/remplates：用于存放页面
```
#### 4.3.1、pom依赖
```
    <dependencies>
​
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
​
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
​
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka-server</artifactId>
        </dependency>
​
    </dependencies>

```

#### 4.3.2、页面跳转控制类
```
package com.ssm.controller;
​
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
​
/**
 * @author brusion
 * @date 2018/9/4
 */
@Controller
public class PageController {
​
    @RequestMapping(value = "/user")
    public String index() {
        return "/user/main";
    }
​
    @RequestMapping(value = "/home")
    public String home() {
        return "/domain/home";
    }
​
}

```
#### 4.3.3、启动类
```
package com.ssm;
​
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
​
/**
 * @author brusion
 * @date 2018/9/4
 */
@SpringBootApplication
@EnableEurekaServer
public class WebApplication {
​
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
​
}
​
```

#### 4.3.4、配置文件
```
#视图层控制
spring.mvc.view.prefix=classpath:/templates/
spring.mvc.view.suffix=.html
spring.mvc.static-path-pattern=/static/**
​
# THYMELEAF
spring.thymeleaf.encoding=UTF-8
# 热部署静态文件
spring.thymeleaf.cache=false
# 使用HTML5标准
spring.thymeleaf.mode=HTML5
​
​
spring.application.name=user-web
server.port=2444
eureka.client.serviceUrl.defaultZone=http://localhost:2000/eureka/

```

##### 注意:
* web模块目前与springmvc基本一致，主要区别在resource中

#### 4.3.5、main.html
* 通过http请求到user列表数据，order中id为1 的数据展示到页面
```
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>数据获取</title>
    <script src="http://cdn.static.runoob.com/libs/angular.js/1.4.6/angular.min.js"></script>
    <script>
        var app = angular.module('myApp', []);
        //注入http对象
        app.controller('myController', function ($scope, $http) {
            $scope.findAll = function () {
                $http.get('http://localhost:2333/user/list').success(
                    function (response) {
                        $scope.list = response;
                        $scope.getUserOrder();
                    }
                );
            }
​
            $scope.getUserOrder = function () {
                $http.get('http://localhost:2333/user/order/1').success(
                    function (response) {
                        $scope.data = response;
                    }
                );
            }
        });
​
    </script>
</head>
<body ng-app="myApp" ng-controller="myController" ng-init="findAll()">
​
​
<h3>用户列表数据</h3>
<table>
    <tr>
        <td>用户id</td>
        <td>用户名称</td>
        <td>订单描述</td>
        <td>订单号</td>
    </tr>
    <tr ng-repeat="item in list">
        <td>{{item.userId}}</td>
        <td>{{item.userName}}</td>
        <td>{{item.userDesc}}</td>
        <td>{{item.orderId}}</td>
    </tr>
</table>
​
<h3>用户订单数据</h3>
<table>
    <tr>
        <td>订单id</td>
        <td>用户名称</td>
        <td>订单描述</td>
    </tr>
    <tr>
        <td>{{data.orderId}}</td>
        <td>{{data.orderTitle}}</td>
        <td>{{data.orderMark}}</td>
    </tr>
</table>
​
</body>
</html>

```

#### 4.3.6、home.html
* 访问静态资源图片，并展示到页面
```
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>home-page</title>
​
    <script src="http://cdn.static.runoob.com/libs/angular.js/1.4.6/angular.min.js"></script>
    <script>
        var app = angular.module('myApp', []);
        //注入http对象
​
    </script>
</head>
​
<body>
​
​
<img src="../../static/test.png"/>
​
</body>
</html>
```
## 五、项目地址
[githubt]()
