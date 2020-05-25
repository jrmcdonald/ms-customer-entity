package com.jrmcdonald.ext.spring.interceptor;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceEntryInterceptorTest {

    @Mock
    private Appender<ILoggingEvent> mockAppender;

    @Mock
    private Clock clock;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Captor
    private ArgumentCaptor<LoggingEvent> loggingEventArgumentCaptor;


    private ServiceEntryInterceptor serviceEntryInterceptor;

    @BeforeEach
    void beforeEach() {
        serviceEntryInterceptor = new ServiceEntryInterceptor(clock);
        MDC.clear();
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(clock, httpServletRequest, httpServletResponse);
    }

    @Test
    @DisplayName("Should log service entry message")
    void shouldLogServiceEntryMessage() {
        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.addAppender(mockAppender);

        Instant startTime = Instant.now();

        when(clock.instant()).thenReturn(startTime);
        doNothing().when(httpServletRequest).setAttribute(eq("requestStartTime"), eq(startTime.toEpochMilli()));

        serviceEntryInterceptor.preHandle(httpServletRequest, httpServletResponse, new Object());

        verify(mockAppender, atLeast(1)).doAppend(loggingEventArgumentCaptor.capture());

        List<LoggingEvent> loggingEvents = loggingEventArgumentCaptor.getAllValues();

        Optional<LoggingEvent> serviceEntryEvent = loggingEvents.stream()
                                                                .filter(event -> event.getFormattedMessage().matches("Entering service"))
                                                                .findFirst();

        assertThat(serviceEntryEvent).isPresent();

        logger.detachAppender(mockAppender);
    }

    @Test
    @DisplayName("Should log service exit message with duration")
    void shouldLogServiceExitMessageWithDuration() {
        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.addAppender(mockAppender);

        Instant startTime = Instant.now();
        Instant endTime = startTime.plus(5, ChronoUnit.MILLIS);

        when(clock.instant()).thenReturn(endTime);
        when(httpServletRequest.getAttribute(eq("requestStartTime"))).thenReturn(startTime.toEpochMilli());
        doNothing().when(httpServletRequest).removeAttribute("requestStartTime");
        when(httpServletResponse.getStatus()).thenReturn(HttpStatus.OK.value());

        serviceEntryInterceptor.afterCompletion(httpServletRequest, httpServletResponse, new Object(), null);

        verify(mockAppender, atLeast(1)).doAppend(loggingEventArgumentCaptor.capture());

        List<LoggingEvent> loggingEvents = loggingEventArgumentCaptor.getAllValues();

        Optional<LoggingEvent> serviceEntryEvent = loggingEvents.stream()
                                                                .filter(event -> event.getFormattedMessage().matches("Exiting service"))
                                                                .findFirst();

        assertThat(serviceEntryEvent).isPresent();
        assertThat(serviceEntryEvent.get().getMDCPropertyMap()).containsKey("duration")
                                                               .extractingByKey("duration").isEqualTo("5");
        assertThat(serviceEntryEvent.get().getMDCPropertyMap()).containsKey("httpStatusCode")
                                                               .extractingByKey("httpStatusCode").isEqualTo(String.valueOf(HttpStatus.OK.value()));

        logger.detachAppender(mockAppender);
    }

    @Test
    @DisplayName("Should log service exit message without duration when request start time is not present")
    void shouldLogServiceExitMessageWithoutDurationWhenRequestStartTimeIsNotPresent() {
        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.addAppender(mockAppender);

        when(httpServletRequest.getAttribute(eq("requestStartTime"))).thenReturn(null);
        when(httpServletResponse.getStatus()).thenReturn(HttpStatus.OK.value());

        serviceEntryInterceptor.afterCompletion(httpServletRequest, httpServletResponse, new Object(), null);

        verify(mockAppender, atLeast(1)).doAppend(loggingEventArgumentCaptor.capture());

        List<LoggingEvent> loggingEvents = loggingEventArgumentCaptor.getAllValues();

        Optional<LoggingEvent> serviceEntryEvent = loggingEvents.stream()
                                                                .filter(event -> event.getFormattedMessage().matches("Exiting service"))
                                                                .findFirst();

        assertThat(serviceEntryEvent).isPresent();
        assertThat(serviceEntryEvent.get().getMDCPropertyMap()).doesNotContainKey("duration");
        assertThat(serviceEntryEvent.get().getMDCPropertyMap()).containsKey("httpStatusCode")
                                                               .extractingByKey("httpStatusCode").isEqualTo(String.valueOf(HttpStatus.OK.value()));

        logger.detachAppender(mockAppender);
    }
}