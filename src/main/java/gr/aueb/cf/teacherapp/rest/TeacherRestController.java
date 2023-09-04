package gr.aueb.cf.teacherapp.rest;

import gr.aueb.cf.teacherapp.dto.TeacherInsertDTO;
import gr.aueb.cf.teacherapp.dto.TeacherReadOnlyDTO;
import gr.aueb.cf.teacherapp.dto.TeacherUpdateDTO;
import gr.aueb.cf.teacherapp.model.Teacher;
import gr.aueb.cf.teacherapp.service.ITeacherService;
import gr.aueb.cf.teacherapp.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.teacherapp.validator.TeacherInsertValidator;
import gr.aueb.cf.teacherapp.validator.TeacherUpdateValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/api")
public class TeacherRestController {

    private final ITeacherService teacherService;
    private final TeacherInsertValidator teacherInsertValidator;
    private final TeacherUpdateValidator teacherUpdateValidator;

    @Autowired
    public TeacherRestController(ITeacherService teacherService, TeacherInsertValidator teacherInsertValidator,
                                 TeacherUpdateValidator teacherUpdateValidator) {
        this.teacherService = teacherService;
        this.teacherInsertValidator = teacherInsertValidator;
        this.teacherUpdateValidator = teacherUpdateValidator;
    }


    @Operation(summary = "Get teachers by their lastname starting with initials")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teachers Found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeacherReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid lastname supplied",
                    content = @Content)})
    @RequestMapping(path = "/teachers", method = RequestMethod.GET)
    public ResponseEntity<List<TeacherReadOnlyDTO>> getTeachersByLastname(@RequestParam("lastname") String lastname) {
        List<Teacher> teachers;
        try {
            teachers = teacherService.getTeachersByLastname(lastname);
            List<TeacherReadOnlyDTO> teachersDTO = new ArrayList<>();
            for (Teacher teacher : teachers) {
                teachersDTO.add(convertToReadOnlyDto(teacher));
            }
            return new ResponseEntity<>(teachersDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Get a Teacher by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher Found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeacherReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Teacher not found",
                    content = @Content)})
    @RequestMapping(value = "/teachers/{teacherId}", method = RequestMethod.GET)
    public ResponseEntity<TeacherReadOnlyDTO> getTeacher(@PathVariable("teacherId") Long teacherId) {
        Teacher teacher;
        try {
            teacher = teacherService.getTeacherById(teacherId);
            TeacherReadOnlyDTO teacherDto = convertToReadOnlyDto(teacher);
            return new ResponseEntity<>(teacherDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Add a teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Teacher created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeacherReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                    content = @Content),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",
                    content = @Content)})
    @RequestMapping(value = "/teachers", method = RequestMethod.POST)
    public ResponseEntity<TeacherReadOnlyDTO> addTeacher(@Valid @RequestBody TeacherInsertDTO dto,
                                                       BindingResult bindingResult) {
        teacherInsertValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Teacher teacher = teacherService.insertTeacher(dto);
            TeacherReadOnlyDTO teacherDTO = convertToReadOnlyDto(teacher);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(teacherDTO.getId())
                    .toUri();

            return ResponseEntity.created(location).body(teacherDTO);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @Operation(summary = "Delete a Teacher by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher Deleted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeacherReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Teacher not found",
                    content = @Content)})
    @RequestMapping(value = "/teachers/{teacherId}", method = RequestMethod.DELETE)
    public ResponseEntity<TeacherReadOnlyDTO> deleteTeacher(@PathVariable("teacherId") Long teacherId) {
        try {
            Teacher teacher = teacherService.getTeacherById(teacherId);
            teacherService.deleteTeacher(teacherId);
            TeacherReadOnlyDTO teacherDTO = convertToReadOnlyDto(teacher);
            return new ResponseEntity<>(teacherDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Update a teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeacherReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized user",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Teacher not found",
                    content = @Content) })
    @RequestMapping(value = "/teachers/{teacherId}", method = RequestMethod.PUT)
    public ResponseEntity<TeacherReadOnlyDTO> updateTeacher(@PathVariable("teacherId") Long teacherId,
                                                            @Valid @RequestBody TeacherUpdateDTO dto,
                                                            BindingResult bindingResult) {
        if (!Objects.equals(teacherId, dto.getId())) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        teacherUpdateValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Teacher teacher = teacherService.updateTeacher(dto);
            TeacherReadOnlyDTO teacherDTO = convertToReadOnlyDto(teacher);
            return new ResponseEntity<>(teacherDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private TeacherReadOnlyDTO convertToReadOnlyDto(Teacher teacher) {
        TeacherReadOnlyDTO teacherDTO = new TeacherReadOnlyDTO();
        teacherDTO.setId(teacher.getId());
        teacherDTO.setFirstname(teacher.getFirstname());
        teacherDTO.setLastname(teacher.getLastname());
        return teacherDTO;
    }
}
