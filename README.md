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

## The Most Useful Features

_fluent-hibrnate_ has the features wich can be used with plain Hibernate or Spring code
without a library infrastructure.

### Scan the class path for Hibernate entities

_fluent-hibernate_ can be used for a quick entites scanning. You will need just the library jar,
without additional dependencies. Just download the library using [Download](#download) section and use [EntityScanner](https://github.com/v-ladynev/fluent-hibernate/blob/master/fluent-hibernate-core/src/main/java/com/github/fluent/hibernate/cfg/scanner/EntityScanner.java):

_For Hibernate 4 and Hibernate 5:_
```Java
    Configuration configuration = new Configuration();
    EntityScanner.scanPackages("my.com.entities", "my.com.other.entities")
        .addTo(configuration);
    SessionFactory sessionFactory = configuration.buildSessionFactory();
```

_Using a new Hibernate 5 bootstrapping API:_
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
