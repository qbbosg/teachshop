package plus.suja.teach.teachshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import plus.suja.teach.teachshop.entity.Role;
import plus.suja.teach.teachshop.service.RoleService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping
    public String createRole(@RequestBody Map<String, Object> params) {
        Role role = new Role();
        role.setName((String)params.get("name"));
        return roleService.create(role);
    }
}
