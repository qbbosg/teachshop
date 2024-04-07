package plus.suja.teach.teachshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import plus.suja.teach.teachshop.entity.Session;
import plus.suja.teach.teachshop.service.AuthService;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    @Autowired
    private AuthService authService;
    @GetMapping("/session")
    public Session getSession () {
        return authService.getSession();
    }
}
