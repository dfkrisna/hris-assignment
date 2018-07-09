package com.pusilkom.hris.util;

import com.google.common.collect.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ValidatorUtil {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public boolean isNotSelected(Long input) {
        return (input == -1L);
    }

    public boolean isBlank(String input) {
        return (input == null || !StringUtils.hasText(input));
    }

    public boolean isBlank(Long input) {
        return (input == null || input == -1);
    }

    public boolean isBlank(BigDecimal input) {
        return (input == null);
    }

    public boolean isBlank(Date input) {
        return (input == null);
    }

    public boolean isBlank(List input) {
        return (input == null);
    }
    
    public boolean isBlank(CommonsMultipartFile input) {
        return (input.isEmpty());
    }

    public boolean hasTrailing(String input) {
        return !input.equals(input.trim());
    }

    public boolean isValidEmailAddress(String email) {
        String EMAIL_PATTERN = "^[_a-z0-9-\\+]+(\\.[_a-z0-9-]+)*@"
                + "[a-z0-9-]+(\\.[a-z0-9]+)*(\\.[a-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    public boolean isValidEmailAddressEndsWith(String email, String endsWith) {
        return email.endsWith(endsWith);
    }

    public boolean isLessThanOrEqualTo(int target, int limit) {
        return (target <= limit);
    }

    public boolean isLessThanOrEqualTo(long target, long limit) {
        return (target <= limit);
    }

    public boolean isContainHashCharacter(String input) {
        return (input.matches(".*(#).*"));
    }

    public boolean isOnlyContainAlphaNumeric(String input) {
        return (input.matches("^([A-Za-z]|[0-9])+$"));
    }

    public boolean isOnlyContainNumeric(String input) {
        return (input.matches("^[0-9]+$"));
    }

    public boolean isOnlyContainFloatingPointNumeric(String input) {
        return (input.matches("^[0-9.]+$"));
    }

    public boolean isInClosedRange(BigDecimal value, BigDecimal bottom, BigDecimal top) {
        return Range.closed(bottom, top).contains(value);
    }

    public boolean isPartiallyFilled(Integer field1, Integer field2) {
        return !((field1 == -1 && field2 == -1) || (field1 != -1 && field2 != -1));
    }

    public boolean isExceedLength(String input, int maxLength) {
        if (this.isBlank(input)) return true;

        return (input.length() > maxLength);
    }

    public boolean isSequence(Comparable... comparables) {
        Comparable prevItem = null;
        boolean isSequence = true;
        for (Comparable item : comparables) {
            if (item == null) {
                isSequence = false;
                break;
            }

            if (prevItem == null) {
                prevItem = item;
                continue;
            }

            if (prevItem.compareTo(item) > 0) {
                isSequence = false;
                break;
            }

            prevItem = item;
        }

        return isSequence;
    }
}
