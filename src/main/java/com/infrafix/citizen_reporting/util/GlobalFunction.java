package com.infrafix.citizen_reporting.util;

import com.infrafix.citizen_reporting.config.JwtConfig;
import com.infrafix.citizen_reporting.security.JwtUtility;
import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class GlobalFunction {

    public static final String AUTH_HEADERS = "Authorization";

    public static Map<String,Object> extractToken(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        token = token.substring(7);
        return new JwtUtility().mappingBodyToken(token);
    }


    public static Boolean checkValue(String value,String pattern){
        Boolean isValid = Pattern.compile(pattern).matcher(value).find();
        return isValid;
    }

    public static Map<String,Object> convertClassToMap(Object object){
        Map<String,Object> map = new LinkedHashMap<>();
        Field[] fields = object.getClass().getDeclaredFields();//Reflection
        for(Field field : fields){
            field.setAccessible(true);
            try{
                map.put(field.getName(),field.get(object));
            }catch(Exception e){

            }
        }
        return map;
    }

    public static String camelToStandard(String camel){
        StringBuilder sb = new StringBuilder();
        char c = camel.charAt(0);
        sb.append(Character.toLowerCase(c));
        for (int i = 1; i < camel.length(); i++) {
            char c1 = camel.charAt(i);
            if(Character.isUpperCase(c1)){
                sb.append(' ').append(Character.toLowerCase(c1));
            }
            else {
                sb.append(c1);
            }
        }
        return sb.toString();
    }
}
