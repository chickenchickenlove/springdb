package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;


@Slf4j
public class StackTraceTest {


    @Test
    void checked() {
        Controller controller = new Controller();
        Assertions.assertThatThrownBy(() -> controller.request()).isInstanceOf(Exception.class);
    }

    @Test
    void printEx() {
        Controller controller = new Controller();

        try {
            controller.request();
        } catch (Exception e) {
            log.info("ex",e);
        }

    }

    static class Controller{
        Service service = new Service();

        public void request(){
            service.call();
        }

    }

    static class Service{

        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void call(){
            repository.call();
            networkClient.call();
        }
    }

    static class Repository{
        public void call() {
            try {
                runSQL();
            }catch (SQLException e){
                // 예외를 포함한다 → StackTrace 정상 출력됨.
                throw new RuntimeSQLException();
            }
        }

        private void runSQL() throws SQLException {
            throw new SQLException("ex");
        }
    }

    static class NetworkClient{
        public void call() {
            throw new RuntimeConnectException("ex");
        }
    }

    static class RuntimeConnectException extends RuntimeException{
        public RuntimeConnectException(String message) {
            super(message);
        }
    }

    static class RuntimeSQLException extends RuntimeException{
        public RuntimeSQLException(String message) {
            super(message);
        }

        public RuntimeSQLException(String message, Throwable cause) {
            super(message, cause);
        }

        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }

        public RuntimeSQLException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }

        public RuntimeSQLException() {
        }
    }


}
