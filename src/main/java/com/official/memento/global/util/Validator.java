package com.official.memento.global.util;

import static com.official.memento.global.exception.ErrorCode.INVALID_JSON_FORMAT;

import com.ibm.icu.text.BreakIterator;
import com.ibm.icu.util.ULocale;
import com.official.memento.global.exception.InvalidRequestBodyException;


public class Validator {

    public static void validateLengthContainEmoji(final String text, final int maxLength) {
        if (text == null) {
            return;
        }

        // ICU4J BreakIterator: "character" = grapheme cluster
        BreakIterator iterator = BreakIterator.getCharacterInstance(ULocale.ROOT);
        iterator.setText(text);

        int count = 0;
        int boundary = iterator.first();
        while ((boundary = iterator.next()) != BreakIterator.DONE) {
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