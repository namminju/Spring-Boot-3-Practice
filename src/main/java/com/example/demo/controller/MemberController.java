package com.example.demo.controller;

import com.example.demo.domain.Member;
import com.example.demo.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
@Tag(name = "\uD83D\uDC65 Member API", description = "사용자 정보 확인")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(
            summary = "회원정보 조회",
            description = "JWT 토큰이 필요합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/token")
    public List<Member> findAll() {
        return memberService.findAll();
    }

    @GetMapping("/noToken")
    public List<Member> findAllMember() {
        return memberService.findAll();
    }
}
