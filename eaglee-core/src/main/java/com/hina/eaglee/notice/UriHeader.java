package com.hina.eaglee.notice;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UriHeader {
    String value() default "";
}
