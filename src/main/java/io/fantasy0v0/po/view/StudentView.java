package io.fantasy0v0.po.view;

import io.fantasy0v0.po.clazz1.Clazz1;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

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
