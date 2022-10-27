package io.fantasy0v0.po.student

import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*

@Entity
@DynamicUpdate
class Student(
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long? = null,

  @Column(nullable = false)
  var name: String
)