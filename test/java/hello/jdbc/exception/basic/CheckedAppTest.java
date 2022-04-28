package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class CheckedAppTest {


    @Test
    void checked() {
        Controller controller = new Controller();
        Assertions.assertThatThrownBy(() -> controller.request()).isInstanceOf(Exception.class);
    }

    static class Controller{
        Service service = new Service();

        public void request() throws SQLException, ConnectException {
            service.call();
        }

    }

    static class Service{

        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void call() throws SQLException, ConnectException {
            repository.call();
            networkClient.call();
        }
    }

    static class Repository{
        public void call() throws SQLException {
            throw new SQLException();
        }
    }

    static class NetworkClient{
        public void call() throws ConnectException {
            throw new ConnectException("ex");
        }
    }

    static class ConnectException extends Exception{
        public ConnectException(String message) {
            super(message);
        }
    }

}
