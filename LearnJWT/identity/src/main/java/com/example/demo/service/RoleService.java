package com.example.demo.service;

import com.example.demo.dto.request.RoleRequest;
import com.example.demo.dto.response.RoleResponse;
import com.example.demo.entity.Permission;
import com.example.demo.entity.Role;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.repository.PermissionRepo;
import com.example.demo.repository.RoleRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleService {
    RoleRepo roleRepo;
    RoleMapper roleMapper;
    PermissionRepo permissionRepo;

    public RoleResponse create(RoleRequest request){
        Role role = roleMapper.toRole(request);
        List<Permission> permissionSet = permissionRepo.findAllById(request.getPermissionSet());
        role.setPermissionSet(new HashSet<>(permissionSet));
        return roleMapper.toRoleResponse(roleRepo.save(role));
    }

    public List<RoleResponse> getAll(){
        return roleRepo.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    public void delete(Long id){
        roleRepo.deleteById(id);
    }

}
