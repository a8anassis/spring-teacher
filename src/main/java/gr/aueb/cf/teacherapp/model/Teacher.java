package gr.aueb.cf.teacherapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TEACHERS")
public class Teacher {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "FIRSTNAME", length = 512, nullable = false)
    private String firstname;

    @Column(name = "LASTNAME", length = 512, nullable = false)
    private String lastname;
}
