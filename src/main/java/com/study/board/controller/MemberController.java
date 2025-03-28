package com.study.board.controller;

import com.study.board.dto.MemberDTO;
import com.study.board.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member/save")
    public String saveForm() {
        return "save";
    }

    @PostMapping("/member/save")
    public String save(@ModelAttribute MemberDTO memberDTO) {
        System.out.println("memberDTO = " + memberDTO);
        memberService.save(memberDTO);
        return "login"; // 또는 회원가입 완료 페이지
    }

    @GetMapping("/member/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/member/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null) {
            // 로그인 성공, 이름을 세션에 저장
            //session.setAttribute("loginEmail", loginResult.getMemberEmail());
            session.setAttribute("loginName", loginResult.getMemberName());  // 이름 추가
            return "redirect:/board/list";
        } else {
            // 로그인 실패
            return "login";
        }
    }
}
