package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MemberRepositoryV0Test {

    private MemberRepositoryV0 repository = new MemberRepositoryV0();

    @Test
    void crud() throws SQLException {
        String memberId = "member5";

        Member member = new Member(memberId, 10000);
        repository.save(member);

        //findById
        Member findMember = repository.findById(member.getMemberId());
        log.info("findMember = {}", findMember);

        // 주소비교 + 해쉬비교
        log.info("member == findMember : {}", findMember == member);
        log.info("member equals findMember : {}", member.equals(findMember));
        assertThat(findMember).isEqualTo(member);

        // update : money 10000 -> 20000
        repository.update(memberId, 20000);
        Member updateMember = repository.findById(memberId);
        assertThat(updateMember.getMoney()).isEqualTo(20000);

        // delete
        repository.delete(member.getMemberId());
        assertThatThrownBy(() -> repository.findById(memberId)).isInstanceOf(NoSuchElementException.class);
    }
}