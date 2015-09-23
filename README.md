# fluent-hibernate
A library to work with Hibernate by fluent API.

The download link:
[fluent-hibernate-0.1.1.jar](https://github.com/v-ladynev/fluent-hibernate/releases/download/v.0.1.1/fluent-hibernate-0.1.1.jar)

This library hasn't dependences except Hibernate dependences. It requires Java 1.6 and above.

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

[V.Ladynev](https://plus.google.com/102177768964957793539/posts),
[DoubleF1re](https://github.com/DoubleF1re),
[Serg samsonych](https://github.com/samsonych),
[Lucas Levvy](https://github.com/Levvy055)

## TODO
- [ ] Refactor custom transformer, add builder
- [ ] Add gradle building system
- [ ] Consider changing API for H.update("delete from User").execute();
- [ ] In testing environment https://discuss.gradle.org/t/how-to-get-multiple-versions-of-the-same-library/7400/2