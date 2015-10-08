package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface Header {
	String project();

	String creationDate();

	String creator();

	int review() default 1;
}
