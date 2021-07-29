package com.tutuka.transactionmatcher.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class DateTimeFormatValidator implements ConstraintValidator<DateTimeFormat, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        if (Objects.isNull(s)) {
            return true;
        }

        try {
            DateTimeFormatter.ofPattern(s);
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
