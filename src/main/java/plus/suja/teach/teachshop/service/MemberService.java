package plus.suja.teach.teachshop.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import plus.suja.teach.teachshop.annotation.Admin;
import plus.suja.teach.teachshop.annotation.Teacher;
import plus.suja.teach.teachshop.dao.MemberRepository;
import plus.suja.teach.teachshop.dao.RoleRepository;
import plus.suja.teach.teachshop.entity.Member;
import plus.suja.teach.teachshop.entity.PageResponse;
import plus.suja.teach.teachshop.entity.Role;
import plus.suja.teach.teachshop.entity.Session;
import plus.suja.teach.teachshop.enums.Status;
import plus.suja.teach.teachshop.exception.HttpException;

import java.util.List;

import static plus.suja.teach.teachshop.config.HttpInterceptor.SESSION_ID;

@Service
public class MemberService {

    private SessionService sessionService;
    private MemberRepository memberRepository;
    private RoleRepository roleRepository;

    @Autowired
    public MemberService(SessionService sessionService, MemberRepository memberRepository, RoleRepository roleRepository) {
        this.sessionService = sessionService;
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
        Session session = sessionService.save(member);
        response.addCookie(new Cookie(SESSION_ID, session.getCookie()));
        response.setStatus(200);
        return member;
    }

    public Member register(String username, String password, HttpServletResponse response) {
        return registerAndSetRole(username, password, "student", response);
    }


    private Role getRole(String roleName) {
        List<Role> roles = (List<Role>) roleRepository.findAll();
        return roles.stream().filter(r -> roleName.equals(r.getName())).findFirst().orElseThrow(() -> new HttpException(404, "Not found"));
    }

    @Admin
    public PageResponse<Member> getAllMember(Integer pageNum, Integer pageSize) {
        return new PageResponse<Member>().getAllPageResponse(pageNum, pageSize, memberRepository::findAll);
    }


    public Member getMemberById(Integer id) {
        return memberRepository.findById(id).orElseThrow(() -> new HttpException(404, "Not find"));
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
    public List<Member> getAllStudents() {
        return memberRepository.findAllByRoleName("student");
    }

    public Member getStudent(Integer id) {
        return getMemberByIdAndHavaRole(id, "student");
    }

    @Teacher
    public Member createStudent(String username, String password, HttpServletResponse response) {
        return registerStudentType(username, password, response);
    }

    @Teacher
    public Member modifyStudent(Integer id, String username, Status status) {
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

    private Member getMemberByIdAndHavaRole(Integer id, String roleName) {
        Member member = getMemberById(id);
        if (member.getRoles().stream().anyMatch(r -> roleName.contains(r.getName()))) {
            return member;
        } else {
            throw new HttpException(404, "Not find");
        }
    }

    public void checkoutUsernameAndPasswordForm(String username, String password) {
        if (!StringUtils.hasLength(username) || username.length() < 6 || username.length() > 20) {
            throw new HttpException(401, "用户名必须6-20之间");
        }
        if (!StringUtils.hasLength(password) || password.length() < 6 || password.length() > 20) {
            throw new HttpException(401, "密码必须6-20之间");
        }
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
}
