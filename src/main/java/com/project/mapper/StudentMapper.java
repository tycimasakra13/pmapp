package com.project.mapper;

import com.project.model.Projekt;
import com.project.model.ProjektES;
import com.project.model.Student;
import com.project.model.dto.ProjektAllDto;
import com.project.model.dto.ProjektDto;
import com.project.model.dto.StudentAllDto;
import com.project.model.dto.StudentDto;
import com.project.model.elastic.StudentES;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface StudentMapper {
    StudentAllDto studentToStudentAllDto(Student student);
    StudentDto studentToStudentDto(Student student);
    Student studentDtoToStudent(StudentDto studentDto);
    StudentES studentToStudentES(Student student);
    Student studentESToStudent(StudentES studentES);
}

