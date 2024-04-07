package plus.suja.teach.teachshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import plus.suja.teach.teachshop.dao.MemberRepository;
import plus.suja.teach.teachshop.entity.Member;
import plus.suja.teach.teachshop.entity.Role;

import java.util.stream.Collectors;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    public String login() {
        return "login";
    }

    public String register(Member member) {
        memberRepository.save(member);
        return "register";
    }

    public String all() {
        memberRepository.findAll().forEach(member -> {
            System.out.print("用户：");
            System.out.println(member.getUsername());
            System.out.print("身份：");
            System.out.println(member.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
            System.out.println("--------------");
        });
        return "all";
    }
}
