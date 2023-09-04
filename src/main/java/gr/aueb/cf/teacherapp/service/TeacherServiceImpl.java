package gr.aueb.cf.teacherapp.service;

import gr.aueb.cf.teacherapp.dto.TeacherInsertDTO;
import gr.aueb.cf.teacherapp.dto.TeacherUpdateDTO;
import gr.aueb.cf.teacherapp.model.Teacher;
import gr.aueb.cf.teacherapp.repository.TeacherRepository;
import gr.aueb.cf.teacherapp.service.exceptions.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TeacherServiceImpl implements ITeacherService {

    private final TeacherRepository teacherRepository;

    @Autowired
    public TeacherServiceImpl(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Transactional
    @Override
    public Teacher insertTeacher(TeacherInsertDTO dto) throws Exception {
        Teacher teacher = null;
        try {
            teacher = teacherRepository.save(convertInsertDtoToTeacher(dto));
            if (teacher.getId() == null) throw new Exception("Insert Error");
        } catch (Exception e) {
            log.info("Insert Error");
            throw e;
        }
        return teacher;
    }

    @Transactional
    @Override
    public Teacher updateTeacher(TeacherUpdateDTO dto) throws EntityNotFoundException {
        Teacher teacher = null;
        Teacher updatedTeacher = null;
        try {
            teacher = teacherRepository.findTeacherById(dto.getId());
            if (teacher == null) throw new EntityNotFoundException(Teacher.class, dto.getId());
            updatedTeacher = teacherRepository.save(convertUpdateDtoToTeacher(dto));
        } catch (EntityNotFoundException e) {
            log.info("Error in Update");
            throw e;
        }
        return updatedTeacher;
    }

    @Transactional
    @Override
    public Teacher deleteTeacher(Long id) throws EntityNotFoundException {
        Teacher teacher = null;

        try {
            teacher = teacherRepository.findTeacherById(id);
            if (teacher == null) throw new EntityNotFoundException(Teacher.class, id);
            teacherRepository.deleteById(id);
        } catch (EntityNotFoundException e) {
            log.info("Error in Update");
            throw e;
        }
        return teacher;
    }

    @Override
    public List<Teacher> getTeachersByLastname(String lastname) throws EntityNotFoundException {
        List<Teacher> teachers = new ArrayList<>();
        try {
            teachers = teacherRepository.findByLastnameStartingWith(lastname);
            if (teachers.size() == 0) throw new EntityNotFoundException(Teacher.class, 0L);
        } catch (EntityNotFoundException e) {
            log.info("Error in Get teachers by lastname");
            throw e;
        }
        return teachers;
    }

    @Override
    public Teacher getTeacherById(Long id) throws EntityNotFoundException {
        Teacher teacher;
        try {
            teacher = teacherRepository.findTeacherById(id);
            if (teacher == null) throw new EntityNotFoundException(Teacher.class, id);
        } catch (EntityNotFoundException e) {
            log.info("Error in Get teacher by id");
            throw e;
        }
        return teacher;
    }

    private Teacher convertInsertDtoToTeacher(TeacherInsertDTO dto) {
        return new Teacher(null, dto.getFirstname(), dto.getLastname());
    }

    private Teacher convertUpdateDtoToTeacher(TeacherUpdateDTO dto) {
        return new Teacher(dto.getId(), dto.getFirstname(), dto.getLastname());
    }
}
