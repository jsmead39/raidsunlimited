package raidsunlimited.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class ServiceUtils {
    static final int RAIDEVENT_ID_LENGTH = 5;
    private static final Pattern INVALID_CHARACTER_PATTERN = Pattern.compile("[^a-zA-Z0-9]");


    private ServiceUtils() {

    }

    /**
     *
     * @param stringToValidate Input string
     * @return return true if String is valid,  false if it contains any invalid characters
     */
    public static boolean isValidString(String stringToValidate) {
        if (StringUtils.isBlank(stringToValidate)) {
            return false;
        } else {
            return !INVALID_CHARACTER_PATTERN.matcher(stringToValidate).find();
        }
    }

    /**
     * Generates a randomalphanumeric Id.
     * @return randomId as a String
     */
    public static String generateRandomId() {
        return RandomStringUtils.randomAlphanumeric(RAIDEVENT_ID_LENGTH);
    }
}
