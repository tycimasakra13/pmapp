package com.project.mapper;

import com.project.model.Projekt;
import com.project.model.ProjektES;
import com.project.model.Student;
import com.project.model.Zadanie;
import com.project.model.ZadanieES;
import com.project.model.dto.ProjektAllDto;
import com.project.model.dto.ProjektDto;
import com.project.model.dto.StudentDto;
import com.project.model.dto.ZadanieAllDto;
import com.project.model.dto.ZadanieDto;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ZadanieMapper {
    ZadanieAllDto zadanieToZadanieAllDto(Zadanie zadanie);
    ZadanieDto zadanieToZadanieDto(Zadanie zadanie);
    Zadanie zadanieDtoToZadanie(ZadanieDto zadanieDto);
    ZadanieES zadanieToZadanieES(Zadanie zadanie);
    Zadanie zadanieESToZadanie(ZadanieES zadanieES);
    //Zadanie zadanieToProjekt(Projekt projekt);
}

