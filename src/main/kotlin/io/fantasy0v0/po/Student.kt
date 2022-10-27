package io.fantasy0v0.po

import jakarta.persistence.*

@Entity
class Student(
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long,

  @Column(nullable = false)
  var name: String
)