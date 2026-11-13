package com.example.demo.Validators;

import java.util.List;
import java.util.regex.Pattern;

public class AddressValidator {


   private final String ADDRESS_REGEX = "^[\\u0980-\\u09FF\\u09E6-\\u09EFa-zA-Z0-9\\s.,\\-#/()]+$";

    private final Pattern ADDRESS_PATTERN = Pattern.compile(ADDRESS_REGEX);

    public boolean isValidAddress(String address) {
        if (address == null) {
            return false;
        }

        String trimmedAddress = address.trim();

        if (trimmedAddress.isEmpty()) {
            return false;
        }

        if (trimmedAddress.length() < 3) {
            return false;
        }


        return ADDRESS_PATTERN.matcher(trimmedAddress).matches();
    }

    public boolean isValid(List<String> addresses) {
        if (addresses == null) {
            return false;
        }

        for (String address : addresses) {
            if (!isValidAddress(address)) {
                return false;
            }
        }

        return true;
    }
}
