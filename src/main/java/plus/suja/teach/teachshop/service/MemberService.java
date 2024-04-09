package plus.suja.teach.teachshop.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import plus.suja.teach.teachshop.annotation.Admin;
import plus.suja.teach.teachshop.annotation.Teacher;
import plus.suja.teach.teachshop.dao.MemberRepository;
import plus.suja.teach.teachshop.dao.SessionDao;
import plus.suja.teach.teachshop.entity.Member;
import plus.suja.teach.teachshop.entity.Permission;
import plus.suja.teach.teachshop.entity.Role;
import plus.suja.teach.teachshop.entity.Session;
import plus.suja.teach.teachshop.exception.HttpException;
import plus.suja.teach.teachshop.util.HttpRequestUtil;
import plus.suja.teach.teachshop.util.UserContextUtil;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static plus.suja.teach.teachshop.config.HttpInterceptor.SESSION_ID;

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

        response.setStatus(200);
        return member;
    }

    public Member register(String username, String password, HttpServletResponse response) {
        Member member = new Member();
        member.setUsername(username);
        member.setEncryptPassword(BCrypt.withDefaults().hashToString(12, password.toCharArray()));
        try {
            memberRepository.save(member);
        } catch (DataIntegrityViolationException e) {
            throw new HttpException(409, "用户名已经存在");
        } catch (Exception e) {
            throw new RuntimeException();
        }
        response.setStatus(201);
        return member;
    }

    @Admin
    public String all(HttpServletResponse response) {
        memberRepository.findAll().forEach(member -> {
            System.out.print("用户：");
            System.out.println(member.getUsername());
            System.out.print("身份：");
            System.out.println(member.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
            System.out.print("权限：");
            member.getRoles().forEach(role -> System.out.println(role.getPermissions().stream().map(Permission::getName).collect(Collectors.toList())));
            System.out.println("--------------");
        });
        response.setStatus(200);
        return "all";
    }

    public Session online(HttpServletResponse response) {
        Member currentUser = UserContextUtil.getCurrentUser();
        if (currentUser == null) {
            throw new HttpException(401, "Unauthorized");
        } else {
            Session session = new Session();
            session.setMember(currentUser);

            response.setStatus(200);
            return session;
        }
    }

    @Transactional
    public void offline(HttpServletRequest request, HttpServletResponse response) {
        HttpRequestUtil.getCookie(request).ifPresent(sessionDao::deleteByCookie);

        Cookie cookie = new Cookie(SESSION_ID, "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        response.setStatus(204);
    }

    @Teacher
    public Member studentInfo(Integer id, HttpServletResponse response) {
        Optional<Member> member = memberRepository.findById(id);
        if (member.isPresent()) {
            return member.get();
        } else {
            throw new HttpException(404, "Not find");
        }
    }
}
