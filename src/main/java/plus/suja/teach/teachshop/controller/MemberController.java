package plus.suja.teach.teachshop.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import plus.suja.teach.teachshop.entity.Member;
import plus.suja.teach.teachshop.entity.Session;
import plus.suja.teach.teachshop.enums.Status;
import plus.suja.teach.teachshop.service.MemberService;

import java.util.List;


@RestController
@RequestMapping("/api/v1/members")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @GetMapping
    public String getAllMembers(HttpServletResponse response) {
        return memberService.all(response);
    }

    @PostMapping
    public Member getMember(@RequestParam String username, @RequestParam String password, HttpServletResponse response) {
        memberService.checkoutUsernameAndPasswordForm(username, password);
        return memberService.login(username, password, response);
    }

    @PostMapping("/register")
    public Member createMember(@RequestParam String username, @RequestParam String password, HttpServletResponse response) {
        memberService.checkoutUsernameAndPasswordForm(username, password);
        return memberService.register(username, password, response);
    }


    @GetMapping("/online")
    public Session online(HttpServletResponse response) {
        return memberService.online(response);
    }

    @GetMapping("/offline")
    public void offline(HttpServletRequest request, HttpServletResponse response) {
        memberService.offline(request, response);
    }

    @GetMapping("/{id}")
    public Member getMember(@PathVariable Integer id) {
        return memberService.getMemberById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteMember(@PathVariable Integer id) {
        return memberService.deleteMember(id);
    }

    @GetMapping("/students")
    public List<Member> getStudents() {
        return memberService.students();
    }

    @PostMapping("/students")
    public Member createStudent(@RequestParam String username, @RequestParam String password, HttpServletResponse response) {
        memberService.checkoutUsernameAndPasswordForm(username, password);
        return memberService.createStudent(username, password, response);
    }

    @GetMapping("/students/{id}")
    public Member getStudent(@PathVariable("id") Integer id) {
        return memberService.getStudent(id);
    }

    @PutMapping("/students/{id}")
    public Member modStudent(@PathVariable Integer id,
                             @RequestParam String username,
                             @RequestParam Status status) {
        return memberService.modStudent(id, username, status);
    }

    @DeleteMapping("/students/{id}")
    public String deleteStudent(@PathVariable Integer id) {
        return memberService.deleteStudent(id);
    }

    @DeleteMapping("/clear/{id}")
    public String clearDeletedMember(@PathVariable Integer id) {
        return memberService.clearDeletedMember(id);
    }
}
