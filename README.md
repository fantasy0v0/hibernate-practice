# Spring Data JPA 使用经验总结

## 说明
本项目使用Kotlin作为主要语言, 但如果与Java的版本有较大差异时, 我也会单独写一份Java版的样例供大家参考  

## 前置工作

## 依赖
建议引入以下依赖, 该依赖会自动生成实体类的一些可能会用到的对象和常量, 方便在后面的条件查询时使用

```xml
<dependency>
  <groupId>org.hibernate</groupId>
  <artifactId>hibernate-jpamodelgen</artifactId>
</dependency>
```

## 实体类的定义

### Java
在引入Lombok的前提下, 可以参考以下方式

```java
/**
 * 不直接使用@Data的原因是, Lombok自动生成的方法会在调用时触发懒加载, 例如toString会打印实体类中所有属性的值
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DynamicUpdate
public class Student {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(foreignKey = ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
  private Clazz clazz;
  
  @CreationTimestamp
  @Column(insertable = false)
  private LocalDateTime createdAt;
}
```
创建一个实体对象, 利用Builder模式, 可以只填写非空字段
```java
var po = Student.builder().name("xxx").build();
```

### Kotlin

Kotlin需要配合NoArg、AllOpen插件才能正常使用, 可以参考本项目的配置
```kotlin
@Entity
@DynamicUpdate
class Student(
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long = 0,

  @Column(nullable = false)
  var name: String,
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(foreignKey = ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
  var clazz: Clazz,
  
  @CreationTimestamp
  @Column(insertable = false)
  var createdAt: LocalDateTime = LocalDateTime.now()
)
```
创建一个实体对象, 利用Kotlin的特性, 可以只填写非空字段
```kotlin
val student = Student(name = "xxx")
```

### 维护好@ManyToOne
如果想方便的联表查询, 必须维护

@OneToMany, 可有可无, 不是特别有用

## Repository
建议新建一个Repository接口来继承JpaRepository、JpaSpecificationExecutor, 这样每个实体仓库都只需要继承一个接口即可, 具体参考项目中的AbstractRepository接口。

## 部分需求的解决方案

### 单表查询
在定义好对应的实体类和Repository接口后, 只需要调用Repository的findById方法即可实现单表查询
```kotlin
// 用于不确定记录是否存在的场景
studentRepository.findByIdOrNull
// 用于确认记录肯定存在时, 仅仅是需要一个引用时使用
studentRepository.getReferenceById
```

### 多表关联
涉及多表查询时, 请先维护好实体们的@ManyToOne

在维护好多对一关系后, 只需像下面一样即可实现多表关联查询
```kotlin
val student = studentRepository.getReferenceById(1L)
// 查询学生所在的班级名称
println(student.clazz.name)
// 该方法在开启懒加载时会导致产生2次查询的问题, 后面的章节会介绍
```
具体可以到ManyToOneTest#test1中进行试用和调试

### 简单条件查询
该章节将为大家介绍如何在Spring Data Jpa中如何进行简单条件查询

#### 根据Id查询
如果我们需要根据id查询, 可以使用我们之前编写好的Repository接口的getReferenceById方法查询

#### 根据id以外的字段查询
光有根据Id查询是不能满足日常的开发工作的, 我们通常还会需要根据其他字段进行查询

此时我们可以使用Spring Data Jpa提供的[Query Methods](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods)来快速编写一些简单的查询方法

```kotlin
interface StudentRepository: AbstractRepository<Student, Long> {

  /**
   * 根据名称查询
   */
  fun findAllByName(name: String): List<Student>
}
```
调用该方法, 会为我们自动生成如下的**HQL**语句
```hql
select s from Student s where s.name = ?1
```
#### 不太想用Query Methods, 有没有直观一点的方法?
如果你也有这样的烦恼的, 可以尝试一下@Query注解, 它支持我们直接编写HQL或SQL
```kotlin
interface StudentRepository: AbstractRepository<Student, Long> {

  /**
   * 根据名称查询
   */
  @Query("""
    select s from Student s where s.name = ?1
  """)
  fun findAllByNameWithQuery(name: String): List<Student>

  /**
   * 根据名称查询, 使用原生sql语句
   */
  @Query("""
    select * from student where name = ?1
  """, nativeQuery = true)
  fun findAllByNameWithNativeQuery(name: String): List<Student>
}
```

### 如何调用原生的函数

JPA默认支持的函数不多,
可以参考[该章节](https://docs.jboss.org/hibernate/orm/6.0/userguide/html_single/Hibernate_User_Guide.html#hql-exp-functions)
知道支持的函数列表

对于原生函数的调用, 可以通过以下方法来实现

```hql
select s from Student s where function('to_char', s.createdAt, 'yyyy-MM-dd HH:mm:ss') 
```

详情可见单元测试**EntityManagerTest#testNativeFunction**

## ❗注意事项

### ❗如何做到动态更新

默认情况下, JPA每次更新都会set所有的非主键字段, 但有些时候我们只需要更新部分字段, 该如何实现呢?

使用@DynamicUpdate注解, 有了该注解的实体类, 在进行更新操作时, 只会更新有数据变更的列

### ❗阻止某些列参与更新

有些时候, 我们希望就算某些属性发送了变更, 也不要更新到数据库中, 此时只需要在@Column的参数声明updatable = false即可

### ❗ManyToOne如何不使用外键
默认情况下, ManyToOne会自动生成一条外键, 部分公司或开发人员可能更倾向于使用没有外键的方式

我们可以通过使用JoinColumn注解取消外键的生成
```kotlin
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(foreignKey = ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
var clazz: Clazz
```

### ❗为什么我没有调用更新方法, 最终却更新到数据库中?
在手动开启事务的情况下(open-in-view的不算), Jpa会提交你对实体类做的任何修改(尽管你没有调用更新方法).
```kotlin
/**
 * 在这个例子中的最后, 
 * 我们修改了clazz的name, 
 * 尽管我们没有进行任何的更新和提交操作, 
 * jpa还是替我们提交了对clazz的修改
 */
@Transactional
@GetMapping("/test")
open fun test(): String {
  val clazz = clazzHelper.create()
  clazz.name = "modify"
  return String.format("%s %s", clazz.id, clazz.name)
}
```

据我了解目前无法对JPA的这种行为进行限制, 不过如果我们换一种思路, 实体类就是数据库中记录的引用, 更新实体类就是在更新表记录, 这样是否更加容易接受呢?

所以, 最好不要将实体类用于其他用途, 只作为数据库记录的载体而使用。

### 不启用open-in-view时的注意事项
在涉及懒加载操作时, 需要主动开启事务


## 复杂条件查询
看过之前章节的人应该会发现, 简单条件查询很难满足实际开发需求, 我们可以通过接下来的内容来了解如何在Spring Data Jpa中进行复杂条件查询

> 可以尝试了解一下[HQL](https://docs.jboss.org/hibernate/orm/6.2/userguide/html_single/Hibernate_User_Guide.html),
>
> 它比Spring Data Jpa提供的方法更加灵活
>
> 接下来为大家介绍一些复杂的查询案例, 看看是否能解决你的需求

## Repository中的方法如何返回非当前实体的对象?

> 日常开发中经常会进行一些联表查询, 只返回一个实体对象是远远不能满足需求的

此时需要另外定义一个类来接收这多个实体

```kotlin
// 类定义
class StudentClassDto(
  val student: Student,
  val clazz: Clazz) {
}

// Repository中的方法可以这样写
@Query("""
    select 
    new io.fantasy0v0.po.student.dto.StudentClassDto(s, c)
    from Student s left join Clazz c on c = s.clazz
  """)
fun findAll(): List<StudentClassDto>
```

详情可见单元测试**DtoTest#test_1**

@Query对应的代码版

```kotlin
val cb = entityManager.criteriaBuilder
val cq = cb.createQuery(StudentClassDto::class.java)
val root = cq.from(Student::class.java)
cq.multiselect(
  root,
  root[Student_.clazz]
)
cq.where(cb.equal(root[Student_.id], student.id))
val query = entityManager.createQuery(cq)
query.firstResult = 0
query.maxResults = 1
Assertions.assertEquals(student.id, query.singleResult.student.id)
Assertions.assertEquals(student.clazz.id, query.singleResult.clazz.id)
```

详情可见单元测试**SpecificationTest#testDto**

## 使用@Query进行动态条件查询

```kotlin
@Query("""
  Select s from Student s left join fetch Clazz c on c = s.clazz
  Where
    (?1 is null or c.id = ?1)
  and
    (?2 is null or s.id = ?2)
""")
fun findAll_2(clazzId: Long?, studentId: Long?): List<Student>
```

## 使用Specification进行动态条件查询

TODO 目前发现的缺陷

- @Query不能和Specification同时使用
- Specification只能返回Entity
- 学习成本较高

## 利用@Subselect注解解决Specification的局限性

利用@Subselect注解来解决复杂多表查询, 并且还存在动态条件的问题

> 如果不存在动态条件那可以直接使用@Query注解

```java

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Immutable
@Subselect("""
  SELECT
    s.id, s.name, c.id as clazz_id, c.name as clazz_name
  from student s left join clazz c on s.clazz_id = c.id 
  """)
@Synchronize({"student", "clazz"})
public class StudentView {

  @Id
  private long id;

  @Column
  private String name;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Clazz1 clazz;

  @Column
  private String clazzName;
}
```

由于我们这个类对应的并非数据库的表, 所以我们需要取消和增加一些注解来表明它无法进行**更新**操作

需要取消的注解:

- @Setter
- @Builder

需要增加的注解:

- @Immutable 表明该Entity只读
- @Subselect 关联的查询语句
- @Synchronize 自动flush指定表, 避免无法查询到对应的数据

## 经验总结

### Spring Data Jpa的findById可能并不好用

findById可以帮我们快速获取一个实体类, 但是我们的实体类中如果有懒加载字段, 并且我们还需要使用这个懒加载字段时, 就会产生*
*2次查询**

使用以下的hql可以帮助我们在获取实体类体的同时，获取它的懒加载字段的实体, 并且只产生**1次查询**

```hql
select s from Student s join fetch Clazz where s.id = 1 
```

### 我们可能并不需要一个实体类?

TODO 尽量直接通过HQL或@Query将实体转换成DTO或者VO, 而不是直接操作实体
