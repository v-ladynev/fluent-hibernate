# fluent-hibernate
A library to work with Hibernate by fluent API. This library hasn't dependencies, except Hibernate libraries. It requires Java 1.6 and above. Now it can be used with Hibernate 5 only. To use the library with Hibernate 4, it should be rebuilt, using Hibernate 4 as a dependency.

[![Build Status](https://travis-ci.org/v-ladynev/fluent-hibernate.svg?branch=master)](https://travis-ci.org/v-ladynev/fluent-hibernate)

## Download
##### Direct link
[![Release 0.1.4](https://img.shields.io/badge/release-0.1.4-blue.svg)](https://github.com/v-ladynev/fluent-hibernate/releases/download/0.1.4/fluent-hibernate-core-0.1.4.jar)

##### Maven (`pom.xml`)
```XML
<dependency>
	<groupId>com.github.v-ladynev</groupId>
	<artifactId>fluent-hibernate-core</artifactId>
	<version>0.1.4</version>
</dependency>
```

##### Gradle (`build.gradle`)
```Gradle
'com.github.v-ladynev:fluent-hibernate-core:0.1.4'
```

## Examples
Get all users
```Java
List<User> users = H.<User> request(User.class).list();
```
Getting an user with a login "my_login"
```Java
final String loginToFind = "my_login";
User user = H.<User> request(User.class).eq("login", loginToFind).first();
```
#### A partial objects loading
Get all users, but only with "login" and "id" properties are filled (other properties will be null).
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
