package com.bgaebalja.blogbackend.util;

import com.bgaebalja.blogbackend.exception.EmailNoValueException;
import com.bgaebalja.blogbackend.exception.IdNoValueException;
import com.bgaebalja.blogbackend.exception.InvalidEmailException;
import com.bgaebalja.blogbackend.exception.InvalidIdException;

import static com.bgaebalja.blogbackend.exception.ExceptionMessage.*;
import static com.bgaebalja.blogbackend.util.RegularExpressionConstant.EMAIL_PATTERN;
import static com.bgaebalja.blogbackend.util.RegularExpressionConstant.POSITIVE_INTEGER_PATTERN;

public class InputFormatValidator {
    public static void validateEmail(String email) {
        checkEmailHasValue(email);
        checkEmailPattern(email);
    }

    private static void checkEmailHasValue(String email) {
        if (email == null || email.isBlank()) {
            throw new EmailNoValueException(EMAIL_NO_VALUE_EXCEPTION_MESSAGE);
        }
    }

    private static void checkEmailPattern(String email) {
        if (!email.matches(EMAIL_PATTERN)) {
            throw new InvalidEmailException(INVALID_EMAIL_EXCEPTION_MESSAGE + email);
        }
    }

    public static void validateId(String id) {
        checkIdIsNotBlank(id);
        checkIdPattern(id);
    }

    private static void checkIdIsNotBlank(String id) {
        if (id == null || id.isBlank()) {
            throw new IdNoValueException(ID_NO_VALUE_EXCEPTION_MESSAGE);
        }
    }

    private static void checkIdPattern(String id) {
        if (!id.matches(POSITIVE_INTEGER_PATTERN)) {
            throw new InvalidIdException(INVALID_ID_EXCEPTION_MESSAGE + id);
        }
    }
}
