package gr.aueb.cf.teacherapp.repository;

import gr.aueb.cf.teacherapp.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    List<Teacher> findByLastnameStartingWith(String lastname);
    Teacher findTeacherById(Long id);
}
