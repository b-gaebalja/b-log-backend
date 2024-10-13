package com.bgaebalja.blogbackend.post.domain;

import com.bgaebalja.blogbackend.post.exception.ContentNoValueException;
import com.bgaebalja.blogbackend.util.FormatValidator;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

import static com.bgaebalja.blogbackend.post.exception.ExceptionMessage.CONTENT_NO_VALUE_EXCEPTION_MESSAGE;

public class Content {
    private final String content;

    private Content(String content) {
        this.content = content;
    }

    public static Content of(String content) {
        validate(content);

        return new Content(content);
    }

    private static void validate(String content) {
        checkContentHasValue(content);
    }

    private static void checkContentHasValue(String content) {
        if (!FormatValidator.hasValue(content)) {
            throw new ContentNoValueException(CONTENT_NO_VALUE_EXCEPTION_MESSAGE);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Content content1 = (Content) object;
        return Objects.equals(content, content1.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    String getValue() {
        return content;
    }

    @Converter
    public static class ContentConverter implements AttributeConverter<Content, String> {
        @Override
        public String convertToDatabaseColumn(Content content) {
            return content.content;
        }

        @Override
        public Content convertToEntityAttribute(String content) {
            return content == null ? null : new Content(content);
        }
    }
}
