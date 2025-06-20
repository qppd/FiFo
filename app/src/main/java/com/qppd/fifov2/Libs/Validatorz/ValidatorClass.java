package com.qppd.fifov2.Libs.Validatorz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorClass {

    private static String LETTERS_ONLY = "^[a-zA-Z ]+$";
    private static String NUMBERS_ONLY = "^[0-9]+$";
    private static String DECIMAL_NUMBERS_ONLY = "^[0-9 .]+$";
    private static String EMAILS_ONLY = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static Pattern pattern;
    private static Matcher matcher;

    public static boolean validateLetterOnly(String value) {
        pattern = Pattern.compile(LETTERS_ONLY);
        matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public static boolean validateNumberOnly(String value) {
        pattern = Pattern.compile(NUMBERS_ONLY);
        matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public static boolean validateDecimalNumberOnly(String value) {
        pattern = Pattern.compile(DECIMAL_NUMBERS_ONLY);
        matcher = pattern.matcher(value);
        return matcher.matches();
    }


    public static boolean validateEmailOnly(String value) {
        pattern = Pattern.compile(EMAILS_ONLY);
        matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public static boolean validateUsernameOnly(String value) {
        // Check if the username is at least 6 characters long
        if (value.length() < 6) {
            return false;
        }

        // Check if the username contains at least one letter and one digit
        boolean hasLetter = false;
        boolean hasDigit = false;

        for (char c : value.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else {
                // If the username contains a special character, return false
                return false;
            }
        }

        // Return true if the username has both letters and digits without special characters
        return hasLetter && hasDigit;
    }

    public static boolean validatePasswordOnly(String password) {
        // Check if the password length is at least 6 characters
        if (password.length() < 6) {
            return false;
        }

        // Regular expression pattern to match at least one letter
        String letterRegex = ".*[a-zA-Z].*";

        // Regular expression pattern to match at least one number
        String numberRegex = ".*\\d.*";

        // Regular expression pattern to match at least one special character
        String specialCharRegex = ".*[~`!@#$%^&*()\\-_=+\\[\\]{}|\\\\;:\"><>,./?].*";

        // Check if the password meets all the criteria
        return password.matches(letterRegex) &&
                password.matches(numberRegex) &&
                password.matches(specialCharRegex);
    }

    public static boolean validatePasswordNoSymbolOnly(String password) {
        // Check if the password length is at least 6 characters
        if (password.length() < 6) {
            return false;
        }

        // Regular expression pattern to match at least one letter
        String letterRegex = ".*[a-zA-Z].*";

        // Regular expression pattern to match at least one number
        String numberRegex = ".*\\d.*";

        // Regular expression pattern to ensure no special characters
        String noSpecialCharRegex = "^[a-zA-Z0-9]*$";

        // Check if the password meets all the criteria
        return password.matches(letterRegex) &&
                password.matches(numberRegex) &&
                password.matches(noSpecialCharRegex);
    }

    public static boolean validateDateOnly(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean validatePhoneOnly(String number) {
        String cleanedNumber = number.replaceAll("[^0-9]", "");
        return cleanedNumber.matches("(09|\\+639)\\d{9}");
    }


}
