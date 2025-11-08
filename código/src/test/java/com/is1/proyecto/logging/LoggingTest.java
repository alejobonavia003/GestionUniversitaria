package com.is1.proyecto.logging;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import com.is1.proyecto.utils.LoggerUtil;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class LoggingTest {
    private static final Logger logger = LoggerUtil.getLogger(LoggingTest.class);
    private static final String LOGS_DIR = "logs";
    private static final String[] LOG_FILES = {
        "debug.log",
        "info.log",
        "warn.log",
        "error.log"
    };

    @BeforeAll
    static void setUp() {
        // Asegurar que existe el directorio de logs
        new File(LOGS_DIR).mkdirs();
        // Limpiar logs anteriores
        for (String logFile : LOG_FILES) {
            new File(LOGS_DIR + File.separator + logFile).delete();
        }
    }

    @Test
    void testLogLevelsAreSeparated() throws Exception {
        // Generar mensajes de log de diferentes niveles
        String debugMsg = "Test debug message " + System.currentTimeMillis();
        String infoMsg = "Test info message " + System.currentTimeMillis();
        String warnMsg = "Test warn message " + System.currentTimeMillis();
        String errorMsg = "Test error message " + System.currentTimeMillis();

        logger.debug(debugMsg);
        logger.info(infoMsg);
        logger.warn(warnMsg);
        logger.error(errorMsg);

        // Dar tiempo a que los logs se escriban
        Thread.sleep(100);

        // Verificar que cada mensaje está en su archivo correspondiente
        assertLogFileContains("debug.log", debugMsg);
        assertLogFileContains("info.log", infoMsg);
        assertLogFileContains("warn.log", warnMsg);
        assertLogFileContains("error.log", errorMsg);

        // Verificar que los mensajes NO están en los archivos incorrectos
        assertLogFileDoesNotContain("info.log", debugMsg);
        assertLogFileDoesNotContain("warn.log", infoMsg);
        assertLogFileDoesNotContain("error.log", warnMsg);
        assertLogFileDoesNotContain("debug.log", errorMsg);
    }

    @Test
    void testLogDirectoryExists() {
        File logDir = new File(LOGS_DIR);
        Assertions.assertTrue(logDir.exists(), "El directorio de logs debe existir");
        Assertions.assertTrue(logDir.isDirectory(), "logs debe ser un directorio");
    }

    @Test
    void testLoggerUtilCreation() {
        Logger logger = LoggerUtil.getLogger(LoggingTest.class);
        Assertions.assertNotNull(logger, "El logger no debe ser null");
        Assertions.assertEquals(
            "com.is1.proyecto.logging.LoggingTest",
            logger.getName(),
            "El nombre del logger debe coincidir con la clase"
        );
    }

    @Test
    void testExceptionLogging() throws Exception {
        String errorMessage = "Test exception " + System.currentTimeMillis();
        Exception testException = new RuntimeException(errorMessage);
        
        logger.error("Error occurred", testException);
        
        Thread.sleep(100);
        
        // Verificar que el error y el stack trace están en el archivo de error
        String errorLog = Files.readString(Path.of(LOGS_DIR, "error.log"));
        Assertions.assertTrue(
            errorLog.contains(errorMessage),
            "El mensaje de error debe estar en el archivo de error"
        );
        Assertions.assertTrue(
            errorLog.contains("RuntimeException"),
            "El tipo de excepción debe estar en el archivo de error"
        );
    }

    private void assertLogFileContains(String fileName, String message) throws Exception {
        Path logFile = Path.of(LOGS_DIR, fileName);
        Assertions.assertTrue(
            Files.exists(logFile),
            "El archivo " + fileName + " debe existir"
        );
        
        List<String> lines = Files.readAllLines(logFile);
        String content = String.join("\\n", lines);
        Assertions.assertTrue(
            content.contains(message),
            "El mensaje '" + message + "' debe estar en " + fileName
        );
    }

    private void assertLogFileDoesNotContain(String fileName, String message) throws Exception {
        Path logFile = Path.of(LOGS_DIR, fileName);
        if (Files.exists(logFile)) {
            List<String> lines = Files.readAllLines(logFile);
            String content = String.join("\\n", lines);
            Assertions.assertFalse(
                content.contains(message),
                "El mensaje '" + message + "' NO debe estar en " + fileName
            );
        }
    }

    @AfterAll
    static void tearDown() {
        // Opcional: limpiar los archivos de log después de las pruebas
        // for (String logFile : LOG_FILES) {
        //     new File(LOGS_DIR + File.separator + logFile).delete();
        // }
    }
}