package com.alvinxu.TheDailyGrind.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateFormatValidator implements ConstraintValidator<DateFormatConstraint, java.util.Date> {

	@Override
	public boolean isValid(java.util.Date value, ConstraintValidatorContext context) {
		/*
		if (value == null) return false;
		String[] dmy = value.split("-");
		return dmy.length == 3 &&
				dmy[0].matches("[0-3][0-9]") &&
				dmy[1].matches("[0-1][0-9]") &&
				dmy[2].matches("[0-9]{4}"); */
		
		return value != null;
	}

}
