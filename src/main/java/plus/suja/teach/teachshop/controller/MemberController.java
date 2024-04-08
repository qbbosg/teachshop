package plus.suja.teach.teachshop.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import plus.suja.teach.teachshop.entity.Member;
import plus.suja.teach.teachshop.exception.HttpException;
import plus.suja.teach.teachshop.service.MemberService;


@RestController
@RequestMapping("/api/v1/members")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @GetMapping
    public String getAllMembers() {
        return memberService.all();
    }

    @PostMapping
    public Member getMember(@RequestParam String username, @RequestParam String password, HttpServletResponse response) {
        if (!StringUtils.hasLength(username) || username.length() < 6 || username.length() > 20) {
            throw new HttpException(401, "用户名必须6-20之间");
        }
        if (!StringUtils.hasLength(password) || password.length() < 6 || password.length() > 20) {
            throw new HttpException(401, "密码必须6-20之间");
        }
        response.setStatus(200);
        return memberService.login(username, password, response);
    }

    @PostMapping("/register")
    public Member createMember(@RequestParam String username, @RequestParam String password, HttpServletResponse response) {
        if (!StringUtils.hasLength(username) || username.length() < 6 || username.length() > 20) {
            throw new HttpException(401, "用户名必须6-20之间");
        }
        if (!StringUtils.hasLength(password) || password.length() < 6 || password.length() > 20) {
            throw new HttpException(401, "密码必须6-20之间");
        }
        response.setStatus(201);
        return memberService.register(username, password);
    }
}
