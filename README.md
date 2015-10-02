# fluent-hibernate
A library to work with Hibernate by fluent API. This library hasn't dependencies except Hibernate dependencies. It requires Java 1.6 and above.

### Download: [![Release 0.1.2](https://img.shields.io/badge/release-0.1.2-blue.svg)](https://github.com/v-ladynev/fluent-hibernate/releases/download/0.1.2/fluent-hibernate-0.1.2.jar)

### Current Project state: [![Build Status](https://travis-ci.org/v-ladynev/fluent-hibernate.svg?branch=master)](https://travis-ci.org/v-ladynev/fluent-hibernate)

## Examples
Get all users.

```Java
List<User> users = H.<User> request(User.class).list();
```

Getting user with login "my_login".

```Java
final String loginToFind = "my_login";
User user = H.<User> request(User.class).eq("login", loginToFind).first();
```

You can find other examples in the [fluent-hibernate-mysql project](https://github.com/v-ladynev/fluent-hibernate-mysql)
and in the subproject [fluent-hibernate-examples](https://github.com/v-ladynev/fluent-hibernate/tree/master/fluent-hibernate-examples/src/main)

## Contributors

- [V.Ladynev](https://plus.google.com/102177768964957793539/posts);
- [DoubleF1re](https://github.com/DoubleF1re);
- [samsonych](https://github.com/samsonych);
- [Lucas Levvy](https://github.com/Levvy055).


## Dependency Status
[![Dependency Status](https://www.versioneye.com/user/projects/56041a64f5f2eb00170007d4/badge.svg?style=flat)](https://www.versioneye.com/user/projects/56041a64f5f2eb00170007d4)

## Progress
[![Throughput Graph](https://graphs.waffle.io/v-ladynev/fluent-hibernate/throughput.svg)](https://waffle.io/v-ladynev/fluent-hibernate/metrics)

### TODO:
[![Stories in Ready](https://badge.waffle.io/v-ladynev/fluent-hibernate.svg?label=ready&title=Ready)](http://waffle.io/v-ladynev/fluent-hibernate)
[![In Progress](https://badge.waffle.io/v-ladynev/fluent-hibernate.svg?label=in%20progress&title=In%20Progress)](http://waffle.io/v-ladynev/fluent-hibernate)
