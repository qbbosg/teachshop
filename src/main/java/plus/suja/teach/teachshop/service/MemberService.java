package plus.suja.teach.teachshop.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import plus.suja.teach.teachshop.annotation.Admin;
import plus.suja.teach.teachshop.annotation.Teacher;
import plus.suja.teach.teachshop.dao.MemberRepository;
import plus.suja.teach.teachshop.dao.RoleRepository;
import plus.suja.teach.teachshop.dao.SessionRepository;
import plus.suja.teach.teachshop.entity.Member;
import plus.suja.teach.teachshop.entity.Role;
import plus.suja.teach.teachshop.entity.Session;
import plus.suja.teach.teachshop.enums.Status;
import plus.suja.teach.teachshop.exception.HttpException;
import plus.suja.teach.teachshop.util.HttpRequestUtil;
import plus.suja.teach.teachshop.util.UserContextUtil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static plus.suja.teach.teachshop.config.HttpInterceptor.SESSION_ID;

@Service
public class MemberService {
    private SessionRepository sessionRepository;
    private MemberRepository memberRepository;
    private RoleRepository roleRepository;

    @Autowired
    public MemberService(SessionRepository sessionRepository, MemberRepository memberRepository, RoleRepository roleRepository) {
        this.sessionRepository = sessionRepository;
        this.memberRepository = memberRepository;
        this.roleRepository = roleRepository;
    }

    public Member login(String username, String password, HttpServletResponse response) {
        Member member = memberRepository.findByUsername(username);
        if (member == null) {
            throw new HttpException(401, "用户不存在");
        }
        BCrypt.Result verify = BCrypt.verifyer().verify(password.toCharArray(), member.getEncryptPassword());
        if (!verify.verified) {
            throw new HttpException(401, "密码错误");
        }

        Session session = new Session();
        session.setMember(member);
        session.setCookie(UUID.randomUUID().toString());
        sessionRepository.save(session);
        response.addCookie(new Cookie(SESSION_ID, session.getCookie()));

        response.setStatus(200);
        return member;
    }

    public Member register(String username, String password, HttpServletResponse response) {
        return registerAndSetRole(username, password, "student", response);
    }

    public Member registerStudentType(String username, String password, HttpServletResponse response) {
        return registerAndSetRole(username, password, "student", response);
    }

    public Member registerTeacherType(String username, String password, HttpServletResponse response) {
        return registerAndSetRole(username, password, "teacher", response);
    }

    public Member registerAndSetRole(String username, String password, String role, HttpServletResponse response) {
        Member member = new Member();
        member.setUsername(username);
        member.setEncryptPassword(BCrypt.withDefaults().hashToString(12, password.toCharArray()));
        member.setRoles(List.of(getRole(role)));
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

    private Role getRole(String roleName) {
        List<Role> roles = (List<Role>) roleRepository.findAll();
        Optional<Role> role = roles.stream().filter(r -> roleName.equals(r.getName())).findFirst();
        if (role.isPresent()) {
            return role.get();
        } else {
            throw new HttpException(404, "没找到 roleName");
        }
    }

    @Admin
    public String all(HttpServletResponse response) {
        memberRepository.findAll().forEach(member -> {
            System.out.print("用户：");
            System.out.println(member.getUsername());
            System.out.print("身份：");
            System.out.println(member.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
//            System.out.print("权限：");
//            member.getRoles().forEach(role -> System.out.println(role.getPermissions().stream().map(Permission::getName).collect(Collectors.toList())));
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
        HttpRequestUtil.getCookie(request).ifPresent(sessionRepository::deleteByCookie);

        Cookie cookie = new Cookie(SESSION_ID, "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        response.setStatus(204);
    }

    public Member getMemberById(Integer id) {
        Optional<Member> member = memberRepository.findById(id);
        if (member.isPresent()) {
            return member.get();
        } else {
            throw new HttpException(404, "Not find");
        }
    }

    private Member getMemberByIdAndHavaRole(Integer id, String roleName) {
        Member member = getMemberById(id);
        if (member.getRoles().stream().anyMatch(r -> roleName.contains(r.getName()))) {
            return member;
        } else {
            throw new HttpException(404, "Not find");
        }
    }

    @Teacher
    public List<Member> students() {
        return memberRepository.findAllByRoleName("student");
    }

    public Member getStudent(Integer id) {
        return getMemberByIdAndHavaRole(id, "student");
    }

    @Teacher
    public Member modStudent(Integer id, String username, Status status) {
        Member student = getStudent(id);
        student.setUsername(username);
        student.setStatus(status);
        memberRepository.save(student);
        return student;
    }

    @Teacher
    public String deleteStudent(Integer id) {
        Member student = getStudent(id);
        student.setStatus(Status.NO);
        memberRepository.save(student);
        return "delete success";
    }


    @Admin
    public String deleteMember(Integer id) {
        Member member = getMemberById(id);
        member.setStatus(Status.NO);
        memberRepository.save(member);
        return "delete success";
    }

    @Admin
    public String clearDeletedMember(Integer id) {
        Member member = getMemberById(id);
        if (member.getStatus().equals(Status.NO)) {
            memberRepository.deleteById(id);
            return "success";
        } else {
            throw new HttpException(409, "不能清理未被删除的学生");
        }
    }

    @Teacher
    public Member createStudent(String username, String password, HttpServletResponse response) {
        return registerStudentType(username, password, response);
    }

    public void checkoutUsernameAndPasswordForm(String username, String password) {
        if (!StringUtils.hasLength(username) || username.length() < 6 || username.length() > 20) {
            throw new HttpException(401, "用户名必须6-20之间");
        }
        if (!StringUtils.hasLength(password) || password.length() < 6 || password.length() > 20) {
            throw new HttpException(401, "密码必须6-20之间");
        }
    }
}
