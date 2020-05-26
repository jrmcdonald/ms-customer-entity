package com.jrmcdonald.ext.spring.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ServiceEntryInterceptor implements HandlerInterceptor {

    private static final String DURATION = "duration";
    private static final String HTTP_STATUS_CODE = "httpStatusCode";
    private static final String REQUEST_START_TIME = "requestStartTime";

    private final Clock clock;
    private final Logger log = createLogger();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) {
        request.setAttribute(REQUEST_START_TIME, clock.instant().toEpochMilli());
        log.info("Entering service");
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {
        addDurationAndStatusToMDC(request, response);
        log.info("Exiting service");
    }

    private void addDurationAndStatusToMDC(HttpServletRequest request, HttpServletResponse response) {
        String timeInMillis = calculateExecutionTime(request);
        if (timeInMillis != null) {
            MDC.put(DURATION, timeInMillis);
        }
        MDC.put(HTTP_STATUS_CODE, String.valueOf(response.getStatus()));
    }

    private String calculateExecutionTime(HttpServletRequest request) {
        Long requestStartTime = (Long) request.getAttribute(REQUEST_START_TIME);
        if (requestStartTime == null) {
            return null;
        }
        request.removeAttribute(REQUEST_START_TIME);
        return String.valueOf(Duration.between(Instant.ofEpochMilli(requestStartTime), clock.instant())
                                      .dividedBy(ChronoUnit.MILLIS.getDuration()));
    }

    protected Logger createLogger() {
        return LoggerFactory.getLogger(ServiceEntryInterceptor.class);
    }
}
