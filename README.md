# mybatis-generator-plus

## 1. 插件列表

### 1.1. Java Model扩展插件

* SerializablePlugin：Model类是否实现序列化标记接口Serializable；
* FieldCommentPlugin： Model类是否根据数据库表字段描述信息生成字段的注释；
* EqualsHashCodePlugin：Model类是否生成hashcode()和equal()方法；
* ToJsonPlugin：Model类是否生成JavaBean转Fastjson的toJson()方法；
* FromJsonPlugin：Model类是否生成Fastjson转JavaBean的fromJson()方法；
* InnerClassKeyPlugin：Model类是否生成XxxKey字段名称内部类。


### 1.2. Java Client/SQL Mapper扩展插件

* UseGeneratedKeysColumnPlugin：SQL XML是否使用主键自增的方式；
* FindByIdsPlugin：SQL XML是否生成根据主键列表批量查询的SQL Statement；
* FindAllPlugin：SQL XML是否生成查询所有列表的SQL Statement；
* DeleteByIdsPlugin：SQL XML是否生成根据主键列表批量从删除的SQL Statement；
* SaveBatchPlugin：SQL XML是否生成高效的批量插入记录的SQL Statement；
* UpdateBatchPlugin：SQL XML是否生成高效的批量更新记录的SQL Statement。
* RenameStatementPlugin：SQL XML是否对Java Client已有的方法进行禁用和重命名。


## 2. 使用配置

### 2.1. 项目构建

克隆mybatis-generator-plus插件到本地目录，进入mybatis-generator-plus目录，将插件以jar包的形式安装到maven本地仓库中。

```xml
mvn clean install
```

### 2.2. 引入依赖 


创建一个新的Maven Project，在pom.xml中引入上面的插件依赖：

```xml
<dependencies>
    <groupId>org.ostenant.mybatis.generator.plus</groupId>
    <artifactId>mybatis-generator-plus</artifactId>
    <version>1.0-SNAPSHOT</version>
<dependencies>
```

同时配置entity.target.dir和dao.resources.dir属性：

```xml
<properties>
	<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
	<entity.target.dir>src/main/java/</entity.target.dir>
	<dao.resources.dir>src/main/resources/</dao.resources.dir>
</properties>
```

### 2.3. 导入数据库表

这里以MySQL为例，新建一个数据库deposit，创建customer表：

```sql
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `birthday` varchar(255) DEFAULT NULL COMMENT '生日日期',
  `sex` int(11) DEFAULT NULL COMMENT '(0: 男，1：女)',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### 2.4. generator配置

#### 2.4.1. 创建jdbc.properties

在src/main/resources目录下创建jdbc.properties文件，配置如下：

```properties
jdbc.driverClass=com.mysql.jdbc.Driver
jdbc.connectionURL=jdbc:mysql://localhost:3306/deposit
jdbc.user=root
jdbc.password=root
```

#### 2.4.2. 配置mybatis-generator.xml

在src/main/resources目录下创建mybatis-generator.xml文件，配置如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">

<generatorConfiguration>
    <!-- XML节点配置顺序按需严格按照如下配置，不然使用maven执行“mybatis-generator:generate -e”时会报错
         property*(plugin*,commentGenerator?,(connectionFactory|jdbcConnection),
         javaTypeResolver?,javaModelGenerator,sqlMapGenerator?,javaClientGenerator?,table+) -->
    <!-- 导入JDBC属性配置 -->
    <properties resource="jdbc.properties"></properties>

    <context id="MYSQL" targetRuntime="MyBatis3" defaultModelType="flat">
        <!-- 生成的Java文件的编码-->
        <property name="javaFileEncoding" value="UTF-8"/>

        <!-- 格式化java代码-->
        <property name="javaFormatter" value="org.mybatis.generator.api.dom.DefaultJavaFormatter"/>

        <!-- 格式化XML代码-->
        <property name="xmlFormatter" value="org.mybatis.generator.api.dom.DefaultXmlFormatter"/>

        <!-- generator原生的插件 -->
        <!-- 配置生成toString()方法的插件 -->
        <plugin type="org.mybatis.generator.plugins.ToStringPlugin"/>
        <!-- 配置大小写不敏感的插件 -->
        <plugin type="org.mybatis.generator.plugins.CaseInsensitiveLikePlugin"/>
        <!-- 配置生成自定义Builder模式的withXXXProperty()方法的插件 -->
        <plugin type="org.mybatis.generator.plugins.FluentBuilderMethodsPlugin"/>
        <!-- 配置对生成example进行重命名的插件 -->
	    <!--  <plugin type="org.mybatis.generator.plugins.RenameExampleClassPlugin">
				  <property name="searchString" value="Example$"/>
				  <property name="replaceString" value="Criteria"/>
			  </plugin>  -->
			  
        <!-- 自定义扩展的插件 -->
        <!-- 配置生成自定义hashCode()方法的插件 -->
        <plugin type="org.ostenant.mybatis.generator.plus.plugins.EqualsHashCodePlugin"/>
        <!-- 配置生成自定义的类序列化接口的插件 -->
        <plugin type="org.ostenant.mybatis.generator.plus.plugins.SerializablePlugin"/>
        <!-- 配置生成EntityKey的内部类的插件-->
        <plugin type="org.ostenant.mybatis.generator.plus.plugins.InnerClassKeyPlugin"/>
        <!-- 配置生成的fromJson()方法的插件 -->
        <plugin type="org.ostenant.mybatis.generator.plus.plugins.FromJsonPlugin"/>
        <!-- 配置生成的toJson()方法的插件 -->
        <plugin type="org.ostenant.mybatis.generator.plus.plugins.ToJsonPlugin"/>
        <!-- 配置生成所有字段的注释的插件 -->
        <plugin type="org.ostenant.mybatis.generator.plus.plugins.FieldCommentPlugin"/>
        <!-- 配置插入时自动生成主键的插件 -->
        <plugin type="org.ostenant.mybatis.generator.plus.plugins.UseGeneratedKeysColumnPlugin"/>
        <!-- 配置生成findAll()方法和sql查询statement的插件 -->
        <plugin type="org.ostenant.mybatis.generator.plus.plugins.FindAllPlugin"/>
        <!-- 配置生成findByIds()方法和sql查询statement的插件 -->
        <plugin type="org.ostenant.mybatis.generator.plus.plugins.FindByIdsPlugin"/>
        <!-- 配置生成deleteByIds()方法和sql查询statement的插件 -->
        <plugin type="org.ostenant.mybatis.generator.plus.plugins.DeleteByIdsPlugin"/>
        <!-- 配置生成saveBatch()方法和sql查询statement的插件 -->
        <plugin type="org.ostenant.mybatis.generator.plus.plugins.SaveBatchPlugin"/>
        <!-- 配置生成updateBatch()方法和sql查询statement的插件 -->
        <plugin type="org.ostenant.mybatis.generator.plus.plugins.UpdateBatchPlugin"/>
        <!-- 配置修改默认的方法名称和statement id的插件 -->
        <plugin type="org.ostenant.mybatis.generator.plus.plugins.RenameStatementPlugin"/>

        <!-- （非必需）用于创建class时，对注释进行控制 -->
        <commentGenerator>
            <!-- 去掉生成日期那行注释 -->
            <property name="suppressDate" value="true"/>
            <!-- 去掉生成代码中的所有注释 -->
            <property name="suppressAllComments" value="true"/>
        </commentGenerator>

        <!-- jdbc的数据库连接 -->
        <jdbcConnection driverClass="${jdbc.driverClass}"
                        connectionURL="${jdbc.connectionURL}"
                        userId="${jdbc.user}" password="${jdbc.password}">
        </jdbcConnection>

        <!-- （非必需）类型处理器，在数据库类型和java类型之间的转换控制-->
        <javaTypeResolver>
            <property name="forceBigDecimals" value="false"/>
        </javaTypeResolver>


        <!-- 1. Model模型生成器,用来生成含有主键key的类，记录类以及查询Example类
            targetPackage：指定生成的model生成所在的包名
            targetProject：指定在该项目下所在的路径
        -->
        <javaModelGenerator targetPackage="org.ostenant.mybatis.generator.plus.examples.entities"
                            targetProject="${entity.target.dir}">
            <!-- 是否对model添加构造函数 -->
            <property name="constructorBased" value="false"/>
            <!-- 是否允许子包，即targetPackage.schemaName.tableName -->
            <property name="enableSubPackages" value="true"/>
            <!-- 建立的Model对象是否不可改变  即生成的Model对象不会有setter方法，只有构造方法 -->
            <property name="immutable" value="false"/>
            <!-- 给Model添加一个接口 -->
            <property name="rootInterface" value="java.io.Serializable"/>
            <!-- 是否对类CHAR类型的列的数据进行trim操作 -->
            <property name="trimStrings" value="true"/>
        </javaModelGenerator>


        <!-- 2. Mapper XML映射文件生成所在的目录，为每一个数据库的表生成对应的SqlMap.xml文件 -->
        <sqlMapGenerator targetPackage="mapper"
                         targetProject="${dao.resources.dir}">
            <property name="enableSubPackages" value="true"/>
        </sqlMapGenerator>


        <!-- 3. 对于mybatis来说，即生成Mapper接口，注意，如果没有配置该元素，那么默认不会生成Mapper接口
            targetPackage/targetProject:同javaModelGenerator
            type：选择怎么生成mapper接口（在MyBatis3/MyBatis3Simple下）：
                1，ANNOTATEDMAPPER：会生成使用Mapper接口+Annotation的方式创建（SQL生成在annotation中），不会生成对应的XML；
                2，MIXEDMAPPER：使用混合配置，会生成Mapper接口，并适当添加合适的Annotation，但是XML会生成在XML中；
                3，XMLMAPPER：会生成Mapper接口，接口完全依赖XML；
            注意，如果context是MyBatis3Simple：只支持ANNOTATEDMAPPER和XMLMAPPER
        -->
        <javaClientGenerator type="XMLMAPPER"
                             targetPackage="org.ostenant.mybatis.generator.plus.examples.mapper"
                             implementationPackage="org.ostenant.mybatis.generator.plus.examples.mapper"
                             targetProject="${entity.target.dir}">
            <property name="enableSubPackages" value="true"/>
        </javaClientGenerator>


        <!-- 所有的数据库表与实体对象
            1，schema：数据库的schema；
            2，catalog：数据库的catalog；
            3，alias：为数据表设置的别名，如果设置了alias，那么生成的所有的SELECT SQL语句中，列名会变成：alias_actualColumnName
            4，domainObjectName：生成的domain类的名字，如果不设置，直接使用表名作为domain类的名字；可以设置为somepck.domainName，那么会自动把domainName类再放到somepck包里面；
            5，enableInsert（默认true）：指定是否生成insert语句；
            6，enableSelectByPrimaryKey（默认true）：指定是否生成按照主键查询对象的语句（就是getById或get）；
            7，enableSelectByExample（默认true）：MyBatis3Simple为false，指定是否生成动态查询语句；
            8，enableUpdateByPrimaryKey（默认true）：指定是否生成按照主键修改对象的语句（即update)；
            9，enableDeleteByPrimaryKey（默认true）：指定是否生成按照主键删除对象的语句（即delete）；
            10，enableDeleteByExample（默认true）：MyBatis3Simple为false，指定是否生成动态删除语句；
            11，enableCountByExample（默认true）：MyBatis3Simple为false，指定是否生成动态查询总条数语句（用于分页的总条数查询）；
            12，enableUpdateByExample（默认true）：MyBatis3Simple为false，指定是否生成动态修改语句（只修改对象中不为空的属性）；
            13，modelType：参考context元素的defaultModelType，相当于覆盖；
            14，delimitIdentifiers：参考tableName的解释，注意，默认的delimitIdentifiers是双引号，如果类似MYSQL这样的数据库，使用的是`（反引号，那么还需要设置context的beginningDelimiter和endingDelimiter属性）
            15，delimitAllColumns：设置是否所有生成的SQL中的列名都使用标识符引起来。默认为false，delimitIdentifiers参考context的属性
        -->
        <table tableName="customer" domainObjectName="Customer"
               enableCountByExample="false"
               enableDeleteByExample="false"
               enableSelectByExample="false"
               enableUpdateByExample="false">
            <property name="useGeneratedKeys" value="false"></property>
        </table>
    </context>

</generatorConfiguration>

```

这里以Intellij IDEA为例，指定工作目录为当前项目空间，配置逆向工程的生成命令如下：

```bash
mybatis-generator:generator -e
```

![](http://ols3fdyll.bkt.clouddn.com/Mybatis-generator-configuration.png)

### 2.5. 逆向工程

运行generator命令，项目空间将会生成目标源文件。

Customer.java

```java
public class Customer implements Serializable {
    /**
     * 主键
     */
    private String id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 生日日期
     */
    private String birthday;

    /**
     * (0: 男，1：女)
     */
    private Integer sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 地址
     */
    private String address;

    public String getId() {
        return id;
    }

    public Customer withId(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public Customer withName(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getBirthday() {
        return birthday;
    }

    public Customer withBirthday(String birthday) {
        this.setBirthday(birthday);
        return this;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday == null ? null : birthday.trim();
    }

    public Integer getSex() {
        return sex;
    }

    public Customer withSex(Integer sex) {
        this.setSex(sex);
        return this;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public Customer withAge(Integer age) {
        this.setAge(age);
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public Customer withAddress(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", birthday=").append(birthday);
        sb.append(", sex=").append(sex);
        sb.append(", age=").append(age);
        sb.append(", address=").append(address);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Customer other = (Customer) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
                && (this.getBirthday() == null ? other.getBirthday() == null : this.getBirthday().equals(other.getBirthday()))
                && (this.getSex() == null ? other.getSex() == null : this.getSex().equals(other.getSex()))
                && (this.getAge() == null ? other.getAge() == null : this.getAge().equals(other.getAge()))
                && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, birthday, sex, age, address);
    }

    public static Customer fromJson(JSONObject fromJsonObj) {
        if (fromJsonObj == null || fromJsonObj.isEmpty()) {
            return null;
        }
        Customer customer = new Customer();
        customer.setId(JSONUtils.getString(fromJsonObj, CustomerKey.ID));
        customer.setName(JSONUtils.getString(fromJsonObj, CustomerKey.NAME));
        customer.setBirthday(JSONUtils.getString(fromJsonObj, CustomerKey.BIRTHDAY));
        customer.setSex(JSONUtils.getInteger(fromJsonObj, CustomerKey.SEX));
        customer.setAge(JSONUtils.getInteger(fromJsonObj, CustomerKey.AGE));
        customer.setAddress(JSONUtils.getString(fromJsonObj, CustomerKey.ADDRESS));
        return customer;
    }

    public JSONObject toJson() {
        JSONObject toJsonObj = new JSONObject();
        toJsonObj.put(CustomerKey.ID, id);
        toJsonObj.put(CustomerKey.NAME, name);
        toJsonObj.put(CustomerKey.BIRTHDAY, birthday);
        toJsonObj.put(CustomerKey.SEX, sex);
        toJsonObj.put(CustomerKey.AGE, age);
        toJsonObj.put(CustomerKey.ADDRESS, address);
        return toJsonObj;
    }

    public static final class CustomerKey {
        /**
         * 主键
         */
        public static final String ID = "id";
        /**
         * 姓名
         */
        public static final String NAME = "name";
        /**
         * 生日日期
         */
        public static final String BIRTHDAY = "birthday";
        /**
         * (0: 男，1：女)
         */
        public static final String SEX = "sex";
        /**
         * 年龄
         */
        public static final String AGE = "age";

        /**
         * 地址
         */
        public static final String ADDRESS = "address";
    }
}
```

CustomerMapper.java

```java
public interface CustomerMapper {
    int deleteById(String id);

    int save(Customer record);

    Customer findById(String id);

    int update(Customer record);

    List<Customer> findAll();

    List<Customer> findByIds(List<String> list);

    String deleteByIds(List<String> list);

    String saveBatch(List<Customer> list);

    String updateBatch(List<Customer> list);
}
```

CustomerMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.ostenant.mybatis.generator.plus.examples.mapper.CustomerMapper">
    <resultMap id="BaseResultMap" type="org.ostenant.mybatis.generator.plus.examples.entities.Customer">
        <id column="id" jdbcType="VARCHAR" property="id"/>
        <result column="name" jdbcType="VARCHAR" property="name"/>
        <result column="birthday" jdbcType="VARCHAR" property="birthday"/>
        <result column="sex" jdbcType="INTEGER" property="sex"/>
        <result column="age" jdbcType="INTEGER" property="age"/>
        <result column="address" jdbcType="VARCHAR" property="address"/>
    </resultMap>
    
    
    <sql id="Base_Column_List">
        id, name, birthday, sex, age, address
    </sql>
    
    <select id="findById" parameterType="java.lang.String" resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List"/>
        from customer
        where id = #{id,jdbcType=VARCHAR}
    </select>
    
    <delete id="deleteById" parameterType="java.lang.String">
        DELETE FROM customer
        WHERE id = #{id,jdbcType=VARCHAR}
    </delete>
    
    <insert id="save" parameterType="org.ostenant.mybatis.generator.plus.examples.entities.Customer">
        INSERT INTO customer (id, name, birthday,
                              sex, age, address)
        VALUES (#{id,jdbcType=VARCHAR}, #{name,jdbcType=VARCHAR}, #{birthday,jdbcType=VARCHAR},
                #{sex,jdbcType=INTEGER}, #{age,jdbcType=INTEGER}, #{address,jdbcType=VARCHAR})
    </insert>
    
    <update id="update" parameterType="org.ostenant.mybatis.generator.plus.examples.entities.Customer">
        update customer
        <set>
            <if test="name != null">
                name = #{name,jdbcType=VARCHAR},
            </if>
            <if test="birthday != null">
                birthday = #{birthday,jdbcType=VARCHAR},
            </if>
            <if test="sex != null">
                sex = #{sex,jdbcType=INTEGER},
            </if>
            <if test="age != null">
                age = #{age,jdbcType=INTEGER},
            </if>
            <if test="address != null">
                address = #{address,jdbcType=VARCHAR},
            </if>
        </set>
        where id = #{id,jdbcType=VARCHAR}
    </update>
    
    <select id="findAll" resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List"/>
        from customer
    </select>
    
    <select id="findByIds" parameterType="java.util.List" resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List"/>
        from customer
        where id in
        <foreach close=")" collection="list" item="item" open="(" separator=",">
            #{item,jdbcType=VARCHAR}
        </foreach>
    </select>
    
    <delete id="deleteByIds" parameterType="java.util.List">
        delete from
        customer
        where
        id in
        <foreach close=")" collection="list" item="item" open="(" separator=",">
            #{item,jdbcType=VARCHAR}
        </foreach>
    </delete>
    
    <insert id="saveBatch" parameterType="java.util.List">
        insert into
        customer
        (id, name, birthday, sex, age, address)
        values
        <foreach collection="list" item="item" separator=",">
            (#{item.id,jdbcType=VARCHAR}, #{item.name,jdbcType=VARCHAR}, #{item.birthday,jdbcType=VARCHAR},
            #{item.sex,jdbcType=INTEGER}, #{item.age,jdbcType=INTEGER}, #{item.address,jdbcType=VARCHAR})
        </foreach>
    </insert>
    
    <update id="updateBatch" parameterType="java.util.List">
        update customer
        <trim prefix="set" suffixOverrides=",">
            <trim prefix="name = case" suffix="end,">
                <foreach collection="list" index="index" item="item">
                    <if test="item.name != null">
                        when #{item.id,jdbcType=VARCHAR} then #{item.name,jdbcType=VARCHAR}
                    </if>
                </foreach>
            </trim>
            <trim prefix="birthday = case" suffix="end,">
                <foreach collection="list" index="index" item="item">
                    <if test="item.birthday != null">
                        when #{item.id,jdbcType=VARCHAR} then #{item.birthday,jdbcType=VARCHAR}
                    </if>
                </foreach>
            </trim>
            <trim prefix="sex = case" suffix="end,">
                <foreach collection="list" index="index" item="item">
                    <if test="item.sex != null">
                        when #{item.id,jdbcType=VARCHAR} then #{item.sex,jdbcType=INTEGER}
                    </if>
                </foreach>
            </trim>
            <trim prefix="age = case" suffix="end,">
                <foreach collection="list" index="index" item="item">
                    <if test="item.age != null">
                        when #{item.id,jdbcType=VARCHAR} then #{item.age,jdbcType=INTEGER}
                    </if>
                </foreach>
            </trim>
            <trim prefix="address = case" suffix="end,">
                <foreach collection="list" index="index" item="item">
                    <if test="item.address != null">
                        when #{item.id,jdbcType=VARCHAR} then #{item.address,jdbcType=VARCHAR}
                    </if>
                </foreach>
            </trim>
        </trim>
        where id in
        <foreach close=")" collection="list" item="item" open="(" separator=", ">
            #{item.id,jdbcType=VARCHAR}
        </foreach>
    </update>
</mapper>
```