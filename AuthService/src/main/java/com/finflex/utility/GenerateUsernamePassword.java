package com.finflex.utility;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class GenerateUsernamePassword {

    public static String generateUsername(String firstName, String lastName) {
        if (firstName == null || lastName == null || firstName.isEmpty() || lastName.isEmpty()) {
            throw new IllegalArgumentException("First name and last name must not be null or empty.");
        }
        firstName = replaceTurkishChars(firstName);
        lastName = replaceTurkishChars(lastName);

        String username = (firstName.charAt(0) + lastName).toLowerCase();

        return username;
    }

    private static String replaceTurkishChars(String input) {
        Map<Character, Character> turkishToEnglishMap = new HashMap<>();
        turkishToEnglishMap.put('ç', 'c');
        turkishToEnglishMap.put('ğ', 'g');
        turkishToEnglishMap.put('ı', 'i');
        turkishToEnglishMap.put('ö', 'o');
        turkishToEnglishMap.put('ş', 's');
        turkishToEnglishMap.put('ü', 'u');
        turkishToEnglishMap.put('Ç', 'C');
        turkishToEnglishMap.put('Ğ', 'G');
        turkishToEnglishMap.put('İ', 'I');
        turkishToEnglishMap.put('Ö', 'O');
        turkishToEnglishMap.put('Ş', 'S');
        turkishToEnglishMap.put('Ü', 'U');

        StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            result.append(turkishToEnglishMap.getOrDefault(c, c));
        }

        return result.toString();
    }
    public static String generatePassword() {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghi"
                +"jklmnopqrstuvwxyz!@#$%&";
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int index = (int) (Math.random() * chars.length());
            password.append(chars.charAt(index));
        }

        return password.toString();
    }
//    public static String hashPassword(String password) {
//        try {
//            MessageDigest digest = MessageDigest.getInstance("SHA-256");
//            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
//
//            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
//            for (byte b : encodedhash) {
//                String hex = Integer.toHexString(0xff & b);
//                if (hex.length() == 1) {
//                    hexString.append('0');
//                }
//                hexString.append(hex);
//            }
//            return hexString.toString();
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public static String generatePersonelNumber(){
        String chars = "0123456789";
        StringBuilder personelNumber = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            int index = (int) (Math.random() * chars.length());
            personelNumber.append(chars.charAt(index));
        }

        return personelNumber.toString();
    }
}
