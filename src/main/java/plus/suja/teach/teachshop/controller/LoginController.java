package plus.suja.teach.teachshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import plus.suja.teach.teachshop.service.MemberService;

import java.util.Map;

@RestController
public class LoginController {
    @Autowired
    private MemberService memberService;

    @PostMapping("/login")
    public String login(@RequestBody Map<String, Object> params) {
        System.out.println(params);
        return memberService.login();
    }
}
