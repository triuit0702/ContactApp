package tri.chung.safetruttest.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Phone {

	String message() default "Invalid format, valid formats are 1234567890, 123-456-7890";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
