package com.bgaebalja.blogbackend.util;

import com.bgaebalja.blogbackend.exception.EmailNoValueException;
import com.bgaebalja.blogbackend.exception.IdNoValueException;
import com.bgaebalja.blogbackend.exception.InvalidEmailException;
import com.bgaebalja.blogbackend.exception.InvalidIdException;

import java.util.List;

import static com.bgaebalja.blogbackend.exception.ExceptionMessage.*;
import static com.bgaebalja.blogbackend.util.RegularExpressionConstant.EMAIL_PATTERN;
import static com.bgaebalja.blogbackend.util.RegularExpressionConstant.POSITIVE_INTEGER_PATTERN;

public class FormatValidator {
    public static void validateEmail(String email) {
        if (!hasValue(email)) {
            throw new EmailNoValueException(EMAIL_NO_VALUE_EXCEPTION_MESSAGE);
        }
        if (!isValid(email, EMAIL_PATTERN)) {
            throw new InvalidEmailException(String.format(INVALID_EMAIL_EXCEPTION_MESSAGE + email));
        }
    }

    public static void validateId(String id) {
        if (!hasValue(id)) {
            throw new IdNoValueException(ID_NO_VALUE_EXCEPTION_MESSAGE);
        }
        if (!isValid(id, POSITIVE_INTEGER_PATTERN)) {
            throw new InvalidIdException(String.format(INVALID_ID_EXCEPTION_MESSAGE, id));
        }
    }

    public static boolean hasValue(String value) {
        return value != null && !value.isBlank();
    }

    public static boolean hasValue(List value) {
        return value != null && !value.isEmpty();
    }

    private static boolean isValid(String value, String pattern) {
        return value.matches(pattern);
    }
}
