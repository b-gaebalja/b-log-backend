package com.bgaebalja.blogbackend.exception;

public class ExceptionMessage {
    public static final String PARSING_LONG_EXCEPTION_MESSAGE
            = "숫자값만 Long 타입으로 변환할 수 있습니다. 현재 변환 대상 값: %s";
    public static final String PARSING_INTEGER_EXCEPTION_MESSAGE
            = "숫자값만 Integer 타입으로 변환할 수 있습니다. 현재 변환 대상 값: %s";
    public static final String PARSING_SHORT_EXCEPTION_MESSAGE
            = "숫자값만 Short 타입으로 변환할 수 있습니다. 현재 변환 대상 값: %s";
    public static final String PARSING_FLOAT_EXCEPTION_MESSAGE
            = "소수값만 float 타입으로 변환할 수 있습니다. 현재 변환 대상 값: %s";
    public static final String PARSING_LOCAL_DATE_TIME_EXCEPTION_MESSAGE
            = "날짜값만 LocalDateTime 타입으로 변환할 수 있습니다. 현재 변환 대상 값: %s";
    public static final String PARSING_BOOLEAN_EXCEPTION_MESSAGE
            = "논리형 값만 Boolean 타입으로 변환할 수 있습니다. 현재 변환 대상 값: %s";
    public static final String INVALID_TARGET_TYPE_EXCEPTION_MESSAGE
            = "존재하는 도메인만 TargetType으로 변환할 수 있습니다. 현재 변환 대상 값: %s";

    public static final String TOKEN_NO_VALUE_EXCEPTION_MESSAGE = "로그인 후 다시 시도해 주세요.";

    public static final String EMAIL_NO_VALUE_EXCEPTION_MESSAGE = "이메일 주소는 필수값입니다.";
    public static final String INVALID_EMAIL_EXCEPTION_MESSAGE
            = "이메일 주소 형식이 잘못되었습니다. 예: abcd@abc.com\n전송된 이메일 주소: %s";

    public static final String ID_NO_VALUE_EXCEPTION_MESSAGE = "ID는 필수값입니다.";
    public static final String INVALID_ID_EXCEPTION_MESSAGE = "ID는 양의 정수(1 이상의 숫자값)여야 합니다. 전송된 ID: %s";
}
