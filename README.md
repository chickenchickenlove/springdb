# Spring DB 접근 기술
이 리포지토리는 인프런 김영한님의 Spring DB 접근기술-1을 듣고 정리한 Repository입니다.

# 전반적인 흐름
- Jdbc
- DataSource / Connection Pool
- Transaction


## JDBC
- 어플리케이션은 DB와 통신을 하며 데이터를 CRUD를 한다. 세상에 DB는 수백가지가 존재하고, 각 DB에 접근 및 사용 방법이 달랐다. 예를 들어 H2 DB는 A라는 방식으로 사용할 수 있는데, MySQL은 B라는 방식으로 사용을 해야한다. 이것은 현재 어플리케이션 코드가 H2 DB를 타겟으로 짜여져 있을 때, MySQL DB로 바꾸게 되면 어플리케이션에서 DB와 관련된 모든 코드가 수정되어야 하는 것을 의미한다. 즉, OCP를 지키지 못하게 된다. OCP를 지킬 수 있도록 자바 진영에서는 이것을 추상화한 JDBC(Java DataBase Connection) 인터페이스를 제공한다. 

- 각 DB는 이제 JDBC 인터페이스의 구현체를 제공한다. 개발자가 어플리케이션을 개발할 때는 JDBC 인터페이스에 의존하면 된다. Connection / Statment / ResultSet을 다루는 방식이 JDBC로 추상화 되어 있기 때문에 편리하게 사용하기만 하면 된다. 

- JDBC는 SqlMapper(JdbcTemplate, MyBatis)와 ORM(JPA, Hiberante)를 지원한다.

- JDBC의 표준화 한계는 명확하다. 가장 일반적인 부분만 표준화 했기 때문에 DB의 세부적인 기능을 사용하게 되면, 다시 코드를 갈아엎어야 하는 상황이 발생한다. 예를 들어 페이징 기능을 사용한다고 가정하면, 각 DB가 제공하는 페이징 기능과 사용방법이 다르기 때문에 다시 한번 코드를 갈아엎어야 한다. 

- JDBC만 사용했을 때 발생할 수 있는 문제점
  1. JDBC는 일반적인 부분만 표준화 했다. 따라서 페이징 기능 등의 세부 기능을 사용할 때, 코드의 수정이 필요해진다.
  2. JDBC는 DriverManager를 통해 필요할 때 마다 Connection을 받아온다. 이 때, 매번 TCP/IP를 맺어야 한다.
  3. JDBC의 Resource Release 과정에서 많은 Try / Catch / If문이 남발된다. 즉, 동일한 코드가 계속 반복되는 사태가 발생한다. 


## DataSource
- 각 DB는 다른 방식으로 커넥션을 얻어야 했다. 따라서 OCP를 지키기 위해, 커넥션을 얻는 방법의 추상화가 필요하다. </br>
> DataSource는 각 DB의 Connection을 얻는 방식을 추상화했다. 어플리케이션은 DataSource 인터페이스에 의존만 하면 된다. 각 DB는 DataSource 구현체를 제공하고, 개발자는 이것을 사용하면 된다.


- DriverManager는 매번 Connection을 얻어올 때 마다 설정 정보(DB URL, USER, Password)를 넘겨줘야한다. 즉, 설정과 사용을 동시에 한다. 이 부분의 분리가 필요하다.
> DataSource는 초기에 단 한번 URL / USER / PASSWORD 정보를 넘긴다. 이후 datasource.getConnection()을 통해 커넥션을 가져오기만 하면 된다. 즉, 설정과 사용이 분리가 된다. 이것은 '설정'과 관련된 변경 포인트가 하나로 줄어드는 것을 의미한다. 


- DriverManager 사용 시, 자원을 Release 할 때 많은 Try / Catch / If문이 필요했다. 이 부분의 개선이 필요하다. </br>
> dataSource를 사용하면 JdbcUtils를 이용해 자원을 손쉽게 Release할 수 있다. JdbcUtils는 Try / Catch / If문의 추상화를 처리해준다. 


- DataSource의 한계
  1. DriverManagerDataSource를 사용하면, 매번 요청할 때 마다 새로운 커넥션을 받아온다. 이 때, TCP/IP 통신을 매번 하기 때문에 비용이 비싸다. 이 부분의 개선이 필요하다. 
</br>

## Connection Pool
- DriverManagerDataSource를 사용하면, 매번 커넥션을 만들어서 사용했기 때문에 비용이 비쌌다. 이 부분의 해결이 필요한데, 기본적으로는 DataSource 인터페이스를 충실히 구현한 무언가가 필요했다. 
> Connection Pool은 DataSource 인터페이스의 구현체다. 어플리케이션 실행 시점에 필요한만큼의 Connection을 생성해서 Pool에서 관리를 한다.


- Connection Pool은 다음과 같이 동작한다
  1. Connection을 요청하면, Connection Pool에서 관리중인 커넥션을 전달한다. 이 때, 프록시 커넥션 객체를 하나 만들고 이 객체로 실제 커넥션을 한번 감싼 다음에 전달해준다. 
  2. Connection을 요청했는데, 현재 가용할 수 있는 Connection이 없는 경우 Connection을 얻을 때까지 기다린다.
  3. Connection을 Close하면, Connection Pool에서 전달된 Connection은 자동으로 Connection Pool에 반환된다. 



## 22.04.12 
+ (강의) 커넥션 풀 이해
+ (강의) Data Source 이해
+ (강의) 트랜잭션의 개념 이해
+ (강의) 데이터베이스 연결 구조와 DB 세션 
+ (강의) 트랜잭션 - DB 예제1 - 개념 이해 
+ (강의) 트랜잭션 - DB 예제2 - 자동 커밋, 수동 커밋
+ (강의) 트랜잭션 - DB 예제3 - 트랜잭션 실습
+ (강의) 트랜잭션 - DB 예제4 - 트랜잭션 실습
+ (강의) 트랜잭션 - DB 락 - 개념이해
+ (강의) 트랜잭션 - DB 락 - 변경
+ (강의) 트랜잭션 - DB 락 - 조회


## 22.04.13
+ (강의) 트랜잭션 - 적용1
+ (강의) 트랜잭션 - 적용2
+ (강의) 문제점들
+ (강의) 트랜잭션 추상화
+ (강의) 트랜잭션 동기화
+ (강의) 트랜잭션 문제 해결 - 트랜잭션 매니저1


## 22.04.14
+ (강의) 트랜잭션 문제 해결 - 트랜잭션 매니저2
+ (강의) 트랜잭션 문제 해결 - 트랜잭션 템플릿
+ (강의) 트랜잭션 문제 해결 - 트랜잭션 AOP 이해, 적용, 정리
+ (정리) Spring DB : 커넥션 풀 (https://ojt90902.tistory.com/848)
+ (정리) Spring DB : 데이터 소스 (https://ojt90902.tistory.com/849)
+ (정리) Spring DB : 트랜잭션 개념(https://ojt90902.tistory.com/850)
+ (정리) Spring DB : 데이터베이스 연결 구조와 DB 세션(https://ojt90902.tistory.com/851)
+ (정리) Spring DB : DB 트랜잭션의 이해 (1) (https://ojt90902.tistory.com/852)
+ (정리) Spring DB : 자동 Commit / 수동 Commit (https://ojt90902.tistory.com/853)
+ (정리) Spring DB : 트랜잭션 실습 Commit (https://ojt90902.tistory.com/854)
+ (정리) Spring DB : 트랜잭션과 계좌이체 (https://ojt90902.tistory.com/855)
+ (정리) Spring DB : DB Lock 개념 (https://ojt90902.tistory.com/856)

## 22.04.15
+ (정리) Spring DB : 조회 시, DB Lock 설정  (https://ojt90902.tistory.com/858)


## 22.04.23
+ (강의) JDBC 이해 / JDBC와 최신 데이터 접근 기술 / DB 연결 / JDBC CRUD 개발
+ (강의) DataSource 예제 / 커넥션 풀 / DataSource 적용 
+ (정리) Spring DB : JDBC / ORM의 필요성 정리(https://ojt90902.tistory.com/878)
+ (정리) Spring DB : 데이터베이스 연결 (https://ojt90902.tistory.com/879)
+ (정리) Spring DB : JDBC 개발 (https://ojt90902.tistory.com/880)

## 22.04.25
+ (정리) Spring DB : JDBC 개발 / 조회 / 수정 / 삭제 (https://ojt90902.tistory.com/880)
+ (정리) Spring DB : DataSource / Connection Pool 설정 및 적용(https://ojt90902.tistory.com/882)

## 22.04.26
+ (정리) Spring DB : JDBC 트랜잭션  (https://ojt90902.tistory.com/860)
+ (정리) Spring DB : JDBC 트랜잭션 문제점 + 트랜잭션 추상화  (https://ojt90902.tistory.com/861)
+ (강의) 자바 예외 이해 (예외 계층 / 예외 기본 규칙 / 체크 예외 기본 이해 / 언체크 예외 기본 이해 / 체크 예외 활용 / 언체크 예외 활용 / 예외 포함과 스택 트레이스)

