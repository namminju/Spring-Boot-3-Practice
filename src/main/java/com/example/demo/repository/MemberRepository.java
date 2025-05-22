package com.example.demo.repository;

import com.example.demo.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    //    JpaRepository = CRUD 기능 자동 제공
    //    MemberRepository = "나 Member 관리할게" 약속만 선언 → Spring 이 알아서 구현
    Optional<Member> findByAccountUsername(String username);
}
