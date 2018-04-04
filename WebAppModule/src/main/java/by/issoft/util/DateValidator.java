package by.issoft.util;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DateValidator {
    private static final String DATE_PATTERN = "^([0-9][0-9])-([0-9][0-9])-(\\d{4})$";

    public boolean dateIsValid(String date) {
        Pattern datePattern = Pattern.compile(DATE_PATTERN);
        Matcher dateMatcher = datePattern.matcher(date);
        return dateMatcher.matches();
    }
}
