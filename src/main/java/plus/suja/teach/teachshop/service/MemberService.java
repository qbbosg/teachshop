package plus.suja.teach.teachshop.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import plus.suja.teach.teachshop.dao.MemberRepository;
import plus.suja.teach.teachshop.entity.Member;
import plus.suja.teach.teachshop.entity.Permission;
import plus.suja.teach.teachshop.entity.Role;
import plus.suja.teach.teachshop.exception.HttpException;

import java.util.stream.Collectors;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    public String login() {
        return "login";
    }

    public Member register(String username, String password) {
        Member member = new Member();
        member.setUsername(username);
        member.setEncryptPassword(BCrypt.withDefaults().hashToString(12, password.toCharArray()));
        try {
            memberRepository.save(member);
        } catch (Exception e) {
            throw new HttpException(400, "用户名已经存在");
        }
        return member;
    }

    public String all() {
        memberRepository.findAll().forEach(member -> {
            System.out.print("用户：");
            System.out.println(member.getUsername());
            System.out.print("身份：");
            System.out.println(member.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
            System.out.print("权限：");
            member.getRoles().forEach(role -> System.out.println(role.getPermissions().stream().map(Permission::getName).collect(Collectors.toList())));
            System.out.println("--------------");
        });
        return "all";
    }
}
