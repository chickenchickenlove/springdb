package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

/**
 * JDBC - Connection을 파라미터로 넘김.
 */

@Slf4j
public class MemberRepositoryV2 {

    private final DataSource dataSource;


    public MemberRepositoryV2(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values(?,?)";

        Connection conn = null;
        PreparedStatement pstmt = null;


        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());

            pstmt.executeUpdate();

            log.info("Connection = {}, class = {}", conn,conn.getClass());

            return member;
        } catch (Exception e) {
            log.error("error",e);
            throw e;
        }finally{
            close(conn, pstmt, null);
        }
    }

    public Member findById(Connection connection, String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";


        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Connection conn = connection;
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
            throw e;
        }finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(pstmt);
        }
    }

    public void update(Connection connection, String memberId, int money) throws SQLException {
        String sql = "update member set money = ? where member_id = ?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Connection conn = connection;
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);

            int resultSize = pstmt.executeUpdate();
            log.info("resultSize = {}", resultSize);
            log.info("Connection = {}, class = {}", conn,conn.getClass());
        } catch (SQLException e) {
            log.error("error", e);
            throw e;
        }finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(pstmt);
        }
    }

    public void delete(String memberId) throws SQLException {
        String sql = "delete from member where member_id=?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, memberId);

            int resultSize = pstmt.executeUpdate();
            log.info("resultSize = {}", resultSize);
            log.info("Connection = {}, class = {}", conn,conn.getClass());
        } catch (SQLException e) {
            log.error("error", e);
            throw e;
        }finally {
            close(conn, pstmt, rs);
        }

    }


    private void close(Connection connection, PreparedStatement pstmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(pstmt);
        JdbcUtils.closeConnection(connection);
    }
}

