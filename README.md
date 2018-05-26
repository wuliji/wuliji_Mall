开发环境： Eclipse+Maven+Git+Linux+EasyUI

软件架构： Mysql+Mybatis+Spring+Springmvc+Redis+Solr

 ![image](https://github.com/wuliji/wuliji_Mall/blob/master/mall-parent/image/main.png)

一. 项目描述：宜立方商城是一个综合性的B2C平台，类似京东商城、天猫商城。会员可以在商城浏览商品、下订单，以及参加各种活动。宜立方商城采用分布式系统架构，子系统之间都是调用服务来实现系统之间的通信，使用http协议传递json数据方式实现。这样降低了系统之间的耦合度，提高了系统的扩展性。为了提高系统的性能使用redis做系统缓存，并使用redis实现session共享。为了保证redis的性能使用redis的集群。搜索功能使用solrCloud做搜索引擎，使用Niginx+FastDFS图片服务器实现静态资源的存储。

二. 系统功能描述如下

后台管理系统：管理商品、订单、类目、商品规格属性、用户管理以及内容发布等功能。

商城门户：用户可以在前台系统中进行注册、登录、浏览商品、首页、下单等操作。

会员系统：用户可以在该系统中查询已下的订单、收藏的商品、我的优惠券、团购等信息。

订单系统：提供下单、查询订单、修改订单状态、定时处理订单。

搜索系统：提供商品的搜索功能。

单点登录系统：为多个系统之间提供用户登录凭证以及查询登录用户的信息。


三. 相关框架：

1.dubbo:当服务越来越多，容量的评估，小服务资源的浪费等问题逐渐显现，此时需增加一个调度中心基于访问压力实时管理集群容量，提高集群利用率。此时，用于提高机器利用率的资源调度和治理中心。

 ![image](https://github.com/wuliji/wuliji_Mall/blob/master/mall-parent/image/dubbo.png)

节点角色说明：

•	Provider: 暴露服务的服务提供方。

•	Consumer: 调用远程服务的服务消费方。

•	Registry: 服务注册与发现的注册中心。

•	Monitor: 统计服务的调用次调和调用时间的监控中心。

•	Container: 服务运行容器。

2.FastDFS：分布式文件系统，用于搭建一个图片服务器，专门保存图片。FastDFS架构包括 Tracker server和Storage server。客户端请求Tracker server进行文件上传、下载，通过Tracker server调度最终由Storage server完成文件上传和下载。Tracker server作用是负载均衡和调度，通过Tracker server在文件上传时可以根据一些策略找到Storage server提供文件上传服务。可以将tracker称为追踪服务器或调度服务器。Storage server作用是文件存储，客户端上传的文件最终存储在Storage服务器上，Storage server没有实现自己的文件系统而是利用操作系统 的文件系统来管理文件。可以将storage称为存储服务器。

 ![image](https://github.com/wuliji/wuliji_Mall/blob/master/mall-parent/image/FastDFS.png)

3.Redis集群：用于添加缓存，减少查询数据库的压力。

 ![image](https://github.com/wuliji/wuliji_Mall/blob/master/mall-parent/image/redis.png)

(1)所有的redis节点彼此互联(PING-PONG机制),内部使用二进制协议优化传输速度和带宽.

(2)节点的fail是通过集群中超过半数的节点检测失效时才生效.

(3)客户端与redis节点直连,不需要中间proxy层.客户端不需要连接集群所有节点,连接集群中任何一个可用节点即可

(4)redis-cluster把所有的物理节点映射到[0-16383]slot上,cluster 负责维护node<->slot<->value

Redis 集群中内置了 16384 个哈希槽，当需要在 Redis 集群中放置一个 key-value 时，redis 先对 key 使用 crc16 算法算出一个结果，然后把结果对 16384 求余数，这样每个 key 都会对应一个编号在 0-16383 之间的哈希槽，redis 会根据节点数量大致均等的将哈希槽映射到不同的节点

4.SolrCloud：用于实现搜索功能，快速高效。

 ![image](https://github.com/wuliji/wuliji_Mall/blob/master/mall-parent/image/solr.png)

物理结构:三个Solr实例（ 每个实例包括两个Core），组成一个SolrCloud。

逻辑结构:索引集合包括两个Shard（shard1和shard2），shard1和shard2分别由三个Core组成，其中一个Leader两个Replication，Leader是由zookeeper选举产生，zookeeper控制每个shard上三个Core的索引数据一致，解决高可用问题。用户发起索引请求分别从shard1和shard2上获取，解决高并发问题。

Collection:Collection在SolrCloud集群中是一个逻辑意义上的完整的索引结构。它常常被划分为一个或多个Shard（分片），它们使用相同的配置信息。
比如：针对商品信息搜索可以创建一个collection。collection=shard1+shard2+....+shardX

Core:每个Core是Solr中一个独立运行单位，提供 索引和搜索服务。一个shard需要由一个Core或多个Core组成。由于collection由多个shard组成所以collection一般由多个core组成。

Master或Slave:Master是master-slave结构中的主结点（通常说主服务器），Slave是master-slave结构中的从结点（通常说从服务器或备服务器）。同一个Shard下master和slave存储的数据是一致的，这是为了达到高可用目的。

Shard:Collection的逻辑分片。每个Shard被化成一个或者多个replication，通过选举确定哪个是Leader。

5.Activemq：使用Activemq发送接收消息，通过消息队列实现商品同步。

 ![image](https://github.com/wuliji/wuliji_Mall/blob/master/mall-parent/image/activemq1.png)
 
 ![image](https://github.com/wuliji/wuliji_Mall/blob/master/mall-parent/image/activemq2.png)

6.Freemarker：FreeMarker是一个用Java语言编写的模板引擎，它基于模板来生成文本输出。FreeMarker与Web容器无关，即在Web运行时，它并不知道Servlet或HTTP。它不仅可以用作表现层的实现技术，而且还可以用于生成XML，JSP或Java 等。

 ![image](https://github.com/wuliji/wuliji_Mall/blob/master/mall-parent/image/freemarker.png)

7.MyCAT：一个彻底开源的，面向企业应用开发的“大数据库集群”支持事务、ACID、可以替代Mysql的加强版数据库，可以低成本的将现有的单机数据库和应用平滑迁移到“云”端，解决数据存储和业务规模迅速增长情况下的数据瓶颈问题。MyCAT通过定义表的分片规则来实现分片，每个表格可以捆绑一个分片规则，每个分片规则指定一个分片字段并绑定一个函数，来实现动态分片算法。

![image](https://github.com/wuliji/wuliji_Mall/blob/master/mall-parent/image/mycat.png)

Schema：逻辑库，与MySQL中的Database（数据库）对应，一个逻辑库中定义了所包括的Table。

Table：表，即物理数据库中存储的某一张表，与传统数据库不同，这里的表格需要声明其所存储的逻辑数据节点DataNode。在此可以指定表的分片规则。

DataNode：MyCAT的逻辑数据节点，是存放table的具体物理节点，也称之为分片节点，通过DataSource来关联到后端某个具体数据库上

DataSource：定义某个物理库的访问地址，用于捆绑到Datanode上

