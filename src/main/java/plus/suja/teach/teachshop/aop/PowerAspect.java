package plus.suja.teach.teachshop.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import plus.suja.teach.teachshop.entity.Member;
import plus.suja.teach.teachshop.exception.HttpException;
import plus.suja.teach.teachshop.util.UserContextUtil;

@Aspect
@Configuration
public class PowerAspect {
    @Around("@annotation(plus.suja.teach.teachshop.annotation.Admin)")
    public Object checkAdmin(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return checkPower("admin", proceedingJoinPoint);
    }

    @Around("@annotation(plus.suja.teach.teachshop.annotation.Teacher)")
    public Object checkTeacher(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return checkPower("teacher", proceedingJoinPoint);
    }

    @Around("@annotation(plus.suja.teach.teachshop.annotation.SuperAdmin)")
    public Object checkSuperAdmin(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return checkPower("root", proceedingJoinPoint);
    }

    private Object checkPower(String roleWords, ProceedingJoinPoint joinPoint) throws Throwable {
        Member currentUser = UserContextUtil.getCurrentUser();
        if (currentUser == null) {
            throw new HttpException(401, "Unauthorized");
        } else if (currentUser.getRoles().stream().anyMatch(role -> roleWords.equals(role.getName()))) {
            return joinPoint.proceed();
        } else {
            throw new HttpException(403, "没有权限");
        }
    }
}
