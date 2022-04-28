package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


@Slf4j
public class UnCheckedAppTest {


    @Test
    void checked() {
        Controller controller = new Controller();
        Assertions.assertThatThrownBy(() -> controller.request()).isInstanceOf(Exception.class);
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
            throw new RuntimeSQLException("ex");
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
    }


}
