package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * JDBC - DriverManager 사용
 */

@Slf4j
public class MemberRepositoryV0 {

    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values(?,?)";

        Connection conn = null;
        PreparedStatement pstmt = null;


        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());

            pstmt.executeUpdate();
            return member;
        } catch (Exception e) {
            log.error("error",e);
            throw e;
        }finally{
            close(conn, pstmt, null);
        }
    }




    private Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }

    private void close(Connection connection, PreparedStatement pstmt, ResultSet rs) {

        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                log.info("error",e);
            }
        }

        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (Exception e) {
                log.info("error",e);
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                log.info("error",e);
            }
        }
    }

}
