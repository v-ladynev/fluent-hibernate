# fluent-hibernate
A library to work with Hibernate by fluent API. This library hasn't dependencies, except Hibernate libraries. It requires Java 1.6 and above. It can be used with Hibernate 5 and Hibernate 4.

[![Build Status](https://travis-ci.org/v-ladynev/fluent-hibernate.svg?branch=master)](https://travis-ci.org/v-ladynev/fluent-hibernate)

## Download
##### Direct link
[![Release 0.2.0](https://img.shields.io/badge/release-0.2.0-blue.svg)](https://github.com/v-ladynev/fluent-hibernate/releases/download/0.2.0/fluent-hibernate-core-0.2.0.jar)

##### Maven (`pom.xml`)
```XML
<dependency>
  <groupId>com.github.v-ladynev</groupId>
  <artifactId>fluent-hibernate-core</artifactId>
  <version>0.2.0</version>
</dependency>
```
All versions in the Maven repository: [fluent-hibernate-core](http://repo1.maven.org/maven2/com/github/v-ladynev/fluent-hibernate-core/)

##### Gradle (`build.gradle`)
```Gradle
compile 'com.github.v-ladynev:fluent-hibernate-core:0.2.0'
```

## Hibernate 4 Notes

If you want to use the library with Hibernate 4, you can consider to exclude a transitive dependency to Hibernate 5.
For an example using Gradle:
```Gradle
    compile('com.github.v-ladynev:fluent-hibernate-core:0.2.0') {
        exclude group: 'org.hibernate'
    }
```
## The Most Useful Features

_fluent-hibrnate_ has the features wich can be used with plain Hibernate or Spring code
without a library infrastructure.

### Scan the class path for Hibernate entities

The library can be used for a quick entites scanning. You will need just the library jar,
without additional dependencies. Just download the library using [Download](#download) section and use [EntityScanner](https://github.com/v-ladynev/fluent-hibernate/blob/master/fluent-hibernate-core/src/main/java/com/github/fluent/hibernate/cfg/scanner/EntityScanner.java):

_For Hibernate 4 and Hibernate 5_
```Java
Configuration configuration = new Configuration();
EntityScanner.scanPackages("my.com.entities", "my.com.other.entities")
    .addTo(configuration);
SessionFactory sessionFactory = configuration.buildSessionFactory();
```
_Using a new Hibernate 5 bootstrapping API_
```Java
List<Class<?>> classes = EntityScanner
        .scanPackages("my.com.entities", "my.com.other.entities").result();

MetadataSources metadataSources = new MetadataSources();
for (Class<?> annotatedClass : classes) {
    metadataSources.addAnnotatedClass(annotatedClass);
}

SessionFactory sessionFactory = metadataSources.buildMetadata()
    .buildSessionFactory();
```

### A Hibernate 5 Implicit Naming Strategy

It generates table and column names with underscores, like [ImprovedNamingStrategy](https://docs.jboss.org/hibernate/orm/4.3/javadocs/index.html?org/hibernate/cfg/ImprovedNamingStrategy.html) from Hibernate 4 and Hibernate 3, and constraint names 
(unique, foreign key) as well. Apart those, it has a lot of configurable interesting features
like: plural table names, the table and column prefixes, the embedded column prefixes via the custom `@FluentName` annotation, automatic name restriction (removing the vowels) and others.

Just download the library using [Download](#download) section and use [Hibernate5NamingStrategy](https://github.com/v-ladynev/fluent-hibernate/blob/master/fluent-hibernate-core/src/main/java/com/github/fluent/hibernate/cfg/strategy/hibernate5/Hibernate5NamingStrategy.java):

```Java
Configuration configuration = new Configuration();
configuration.setImplicitNamingStrategy(new Hibernate5NamingStrategy());
SessionFactory sessionFactory = configuration.configure().buildSessionFactory();
```
_Using strategy options for a tables prefix and a names restriction except the join table names_

```Java
Configuration configuration = new Configuration();
configuration.setImplicitNamingStrategy(
        new Hibernate5NamingStrategy(StrategyOptions.builder().tablePrefix("acps")
                .restrictLength(50).restrictJoinTableNames(false).build()));
SessionFactory sessionFactory = configuration.configure().buildSessionFactory();
```

_Using Spring_

```XML
<bean id="sessionFactory" class="org.springframework.orm.hibernate5.LocalSessionFactoryBean">
  <property name="implicitNamingStrategy">
    <bean class="com.github.fluent.hibernate.cfg.strategy.hibernate5.Hibernate5NamingStrategy">
      <property name="options">
        <bean class="com.github.fluent.hibernate.cfg.strategy.StrategyOptions">
          <property name="tablePrefix" value="acps" />
          <property name="maxLength" value="50" />
          <property name="restrictJoinTableNames" value="false" />
        </bean>
      </property>
    </bean>
  </property>
</bean>
```
### A Nested Transformer

It is a custom transformer like `Transformers.aliasToBean(SomeDto.class)` but with nested projections support.

Just download the library using [Download](#download) section and use [FluentHibernateResultTransformer](https://github.com/v-ladynev/fluent-hibernate/blob/master/fluent-hibernate-core/src/main/java/com/github/fluent/hibernate/transformer/FluentHibernateResultTransformer.java).

_Load `User` only with `login` and `department.name` fields_
```Java
@Entity
public class User {

    @Column
    private String login;

    @ManyToOne
    private Department department;

}

@Entity
public class Department {

    @Column
    private String name;

}

Criteria criteria = session.createCriteria(User.class, "u");
criteria.createAlias("u.department", "d", JoinType.LEFT_OUTER_JOIN);
criteria.setProjection(Projections.projectionList()
        .add(Projections.property("u.login").as("login"))
        .add(Projections.property("d.name").as("department.name")));

List<User> users = criteria
        .setResultTransformer(new FluentHibernateResultTransformer(User.class))
        .list();
```
#### Using with the native SQL

It is impossible with Hibernate 5 to use `Transformers.aliasToBean(SomeDto.class)` the same way as
it is used with Hibernate 3 — without the aliases with the quotes ([a more deep explanation](http://stackoverflow.com/a/37423885/3405171)).



## Examples
Get all users
```Java
List<User> users = H.<User> request(User.class).list();
```
Getting a user with a login `my_login`
```Java
final String loginToFind = "my_login";
User user = H.<User> request(User.class).eq("login", loginToFind).first();
```
#### A partial objects loading
Get all users, but only with `login` and `id` properties are filled (other properties will be null).
```Java
List<User> users = H.<User> request(User.class).proj("login").proj("id")
    .transform(User.class).list();
```
## Example Projects
- [A console project with a lot of query examples](https://github.com/v-ladynev/fluent-hibernate/tree/master/fluent-hibernate-examples/simply-console/)
- [A console project using Spring to configure fluent-hibernate](https://github.com/v-ladynev/fluent-hibernate/tree/master/fluent-hibernate-examples/spring-console/)
- [A simply console Eclipse project](https://github.com/v-ladynev/fluent-hibernate-mysql). This project uses Hibernate 5 and MySQL. It has a very simply Eclipse structure (without Gradle stuff).

## Contributors

- [Vladimir Ladynev](https://plus.google.com/102177768964957793539/posts)
- [DoubleF1re](https://github.com/DoubleF1re)
- [samsonych](https://github.com/samsonych)
- [Lucas Levvy](https://github.com/Levvy055)
- [Aleksey Pchelnikov](https://github.com/aleksey-pchelnikov)

## Dependency Status
[![Dependency Status](https://www.versioneye.com/user/projects/560424a1f5f2eb0019000933/badge.svg?style=flat)](https://www.versioneye.com/user/projects/560424a1f5f2eb0019000933)

## Progress
[![Throughput Graph](https://graphs.waffle.io/v-ladynev/fluent-hibernate/throughput.svg)](https://waffle.io/v-ladynev/fluent-hibernate/metrics)

### TODO:
[![Stories in Ready](https://badge.waffle.io/v-ladynev/fluent-hibernate.svg?label=ready&title=Ready)](http://waffle.io/v-ladynev/fluent-hibernate)
[![In Progress](https://badge.waffle.io/v-ladynev/fluent-hibernate.svg?label=in%20progress&title=In%20Progress)](http://waffle.io/v-ladynev/fluent-hibernate)
