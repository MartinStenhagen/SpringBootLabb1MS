package org.example.springbootlabb1ms.book.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsbnValidator implements ConstraintValidator<Isbn, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }

        String normalized = value.trim().replaceAll("[-\\s]", "");

        if (normalized.length() == 10) {
            return isValidIsbn10(normalized);
        }

        if (normalized.length() == 13) {
            return isValidIsbn13(normalized);
        }

        return false;
    }

    private boolean isValidIsbn10(String isbn) {
        if (!isbn.matches("^\\d{9}[\\dXx]$")) {
            return false;
        }

        int sum = 0;

        for (int i = 0; i < 9; i++) {
            int digit = Character.getNumericValue(isbn.charAt(i));
            sum += digit * (10 - i);
        }

        char lastChar = isbn.charAt(9);
        int checkValue = (lastChar == 'X' || lastChar == 'x') ? 10 : Character.getNumericValue(lastChar);
        sum += checkValue;

        return sum % 11 == 0;
    }

    private boolean isValidIsbn13(String isbn) {
        if (!isbn.matches("^\\d{13}$")) {
            return false;
        }

        int sum = 0;

        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(isbn.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }

        int expectedCheckDigit = (10 - (sum % 10)) % 10;
        int actualCheckDigit = Character.getNumericValue(isbn.charAt(12));

        return expectedCheckDigit == actualCheckDigit;
    }
}
