package com.zte.clonedata.web.dto.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * ProjectName: miaosha-com.miaoshaDemo.validator
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 16:47 2019/1/31
 * @Description:
 *
 *
 *
<dependency>
<groupId>org.hibernate</groupId>
<artifactId>hibernate-validator</artifactId>
<version>5.2.4.Final</version>
</dependency>
 */
@Component
public class ValidatorImpl implements InitializingBean {

    private Validator validator;

    public ValidationResult validator(Object object){//传入一个Object
        ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(object);
        if (constraintViolationSet.size()>0){
            //有错误
            result.setHasErrors(true);
            //循环获取错误信息
            constraintViolationSet.forEach(constraintViolation->{
                String errorMsg = constraintViolation.getMessage();
                String errorProperty = constraintViolation.getPropertyPath().toString();
                result.getErrorMsgMap().put(errorProperty,errorMsg);
            });
        }
        return result;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
}
