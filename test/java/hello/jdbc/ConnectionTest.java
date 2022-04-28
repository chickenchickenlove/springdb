package hello.jdbc;

import com.zaxxer.hikari.HikariDataSource;
import hello.jdbc.connection.ConnectionConst;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;
import static java.lang.Thread.*;

@Slf4j
public class ConnectionTest {

    @Test
    void driverManager() throws SQLException {
        Connection conn1 = DriverManager.getConnection(URL,USERNAME, PASSWORD);
        Connection conn2 = DriverManager.getConnection(URL,USERNAME, PASSWORD);
        Connection conn3 = DriverManager.getConnection(URL,USERNAME, PASSWORD);
        Connection conn4 = DriverManager.getConnection(URL,USERNAME, PASSWORD);
        log.info("Connection = {}, class = {}", conn1, conn1.getClass());
        log.info("Connection = {}, class = {}", conn2, conn2.getClass());
        log.info("Connection = {}, class = {}", conn3, conn3.getClass());
        log.info("Connection = {}, class = {}", conn4, conn4.getClass());
    }

    @Test
    void driverManagerDataSource() throws SQLException {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        useDataSource(dataSource);
    }

    @Test
    void dataSourceConnectionPool() throws SQLException, InterruptedException {

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10);
        dataSource.setPoolName("MyPool");

        useDataSource(dataSource);
        sleep(2000);
    }



    private void useDataSource(DataSource dataSource) throws SQLException {
        Connection conn1 = dataSource.getConnection();
        Connection conn2 = dataSource.getConnection();
        Connection conn3 = dataSource.getConnection();
        Connection conn4 = dataSource.getConnection();
        log.info("Connection = {}, class = {}", conn1, conn1.getClass());
        log.info("Connection = {}, class = {}", conn2, conn2.getClass());
        log.info("Connection = {}, class = {}", conn3, conn3.getClass());
        log.info("Connection = {}, class = {}", conn4, conn4.getClass());
    }


}
