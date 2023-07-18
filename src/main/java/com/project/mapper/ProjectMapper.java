package com.project.mapper;

import com.project.model.Projekt;
import com.project.model.ProjektES;
import com.project.model.Student;
import com.project.model.dto.ProjektAllDto;
import com.project.model.dto.ProjektDto;
import com.project.model.dto.StudentDto;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjektAllDto projektToProjektAllDto(Projekt projekt);
    ProjektDto projektToProjektDto(Projekt projekt);
    StudentDto studentToStudentDto(Student student);
    //Projekt projektDtoToProjekt(ProjektDto projektDto);
    ProjektES projektToProjektES(Projekt projekt);
    //Projekt projektESToProjekt(ProjektES projektES);
}

