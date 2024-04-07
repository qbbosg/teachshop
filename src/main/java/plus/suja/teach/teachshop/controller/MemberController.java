package plus.suja.teach.teachshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
