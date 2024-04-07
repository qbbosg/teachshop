package plus.suja.teach.teachshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import plus.suja.teach.teachshop.dao.RoleRepository;
import plus.suja.teach.teachshop.entity.Role;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
    public String create(Role role) {
        roleRepository.save(role);
        return "create";
    }
}
