# fluent-hibernate
A library to work with Hibernate by fluent API. This library hasn't dependencies, except Hibernate libraries. It requires Java 1.6 and above. It can be used with Hibernate 5 and Hibernate 4.

[![Build Status](https://travis-ci.org/v-ladynev/fluent-hibernate.svg?branch=master)](https://travis-ci.org/v-ladynev/fluent-hibernate)

## Download
##### Direct link
[![Release 0.3.0](https://img.shields.io/badge/release-0.3.0-blue.svg)](https://github.com/v-ladynev/fluent-hibernate/releases/download/0.3.0/fluent-hibernate-core-0.3.0.jar)

##### Maven (`pom.xml`)
```XML
<dependency>
  <groupId>com.github.v-ladynev</groupId>
  <artifactId>fluent-hibernate-core</artifactId>
  <version>0.3.0</version>
</dependency>
```
All versions in the Maven repository: [fluent-hibernate-core](http://repo1.maven.org/maven2/com/github/v-ladynev/fluent-hibernate-core/)

##### Gradle (`build.gradle`)
```Gradle
compile 'com.github.v-ladynev:fluent-hibernate-core:0.3.0'
```

## Hibernate 4 Notes

If you want to use the library with Hibernate 4, you can consider to exclude a transitive dependency to Hibernate 5.
For an example using Gradle:
```Gradle
    compile('com.github.v-ladynev:fluent-hibernate-core:0.3.0') {
        exclude group: 'org.hibernate'
    }
```
## The Most Useful Features

_fluent-hibrnate_ has features which can be used with plain Hibernate or Spring code
without a library infrastructure.

### Scan the Class Path for Hibernate Entities

The library can be used for a quick entities scanning. You will need just the library jar,
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

### Hibernate 5 Implicit Naming Strategy

It generates table and column names with underscores, like [ImprovedNamingStrategy](https://docs.jboss.org/hibernate/orm/4.3/javadocs/index.html?org/hibernate/cfg/ImprovedNamingStrategy.html) from Hibernate 4 and Hibernate 3, and constraint names 
(unique, foreign key) as well. Apart those, it has a lot of configurable interesting features
like: plural table names, table and column prefixes, embedded column prefixes via the custom `@FluentName` annotation, automatic name restriction (by removing the vowels) and others.

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
### ImprovedNamingStrategy for Hibernate 5

It is imposible to use  Hibernate 4  naming strategies, for an example [org.hibernate.cfg.ImprovedNamingStrategy](https://docs.jboss.org/hibernate/orm/4.3/javadocs/index.html?org/hibernate/cfg/ImprovedNamingStrategy.html), with Hibernate 5 ([HHH-10155](https://hibernate.atlassian.net/browse/HHH-10155)).  To continue using `ImprovedNamingStrategy` functionality with Hibernate 5 
[ImprovedNamingStrategyHibernate5](https://github.com/v-ladynev/fluent-hibernate/blob/master/fluent-hibernate-core/src/main/java/com/github/fluent/hibernate/cfg/strategy/hibernate5/adapter/ImprovedNamingStrategyHibernate5.java) can be used. Apart an `ImprovedNamingStrategy` behaviour, `ImprovedNamingStrategyHibernate5` provides generation of foreign key and unique constraint names like Hibernate 4 does (with some limitations, see below). 

Just pass `ImprovedNamingStrategyHibernate5` to the Hibernate 5 configuration. `ImprovedNamingStrategyHibernate5` implements the `ImplicitNamingStrategy` interface, so you can place it everywhere `ImplicitNamingStrategy` can be placed.

For an example, using `Configuration`:

```Java
Configuration configuration = new Configuration();
configuration.setImplicitNamingStrategy(ImprovedNamingStrategyHibernate5.INSTANCE);
SessionFactory sessionFactory = configuration.configure().buildSessionFactory();
```
`ImprovedNamingStrategyHibernate5` can be used with JPA configuration as well.

### Limitations of ImprovedNamingStrategy for Hibernate 5 

Keep in mind, that `ImprovedNamingStrategyHibernate5` has some limitations are described below. I think, that these
limitations can be encountered in rarely situations. If you have problems with these situations fill free to create an [issue](https://github.com/v-ladynev/fluent-hibernate/issues/new) or make a pull request.

I used [ImprovedNamingStrategyHibernate5Test](https://github.com/v-ladynev/fluent-hibernate/blob/master/fluent-hibernate-core/src/test/java/com/github/fluent/hibernate/cfg/strategy/hibernate5/adapter/ImprovedNamingStrategyHibernate5Test.java) to test the strategy. To check an `ImprovedNamingStrategy` behaviour with Hibernate 4 I used this unit test: [ImprovedNamingStrategyTest](https://github.com/v-ladynev/hibernate4-experimental/blob/master/src/test/java/com/github/experimental/strategy/ImprovedNamingStrategyTest.java).

#### Constraint Names Generation
Foreign key and unique constraint names are generated by Hibernate 5 are slightly different from ones are generated by Hibernate 4. 

I implement a Hibernate 4 behaviour in `ImprovedNamingStrategyHibernate5`, but Hibernate 5  doesn't always use a naming strategy to generate unique constraint names. For an, example: a unique constraint for `@OneToMany` annotation using a join table and  `@Column(unique = true)` don't use `ImplicitNamingStrategy`. You need to delete such unique constraint names yourself, before Hibernate 5 will update a schema.   

#### @DiscriminatorColumn Name Generation

Hibernate 5 doesn't use a naming strategy to generate such names, because of an issue. Hiibernate 5 generates "DTYPE" name for the discriminator column. Hibernate 4 uses `ImprovedNamingStrategy#columnName()` to convert "DTYPE" to "dtype". You need to provide explicit names in such situation.

#### @OrderColumn Name Generation

Hibernate 5 doesn't use a naming strategy to generate such names, because of an issue. Hibernate 5 generates `booksOrdered_ORDER`, opposite `books_ordered_order` is generated by Hibernate 4 `ImprovedNamingStrategy`. You need to provide explicit names in such situation.

#### @MapKeyColumn Name Generation

Hibernate 5 doesn't use a naming strategy to generate such names, because of an issue. Hibernate 5 generates `booksMap_KEY`, opposite `books_map_key` is generated by Hibernate 4 `ImprovedNamingStrategy`. You need to provide explicit names in such situation.

#### Other Artifacts Name Generation
There are some methods in the `ImplicitNamingStrategy` are not used by Hibernate 5 to generate names. And there are some methods for which I can't represent unit tests. All these methods throw `UnsupportedOperationException`. The list of methods below
```Java
    Identifier determineTenantIdColumnName(ImplicitTenantIdColumnNameSource source);

    Identifier determineIdentifierColumnName(ImplicitIdentifierColumnNameSource source);

    Identifier determinePrimaryKeyJoinColumnName(ImplicitPrimaryKeyJoinColumnNameSource source);

    Identifier determineAnyDiscriminatorColumnName(ImplicitAnyDiscriminatorColumnNameSource source);

    Identifier determineAnyKeyColumnName(ImplicitAnyKeyColumnNameSource source);
```

### Hibernate4To5NamingStrategyAdapter
This adapter can be used to adapt Hibernate 4 naming strategies to Hibernate 5 `ImplicitNamingStrategy`. [ImprovedNamingStrategy for Hibernate 5](https://github.com/v-ladynev/fluent-hibernate#improvednamingstrategy-for-hibernate-5) is builded on top of it. So it has the same [limitations](https://github.com/v-ladynev/fluent-hibernate#limitations-of-improvednamingstrategy-for-hibernate-5).

You can use the adapter by this way:
```Java
Configuration configuration = new Configuration();
configuration.setImplicitNamingStrategy(new Hibernate4To5NamingStrategyAdapter(EJB3NamingStrategy.INSTANCE));
SessionFactory sessionFactory = configuration.configure().buildSessionFactory();
```
It can be used with JPA configuration as well. Probably, you  will need to extend `Hibernate4To5NamingStrategyAdapter` similar as [ImprovedNamingStrategyHibernate5](https://github.com/v-ladynev/fluent-hibernate/blob/master/fluent-hibernate-core/src/main/java/com/github/fluent/hibernate/cfg/strategy/hibernate5/adapter/ImprovedNamingStrategyHibernate5.java) extends it.

### Nested Transformer

It is a custom transformer like `Transformers.aliasToBean(SomeDto.class)`, but with the nested projections support.

Just download the library using [Download](#download) section and use [FluentHibernateResultTransformer](https://github.com/v-ladynev/fluent-hibernate/blob/master/fluent-hibernate-core/src/main/java/com/github/fluent/hibernate/transformer/FluentHibernateResultTransformer.java).

_Load `User` only with `login` and `department.name` fields_
```Java
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "pid")
    private Long pid;

    @Column(name = "login")
    private String login;

    @ManyToOne
    @JoinColumn(name = "fk_department")
    private Department department;

}

@Entity
@Table(name = "departments")
public class Department {

    @Id
    @Column(name = "pid")
    private Long pid;

    @Column(name = "name")
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
Please, don't forget to specify projection aliases. 

#### Using With Native SQL

It is impossible with Hibernate 5 to use `Transformers.aliasToBean(SomeDto.class)` the same way as
it is used with Hibernate 3 — without the aliases with the quotes ([a more deep explanation](http://stackoverflow.com/a/37423885/3405171)), but  it is possible using `FluentHibernateResultTransformer`. This code works pretty well:
```Java
List<User> users = session.createSQLQuery("select login from users")
        .setResultTransformer(new FluentHibernateResultTransformer(User.class))
        .list();
```
The transformer can be used for a native SQL with the nested projections (opposite HQL). 
It is need to use the aliases with the quotes in this case:
```Java
String sql = "select u.login, d.name as \"department.name\" "
        + "from users u left outer join departments d on u.fk_department = d.pid";
List<User> users = session.createSQLQuery(sql)
        .setResultTransformer(new FluentHibernateResultTransformer(User.class))
        .list();
```

### Ignore Alias Duplicates

Sometimes it is convenient using complex search criteries to add an alias multiple times. In such situations Hibernate generates the `org.hibernate.QueryException: duplicate alias` exception . There is a utility class to solve this problem: [Aliases](https://github.com/v-ladynev/fluent-hibernate/blob/master/fluent-hibernate-core/src/main/java/com/github/fluent/hibernate/request/aliases/Aliases.java). This code with alias duplicates working without errors:
```Java
Criteria criteria = session.createCriteria(User.class, "u");
Aliases aliases = Aliases.create()
        .add("u.department", "d", JoinType.LEFT_OUTER_JOIN)
        .add("u.department", "d", JoinType.LEFT_OUTER_JOIN);

criteria.add(Restrictions.isNotNull("d.name"));
aliases.addToCriteria(criteria);
List<User> users = criteria.list();
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
