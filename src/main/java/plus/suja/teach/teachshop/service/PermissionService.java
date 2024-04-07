package plus.suja.teach.teachshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import plus.suja.teach.teachshop.dao.PermissionRepository;
import plus.suja.teach.teachshop.entity.Permission;

@Service
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    public String create(Permission permission) {
        permissionRepository.save(permission);
        return "created";
    }
}
