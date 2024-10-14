package com.bgaebalja.blogbackend.util;

import com.bgaebalja.blogbackend.exception.InvalidTargetTypeException;
import com.bgaebalja.blogbackend.exception.ParsingLongException;
import com.bgaebalja.blogbackend.image.domain.TargetType;

import static com.bgaebalja.blogbackend.exception.ExceptionMessage.INVALID_TARGET_TYPE_EXCEPTION_MESSAGE;
import static com.bgaebalja.blogbackend.exception.ExceptionMessage.PARSING_LONG_EXCEPTION_MESSAGE;

public class FormatConverter {
    public static long parseToLong(String number) {
        try {
            return Long.parseLong(number);
        } catch (NumberFormatException nfe) {
            throw new ParsingLongException((String.format(PARSING_LONG_EXCEPTION_MESSAGE, number)));
        }
    }

    public static TargetType parseToTargetType(String targetType) {
        try {
            return TargetType.valueOf(targetType);
        } catch (IllegalArgumentException iae) {
            throw new InvalidTargetTypeException(String.format(INVALID_TARGET_TYPE_EXCEPTION_MESSAGE, targetType));
        }
    }

    public static String sanitizeFileName(String filename) {
        return filename.replaceAll("\\s+", "-")
                .replaceAll("[^a-zA-Z0-9._-]", "");
    }
}
