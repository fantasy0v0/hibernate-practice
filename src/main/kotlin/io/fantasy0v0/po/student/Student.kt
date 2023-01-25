package io.fantasy0v0.po.student

import io.fantasy0v0.po.clazz.Clazz
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime
import jakarta.persistence.*

@Entity
@DynamicUpdate
class Student(
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long? = null,

  @Column(nullable = false)
  var name: String,

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(foreignKey = ForeignKey(value = ConstraintMode.CONSTRAINT))
  var clazz: Clazz,

  @Column(nullable = false, updatable = false)
  var createdAt: LocalDateTime = LocalDateTime.now()
)