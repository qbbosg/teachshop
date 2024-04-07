package plus.suja.teach.teachshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import plus.suja.teach.teachshop.dao.UserRepository;
import plus.suja.teach.teachshop.entity.Member;

@Service
public class MemberService {
    @Autowired
    private UserRepository userRepository;

    public String login() {
        return "login";
    }

    public String register(Member member) {
        userRepository.save(member);
        return "register";
    }
}
