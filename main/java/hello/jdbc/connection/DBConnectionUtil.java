package hello.jdbc.connection;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.PASSWORD;

@Slf4j
public class DBConnectionUtil {

    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(ConnectionConst.URL, ConnectionConst.USERNAME, PASSWORD);
            log.info("Connection = {}, class = {}", conn, conn.getClass());
            return conn;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

}
