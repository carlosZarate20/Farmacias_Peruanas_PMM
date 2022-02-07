package com.farmaciasperuanas.pmmli.monitor.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatMessageError {
    private static String CLASS_TRUNCATION_MYSQL_CLASS ="class com.mysql.cj.jdbc.exceptions.MysqlDataTruncation";
    private static String PARSE_EXCEPTION_CLASS = "class java.text.ParseException";
    private static String PARSE_NUMBER = "class java.lang.NumberFormatException";
    private static String NULL_CONSTRAINT_BD = "class java.sql.SQLIntegrityConstraintViolationException";
    private static String getPortion(String error, String patternStr){
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(error);
        List<Integer> indexes = new ArrayList<>();
        while(matcher.find()) {
            indexes.add(matcher.start()) ;
        }
        String value = error.substring(indexes.get(0),indexes.get(1)+1);
        return value;
    }
    public static String getNewMessage(String error, String className, Throwable exception){
        String message="";
        if(className.equals(CLASS_TRUNCATION_MYSQL_CLASS)){
            String column = getPortion(error,"\\'");
            message += "Longitud del campo "+ column + " mayor al definido, revisar el diccionario de datos";
        } else if(className.equals(PARSE_EXCEPTION_CLASS)) {
            String value = getPortion(error,"\\\"");
            message += "Formato de fecha incorrecto: "+value+", el formato debe ser yyyyMMdd";
        } else if(className.equals(PARSE_NUMBER)){
            String value = getPortion(error,"\\\"");
            message += "Formato número incorrecto: "+value+", debe ser un decimal";
        } else if(className.equals(NULL_CONSTRAINT_BD)){
            String value = getPortion(error,"\\'");
            message += "La columna "+value+" no puede ser un valor nulo o vacío";
        }else{
            message += error+className;
        }

        return message;
    }

    public static String getErrorMessage(Exception e) {
        if (e.getCause() != null) {
            if (e.getCause().getCause() == null) {
                return getNewMessage(e.getCause().getMessage(), e.getCause().getClass().toString(), e.getCause());
            } else {
                return getNewMessage(e.getCause().getCause().getMessage(), e.getCause().getCause().getClass().toString(), e.getCause().getCause());
            }
        } else {
            return getNewMessage(e.getMessage(), e.getClass().toString(), e);
        }
    }
}