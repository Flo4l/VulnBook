package de.albers.vulnbook;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

@Service
public class DatabaseService {

    private static Properties properties;
    private static HikariDataSource dataSource;

    static {
        try {
            properties = new Properties();
            properties.load(new FileInputStream("src/main/resources/application.properties"));

            dataSource = new HikariDataSource();
            dataSource.setDriverClassName(properties.getProperty("spring.datasource.driver-class-name"));
            dataSource.setJdbcUrl(properties.getProperty("spring.datasource.url"));
            dataSource.setUsername(properties.getProperty("spring.datasource.username"));
            dataSource.setPassword(properties.getProperty("spring.datasource.password"));

            dataSource.setMinimumIdle(3);
            dataSource.setMaximumPoolSize(10);
            dataSource.setAutoCommit(true);
            dataSource.setLoginTimeout(3);

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static DataSource getDataSource() {
        return dataSource;
    }
}
