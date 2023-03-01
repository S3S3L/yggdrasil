package io.github.s3s3l.yggdrasil.test;

import java.lang.reflect.AnnotatedElement;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import io.github.s3s3l.yggdrasil.test.base.Timeout;

public class TimeCalExtension
        implements BeforeAllCallback, BeforeTestExecutionCallback, AfterTestExecutionCallback, AfterAllCallback {
    private LocalDateTime total = null;
    private LocalDateTime each = null;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        total = LocalDateTime.now();
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        each = LocalDateTime.now();
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        long usedTime = each.until(LocalDateTime.now(), ChronoUnit.MILLIS);

        System.out.println(String.format("%s spend %dms", context.getDisplayName(), usedTime));

        verifyTimeout(context, usedTime);
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        long usedTime = total.until(LocalDateTime.now(), ChronoUnit.MILLIS);

        System.out.println(String.format("%s spend %dms", context.getDisplayName(), usedTime));

        verifyTimeout(context, usedTime);
    }

    private void verifyTimeout(ExtensionContext context, long usedTime) {
        if (context.getElement()
                .isPresent()) {
            AnnotatedElement annotatedElement = context.getElement()
                    .get();
            if (annotatedElement.isAnnotationPresent(Timeout.class)) {
                Timeout timeout = annotatedElement.getAnnotation(Timeout.class);
                if (timeout.value() >= 0) {
                    Assertions.assertTrue(usedTime < timeout.value(),
                            () -> context.getDisplayName() + " cost timeout.");
                }
            }
        }
    }

}
