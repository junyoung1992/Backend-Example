package hello.core.member;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 테스트용 MemoryMemberRepository
 */
@Component
public class MemoryMemberRepository implements MemberRepository{

    // 동시성 이슈가 있어 ConcurrentHashMap 등을 사용해야 하지만... 교육이니까
    private  static Map<Long, Member> store = new HashMap<>();

    @Override
    public void save(Member member) {
        store.put(member.getId(), member);
    }

    @Override
    public Member findById(Long memberId) {
        return store.get(memberId);
    }

}
