package study.datajpa.repository;

import study.datajpa.entity.Member;

import java.util.List;

//-- 21강 사용자 정의 리포지토리 구현
public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}
