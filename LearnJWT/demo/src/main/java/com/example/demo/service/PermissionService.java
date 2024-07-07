package com.example.demo.service;

import com.example.demo.dto.request.PermissionRequest;
import com.example.demo.dto.response.PermissionResponse;
import com.example.demo.entity.Permission;
import com.example.demo.mapper.PermissionMapper;
import com.example.demo.repository.PermissionRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionService {
    PermissionRepo permissionRepo;
    PermissionMapper permissionMapper;

    public PermissionResponse create (PermissionRequest request){
        Permission permission = permissionMapper.toPermission(request);
        permissionRepo.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll(){
        List<Permission> permissionList = permissionRepo.findAll();
        return permissionList.stream().map(
                permission -> permissionMapper.toPermissionResponse(permission))
                .toList();
    }

    public void delete (Long id){
        permissionRepo.deleteById(id);
    }

}
