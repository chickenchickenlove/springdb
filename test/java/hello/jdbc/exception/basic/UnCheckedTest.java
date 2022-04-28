package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class UnCheckedTest {


    @Test
    void unChecked_Catch() {
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void unChecked_Throw() {
        Service service = new Service();
        assertThatThrownBy(() -> service.callThrow()).isInstanceOf(MyUnCheckedException.class);
    }


    /**
     * RuntimeException 상속 예외 → 언체크 예외
     */
    static class MyUnCheckedException extends RuntimeException{
        public MyUnCheckedException(String message) {
            super(message);
        }
    }


    static class Service{
        Repository repository = new Repository();

        public void callCatch() {
            try {
                repository.call();
            } catch (MyUnCheckedException e) {
                log.info("예외 처리, message = {}", e.getMessage());
            }
        }

        public void callThrow() {
            repository.call();
        }
    }

    static class Repository{
        public void call() throws MyUnCheckedException {
            throw new MyUnCheckedException("ex");
        }
    }


}
