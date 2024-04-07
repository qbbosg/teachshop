package plus.suja.teach.teachshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import plus.suja.teach.teachshop.entity.Member;
import plus.suja.teach.teachshop.service.MemberService;

import java.util.Map;
import java.util.UUID;

@RestController
public class RegisterController {
    @Autowired
    private MemberService memberService;

    @PostMapping("/register")
    public String register (@RequestBody Map<String, Object> params) {
        Member member = new Member();
        member.setUsername((String) params.get("username"));
        member.setEncryptPassword(UUID.randomUUID().toString());
        return memberService.register(member);
    }
}
