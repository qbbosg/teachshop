package plus.suja.teach.teachshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import plus.suja.teach.teachshop.entity.Permission;
import plus.suja.teach.teachshop.service.PermissionService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/permissions")
public class PermissionCotroller {
    @Autowired
    private PermissionService permissionService;

    @PostMapping
    public String createPermission(@RequestBody Map<String, Object> params) {
        Permission permission = new Permission();
        permission.setName((String) params.get("name"));
        return permissionService.create(permission);
    }
}
