package org.dows.eaglee.notice;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UriParam {
    String value() default "";
}
