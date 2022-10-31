# Spring Data JPA 使用经验总结

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

Kotlin需要配合NoArg插件才能正常使用, 可以参考本项目的配置
```kotlin
@Entity
@DynamicUpdate
class Student(
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long? = null,

  @Column(nullable = false)
  var name: String,

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
```
具体可以到ManyToOneTest#test1中进行试用和调试

## 注意事项

### 如何做到动态更新
默认情况下, JPA每次更新都会set所有的非主键字段, 但有些时候我们只需要更新部分字段, 该如何实现呢?

使用@DynamicUpdate注解, 有了该注解的实体类, 在进行更新操作时, 只会更新有数据变更的列

### 阻止某些列参与更新
有些时候, 我们希望就算某些属性发送了变更, 也不要更新到数据库中, 此时只需要在@Column的参数声明updatable = false即可

### ManyToOne如何不使用外键
默认情况下, ManyToOne会自动生成一条外键, 部分公司或开发人员可能更倾向于使用没有外键的方式

我们可以通过使用JoinColumn注解取消外键的生成
```kotlin
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(foreignKey = ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
var clazz: Clazz
```

### 为什么我没有调用更新方法, 最终却更新到数据库中?
> _由于在Kotlin下没有复现, 所以需要先确认下是否是新版本做了改动还是Java才会有的问题_

默认情况下, JPA会提交你对实体类做的任何修改(尽管你没有调用更新方法).

据我了解目前无法阻止JPA的这种行为, 不过如果我们换一种思路, 实体类就是数据库中记录的引用, 更新实体类就是在更新表记录, 这样是否更加容易接受呢?

最好不要将实体类用于其他用途
