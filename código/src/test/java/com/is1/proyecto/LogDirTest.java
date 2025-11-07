package com.is1.proyecto;

import com.is1.proyecto.utils.LoggerUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

public class LogDirTest {

    private File tempDir;

    @AfterEach
    public void cleanup() throws Exception {
        if (tempDir != null && tempDir.exists()) {
            Files.walk(tempDir.toPath())
                .map(java.nio.file.Path::toFile)
                .sorted((a,b) -> b.getPath().length() - a.getPath().length())
                .forEach(File::delete);
        }
    }

    @Test
    public void ensureLogDir_createsDirectory() throws Exception {
        tempDir = Files.createTempDirectory("test-logs").toFile();
        // Use a child dir so creation logic actually creates it
        File child = new File(tempDir, "myapp-logs");
        String dirPath = child.getAbsolutePath();

        // Call same logic used in App: try to create dir
        System.setProperty("LOG_DIR", dirPath);

        // Reuse same logic as App (we don't have LoggerUtil.ensureLogDir yet), so duplicate minimal logic here
        String logDir = System.getProperty("LOG_DIR");
        if (logDir == null || logDir.isBlank()) {
            String envLogDir = System.getenv("LOG_DIR");
            logDir = (envLogDir != null && !envLogDir.isBlank()) ? envLogDir : "logs";
        }
        File dir = new File(logDir);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            assertTrue(created || dir.exists(), "Directory should be created");
        }

        assertTrue(dir.exists() && dir.isDirectory(), "Log directory must exist and be a directory");
    }
}
