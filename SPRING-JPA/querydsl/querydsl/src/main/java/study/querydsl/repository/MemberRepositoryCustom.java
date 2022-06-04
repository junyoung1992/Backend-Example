package study.querydsl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;

import java.util.List;

public interface MemberRepositoryCustom {

    List<MemberTeamDto> search(MemberSearchCondition condition);

    @Deprecated
    Page<MemberTeamDto> searchPageSimple(MemberSearchCondition condition, Pageable pageable);

    /**
     * fetchResult(), fetchCount() 가 deprecated 되면서 코드 수정<br>
     * 시작페이지면서 컨텐츠 사이즈가 페이지 사이즈보다 작을 때나 마지막 페이지일 때는 count 쿼리를 생략함
     */
    Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition, Pageable pageable);

}
