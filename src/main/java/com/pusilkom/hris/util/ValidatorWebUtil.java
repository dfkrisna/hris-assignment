package com.pusilkom.hris.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Component
public class ValidatorWebUtil {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ValidatorUtil validatorUtil;

    @Autowired
    MessageSource messageSource;

    public void validateNotBlank(String field, Errors errors, String input) {
        if (validatorUtil.isBlank(input)) {
            errors.rejectValue(field, "msg.validate.not.blank");
        }
    }

    public void validateNotBlank(String field, Errors errors, Long input) {
        if (validatorUtil.isBlank(input)) {
            errors.rejectValue(field, "msg.validate.not.blank");
        }
    }

    public void validateNotBlank(String field, Errors errors, BigDecimal input) {
        if (validatorUtil.isBlank(input)) {
            errors.rejectValue(field, "msg.validate.not.blank");
        }
    }

    public void validateNotBlank(String field, Errors errors, Boolean input) {
        if (input == null) {
            errors.rejectValue(field, "msg.validate.not.blank");
        }
    }

    public void validateNotBlank(String field, Errors errors, Date input) {
        if (validatorUtil.isBlank(input)) {
            errors.rejectValue(field, "msg.validate.not.blank");
        }
    }

    public void validateNotBlank(String field, Errors errors, List input) {
        if (validatorUtil.isBlank(input)) {
            errors.rejectValue(field, "msg.validate.not.blank");
        }
    }
    
    public void validateNotBlank(String field, Errors errors, CommonsMultipartFile input) {
        if (validatorUtil.isBlank(input)) {
            errors.rejectValue(field, "msg.validate.not.blank");
        }
    }

    public void validateNotContainTrailing(String field, Errors errors, String input) {
        if (validatorUtil.isBlank(input)) return;

        if (validatorUtil.hasTrailing(input)) {
            errors.rejectValue(field, "msg.validate.not.contain.trailing");
        }
    }

    public void validateEmailAddress(String field, Errors errors, String email) {
        if (validatorUtil.isBlank(email)) return;

        if (!validatorUtil.isValidEmailAddress(email)) {
            errors.rejectValue(field, "msg.validate.email");
        }
    }
    
    public void validateUsername(String field, Errors errors, String username) {
        if (validatorUtil.isBlank(username)) {
            return;
        }
        
        if (!validatorUtil.isValidEmailAddress(username)) {
//            errors.rejectValue(field, "msg.validate.username.invalid.format");
            errors.rejectValue(field, "msg.validate.uid.invalid.format");
        }
    }

    public void validateEmailAddressEndsWith(String field, Errors errors, String email, String endsWith) {
        if (validatorUtil.isBlank(email)) return;

        if (!validatorUtil.isValidEmailAddressEndsWith(email, endsWith)) {
            errors.rejectValue(field, null, "Alamat email wajib: " + endsWith);
        }
    }

    public void validateNotPartiallyFilled(String field, Errors errors, Integer field1, Integer field2) {
        if (validatorUtil.isPartiallyFilled(field1, field2))
            errors.rejectValue(field, "msg.validate.not.partially.filled");
    }

    public void validateLessThanOrEqualTo(String field, Errors errors, int target, int limit) {
        if (!validatorUtil.isLessThanOrEqualTo(target, limit)) {
            String msg = messageSource.getMessage("msg.validate.less", null, LocaleContextHolder.getLocale());
            String msgFormatted = MessageFormat.format(msg, limit);

            errors.rejectValue(field, null, msgFormatted);
        }
    }

    public void validateLessThanOrEqualTo(String field, Errors errors, long target, long limit) {
        if (!validatorUtil.isLessThanOrEqualTo(target, limit)) {
            String msg = messageSource.getMessage("msg.validate.less", null, LocaleContextHolder.getLocale());
            String msgFormatted = MessageFormat.format(msg, limit);

            errors.rejectValue(field, null, msgFormatted);
        }
    }


    public void validateNotContainHashCharacter(String field, Errors errors, String input) {
        if (validatorUtil.isContainHashCharacter(input)) {
            errors.rejectValue(field, "msg.validate.not.contain.hashcharacter");
        }
    }

    public void validateOnlyContainAlphaNumeric(String field, Errors errors, String input) {
        if (validatorUtil.isBlank(input)) return;

        if (!(validatorUtil.isOnlyContainAlphaNumeric(input))) {
            errors.rejectValue(field, "msg.validate.only.contain.alphanumeric");
        }
    }

    public void validateOnlyContainNumeric(String field, Errors errors, String input) {
        if (validatorUtil.isBlank(input)) return;

        if (!(validatorUtil.isOnlyContainNumeric(input))) {
            errors.rejectValue(field, "msg.validate.only.contain.numeric");
        }
    }

    public void validateOnlyContainFloatingPoint(String field, Errors errors, String input) {
        if (validatorUtil.isBlank(input)) return;

        if (!(validatorUtil.isOnlyContainFloatingPointNumeric(input))) {
            errors.rejectValue(field, "msg.validate.only.contain.floating-point");
        }
    }

    public void validateValueInClosedRange(BigDecimal value, BigDecimal bottom, BigDecimal top, String field, Errors errors) {
        if (!(validatorUtil.isInClosedRange(value, bottom, top))) {
            errors.rejectValue(field, "msg.validate.value.not.in.range");
        }
    }

    public void validateSequence(String field, Errors errors, Comparable... comparables) {
        validateSequence(null, field, errors, comparables);
    }

    public void validateSequence(String customMsg, String field, Errors errors, Comparable... comparables) {
        if (!validatorUtil.isSequence(comparables)) {
            if (customMsg == null) {
                errors.rejectValue(field, "msg.validate.sequence");
            } else {
                errors.rejectValue(field, null, customMsg);
            }
        }
    }

    public void validateDateSequence(String field, Errors errors, Date tanggalAwal, Date tanggalAkhir) {
        if (!validatorUtil.isSequence(tanggalAwal, tanggalAkhir)) {
            errors.rejectValue(field, "msg.validate.date.range");
        }
    }

    public void validateNotExceedLength(String field, Errors errors, String input, int maxLength) {
        if (validatorUtil.isBlank(input)) return;

        if (input.length() > maxLength) {
            String msg = messageSource.getMessage("msg.validate.not.exceed.length", null, LocaleContextHolder.getLocale());
            String msgFormatted = MessageFormat.format(msg, maxLength);

            errors.rejectValue(field, null, msgFormatted);
        }
    }
    
}
