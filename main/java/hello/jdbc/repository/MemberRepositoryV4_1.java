package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.ex.MyDbException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

/**
 * 예외 누수 문제 해결
 * 1. 체크 예외 → 런타임 예외로 변경
 * 2. MemberRepository 인터페이스 사용
 * 3. Throws SQLException 제거
 */

@Slf4j
public class MemberRepositoryV4_1 implements MemberRepository{

    private final DataSource dataSource;

    public MemberRepositoryV4_1(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Member save(Member member) {
        String sql = "insert into member(member_id, money) values(?,?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // 트랜잭션 매니저 사용
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());

            pstmt.executeUpdate();

            log.info("Connection = {}, class = {}", conn,conn.getClass());

            return member;
        } catch (Exception e) {
            log.error("error",e);
            throw new MyDbException(e);
        }finally{
            close(conn, pstmt, null);
        }
    }

    @Override
    public Member findById(String memberId){
        String sql = "select * from member where member_id = ?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            Connection conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, memberId);

            rs = pstmt.executeQuery();
            log.info("Connection = {}, class = {}", conn,conn.getClass());

            if (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            }else{
                throw new NoSuchElementException("member not found memberId = " + memberId);
            }
        } catch (SQLException e) {
            log.error("error", e);
            throw new MyDbException(e);
        }finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(pstmt);
        }
    }

    @Override
    public void update(String memberId, int money){
        String sql = "update member set money = ? where member_id = ?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // 트랜잭션 매니저 사용
            Connection conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);

            int resultSize = pstmt.executeUpdate();
            log.info("resultSize = {}", resultSize);
            log.info("Connection = {}, class = {}", conn,conn.getClass());
        } catch (SQLException e) {
            log.error("error", e);
            throw new MyDbException(e);
        }finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(pstmt);
        }
    }

    @Override
    public void delete(String memberId){
        String sql = "delete from member where member_id=?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // 트랜잭션 매니저 사용
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, memberId);

            int resultSize = pstmt.executeUpdate();
            log.info("resultSize = {}", resultSize);
            log.info("Connection = {}, class = {}", conn,conn.getClass());
        } catch (SQLException e) {
            log.error("error", e);
            throw new MyDbException(e);
        }finally {
            close(conn, pstmt, rs);
        }

    }

    // 새로 추가된 코드
    private Connection getConnection() {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        log.info("get connection={}, class = {}", connection, connection.getClass());
        return connection;
    }

    private void close(Connection connection, PreparedStatement pstmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(pstmt);
        DataSourceUtils.releaseConnection(connection, dataSource);
    }
}

