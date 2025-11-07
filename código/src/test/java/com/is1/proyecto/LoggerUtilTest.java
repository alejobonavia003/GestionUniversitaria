package com.is1.proyecto;

import com.is1.proyecto.utils.LoggerUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class LoggerUtilTest {

    @Test
    public void getLogger_returnsNonNullLogger() {
        Logger logger = LoggerUtil.getLogger(LoggerUtilTest.class);
        assertNotNull(logger, "Logger should not be null");
    }

    @Test
    public void getLogger_multipleCallsReturnLogger() {
        Logger a = LoggerUtil.getLogger(LoggerUtilTest.class);
        Logger b = LoggerUtil.getLogger(String.class);
        assertNotNull(a);
        assertNotNull(b);
        // different classes may return different logger instances, but must not throw
    }
}
