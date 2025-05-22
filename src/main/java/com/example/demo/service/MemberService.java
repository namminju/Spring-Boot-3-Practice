package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.repository.MemberRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member save(String name) {
        Member member = new Member();
        member.setName(name);
        return memberRepository.save(member);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }
}
