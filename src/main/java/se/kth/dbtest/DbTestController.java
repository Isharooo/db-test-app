package se.kth.dbtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DbTestController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/")
    public Map<String, Object> testConnection() {
        Map<String, Object> result = new HashMap<>();
        result.put("timestamp", System.currentTimeMillis());
        
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            result.put("status", "SUCCESS");
            result.put("database", meta.getDatabaseProductName());
            result.put("version", meta.getDatabaseProductVersion());
            result.put("url", meta.getURL());
            result.put("user", meta.getUserName());
        } catch (Exception e) {
            result.put("status", "FAILED");
            result.put("error", e.getMessage());
            result.put("errorClass", e.getClass().getSimpleName());
        }
        
        return result;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> result = new HashMap<>();
        result.put("status", "UP");
        return result;
    }
}
