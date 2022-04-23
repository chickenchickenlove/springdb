package hello.jdbc.connection;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
public class DBConnectionUtil {

    // JDBC 표준 인터페이스 제공 커넥션
    public static Connection getConnection() {
        try {
            // 여기서 나오는 Connection은 인터페이스다.
            // 여기서 각 DB는 각 DB에 대한 Connection 구현체를 제공한다.
            // 이 때, org.h2.jdbc.JdbcConnection을 제공해줌.

            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            log.info("get Connection = {}, class = {}", conn, conn.getClass());
            return conn;
        } catch (SQLException e) {
            // Check 예외를 런타입 예외로 바꿔서 던진다.
            // 추후 설명함.
            throw new IllegalStateException(e);
        }
    }

}
