package plus.suja.teach.teachshop.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import plus.suja.teach.teachshop.dao.MemberRepository;
import plus.suja.teach.teachshop.dao.SessionDao;
import plus.suja.teach.teachshop.entity.Member;
import plus.suja.teach.teachshop.entity.Permission;
import plus.suja.teach.teachshop.entity.Role;
import plus.suja.teach.teachshop.entity.Session;
import plus.suja.teach.teachshop.exception.HttpException;

import java.util.UUID;
import java.util.stream.Collectors;

import static plus.suja.teach.teachshop.config.Interceptor.MemberInterceptor.SESSION_ID;

@Service
public class MemberService {
    @Autowired
    private SessionDao sessionDao;
    @Autowired
    private MemberRepository memberRepository;

    public Member login(String username, String password, HttpServletResponse response) {
        Member member = memberRepository.findByUsername(username);
        if (member == null) {
            throw new HttpException(401, "用户不存在");
        }
        BCrypt.Result verify = BCrypt.verifyer().verify(password.toCharArray(), member.getEncryptPassword());
        if (verify.verified == false) {
            throw new HttpException(401, "密码错误");
        }

        Session session = new Session();
        session.setMember(member);
        session.setCookie(UUID.randomUUID().toString());
        sessionDao.save(session);
        response.addCookie(new Cookie(SESSION_ID, session.getCookie()));
        return member;
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
