package gr.aueb.cf.teacherapp.validator;

import gr.aueb.cf.teacherapp.dto.TeacherInsertDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class TeacherInsertValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return TeacherInsertDTO.class == clazz;
    }

    @Override
    public void validate(Object target, Errors errors) {
        TeacherInsertDTO teacherInsertDto = (TeacherInsertDTO) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstname", "empty");
        if (teacherInsertDto.getFirstname().length() < 3 || teacherInsertDto.getFirstname().length() > 50) {
            errors.rejectValue("firstname", "size");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastname", "empty");
        if (teacherInsertDto.getLastname().length() < 3 || teacherInsertDto.getLastname().length() > 50) {
            errors.rejectValue("lastname", "size");
        }
    }
}
