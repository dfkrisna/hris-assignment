package com.pusilkom.hris.validator.cmd;
import com.pusilkom.hris.dto.form.cmd.AuthItemChildCmd;
import com.pusilkom.hris.util.ValidatorUtil;
import com.pusilkom.hris.util.ValidatorWebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AuthItemChildCmdValidator implements Validator {

    @Autowired
    ValidatorUtil validatorUtil;

    @Autowired
    ValidatorWebUtil validatorWebUtil;

    @Override
    public boolean supports(Class<?> type) {
        return AuthItemChildCmd.class.isAssignableFrom(type);
    }

    @Override
    public void validate(Object o, Errors errors) {
        AuthItemChildCmd cmd = (AuthItemChildCmd) o;

        validatorWebUtil.validateNotBlank("parent", errors, cmd.getParent());
        validatorWebUtil.validateNotBlank("child", errors, cmd.getChild());
    }
}
