# spring cloud 微服务项目最佳实践框架
## 一、技术要点
#### 1、注册中心：单机版
#### 2、前后端分离：
* ssm-user-web和ssm-user-provide（前后端通过http数据交互）
#### 3、服务之间通过注册中心数据交互
* ssm-order-consumer和ssm-user-provide（服务通过注册中心数据交互）
#### 4、服务之间调用通过熔断做降级处理
* ssm-order-consumer内部有熔断降级处理
#### 5、前端技术：angularJS
#### 6、后端技术：spring，mybatis


## 二、项目说明
* base-eureka：注册中心
* ssm-order：订单模块
* ssm-order-api：订单api模块
* ssm-order-consumer：订单消费者模块
* ssm-order-provide：订单服务模块
* ssm-user：用户模块
* ssm-user-api：用户api模块
* ssm-user-provide：用户服务模块（通过ssm-order-consumer访问订单访问数据）
* ssm-user-web：用户前端页面

## 三、项目启动及访问
#### 1、base-eureka：访问注册中心，http://localhost:2000/
#### 2、ssm-order-provide：通过服务访问订单数据（crud操作）
* http://localhost:2111/order/list
* http://localhost:2111/order/getone/1
* http://localhost:2111/order/delete/1

#### 3、ssm-order-consumer：通过消费者访问订单数据（crud操作）
* http://localhost:2222/client/list
* http://localhost:2222/client/getone/1
* http://localhost:2222/client/delete/1

#### 4、ssm-user-provide：通过用户服务访问订单数据（crud操作）
* http://localhost:2333/user/list
* http://localhost:2333/user/getone/1
* http://localhost:2333/user/delete/1

#### 5、ssm-user-web：访问前端页面
* http://localhost:2444/user（用户模块数据，订单模块数据）
* http://localhost:2444/home（静态数据）

## 四、数据库说明
```
spring.datasource.url = jdbc:mysql://localhost:3306/demo_cloud
spring.datasource.username = root
spring.datasource.password = 123123
```

#### 数据库表：
```
ssm_user:user_id, user_name, user_desc, order_id
ssm_order:order_id, order_title, order_mark
```