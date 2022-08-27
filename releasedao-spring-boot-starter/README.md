### releasedao-spring-boot-starter

### 介绍:

releaseDao（ORM），含义：释放dao层，通过APi+注解的形式轻松操作数据库。

简化开发、提高效率，使开发人员更加专注于业务。

### 主要功能:

-  通过**APi+注解**的方式，**无需编写sql语句**，就能对数据库基本 CURD操作。
-  自带一整套**日志**体系，异常日志输出。
-  自带Redis缓存，**自动管理缓存**，减少数据库I/O。
-  非侵入式、开箱即用、轻量级。

### 技术:

- 反射。

- 注解。

- 数据结构。

- Redis。

- Spring Boot。

- ThreadLocal。

  。。。。

### 接口文档：

请查看 javadoc 目录。


## 安装教程

##### 使用方法：

​	开箱即用。

##### Maven依赖:

```xml
<!-- https://mvnrepository.com/artifact/io.github.calwenb/releasedao-spring-boot-starter -->
<dependency>
    <groupId>io.github.calwenb</groupId>
    <artifactId>releasedao-spring-boot-starter</artifactId>
    <version>1.x.x</version> 
</dependency>
```

#### Quick Start

1.Maven依赖引用。

2.使用案例如下：CURD只需要一行代码，是不是很简单。

```java
import com.wen.releasedao.core.mapper.BaseMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Demo
 * 1.注入 BaseMapper。
 * 2.调用API。
 *
 * @author calwen
 * @since 2022/8/19
 */
@Service
@Transactional
public class ClientServiceImpl implements ClientService {
    @Resource
    BaseMapper baseMapper;

    @Override
    public Client get(Integer id) {
        return baseMapper.getById(Client.class, id);
    }

    @Override
    public List<Client> list(ClientFindDto findDto) {
        return baseMapper.getList(Client.class);
    }

    @Override
    public void add(Client client) {
        baseMapper.add(client);
    }

    @Override
    public void update(Integer id, Client client) {
        baseMapper.save(client);
    }

    @Override
    public void del(Integer id) {
        baseMapper.deleteById(Client.class, id);
    }
}
```



3. 配置 application.yml（按需配置，不配也可）

```yaml
releasedao:
  config:
    camel-case: true	 #开启 驼峰自动转蛇形式 默认true
    logger: true	 	#开启 日志输出 ，默认false
    cache: true			#是否开启缓存，默认false
    cache-expired-time: 604800    #缓存过期时间 默认七天
```
