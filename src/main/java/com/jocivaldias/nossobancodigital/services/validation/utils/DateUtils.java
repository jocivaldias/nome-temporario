package com.jocivaldias.nossobancodigital.services.validation.utils;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {

    public static boolean maiorDeIdade(LocalDate dataDeNascimento){
        return Period.between(dataDeNascimento, LocalDate.now()).getYears() >= 18;
    }


}
