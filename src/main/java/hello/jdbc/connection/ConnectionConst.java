package hello.jdbc.connection;

// 상수형 커넥션 만들기
// 따라서 추상 클래스로 만듦.
public abstract class ConnectionConst {

    // DB 정보
    public static final String URL = "jdbc:h2:tcp://localhost/~/ansang1";
    public static final String USERNAME = "sa";
    public static final String PASSWORD = "1";
}
