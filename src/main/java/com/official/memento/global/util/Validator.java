package com.official.memento.global.util;

import static com.official.memento.global.exception.ErrorCode.INVALID_JSON_FORMAT;

import com.official.memento.global.exception.InvalidRequestBodyException;
import java.text.BreakIterator;

public class Validator {

    public static void validateLengthContainEmoji(final String text, final int maxLength) {
        BreakIterator iterator = BreakIterator.getCharacterInstance();
        iterator.setText(text);

        int count = 0;
        while (BreakIterator.DONE != iterator.next()) {
            count++;
        }
        if (count > maxLength) {
            throw new InvalidRequestBodyException(INVALID_JSON_FORMAT);
        }
    }

    public static void validateLength(final String text, final int maxLength) {
        if (text.length() > maxLength) {
            throw new InvalidRequestBodyException(INVALID_JSON_FORMAT);
        }
    }

    public static void isNull(final Object object) {
        if (object == null) {
            throw new InvalidRequestBodyException(INVALID_JSON_FORMAT);
        }
        if(object instanceof String) {
            if(((String) object).isEmpty()) {
                throw new InvalidRequestBodyException(INVALID_JSON_FORMAT);
            }
        }
    }
}