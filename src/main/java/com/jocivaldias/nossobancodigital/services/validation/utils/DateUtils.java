package com.jocivaldias.nossobancodigital.services.validation.utils;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {

    public static boolean maiorDeIdade(Date dataDeNascimento){
        return Period.between(convertToLocalDateViaInstant(dataDeNascimento), LocalDate.now()).getYears() >= 18;
    }

    //Fonte: https://www.baeldung.com/java-date-to-localdate-and-localdatetime
    private static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
