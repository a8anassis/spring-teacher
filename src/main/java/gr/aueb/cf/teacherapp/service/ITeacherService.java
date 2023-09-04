package gr.aueb.cf.teacherapp.service;

import gr.aueb.cf.teacherapp.dto.TeacherInsertDTO;
import gr.aueb.cf.teacherapp.dto.TeacherUpdateDTO;
import gr.aueb.cf.teacherapp.model.Teacher;
import gr.aueb.cf.teacherapp.service.exceptions.EntityNotFoundException;

import java.util.List;

public interface ITeacherService {
    Teacher insertTeacher(TeacherInsertDTO dto) throws Exception;
    Teacher updateTeacher(TeacherUpdateDTO dto) throws EntityNotFoundException;
    Teacher deleteTeacher(Long id) throws EntityNotFoundException;
    List<Teacher> getTeachersByLastname(String lastname) throws EntityNotFoundException;
    Teacher getTeacherById(Long id) throws EntityNotFoundException;
}

