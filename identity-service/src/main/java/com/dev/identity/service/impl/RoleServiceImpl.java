package com.dev.identity.service.impl;

import com.dev.commons.Message;
import com.dev.commons.exception.CustomException;
import com.dev.commons.response.ErrorModel;
import com.dev.identity.dto.request.RoleRequest;
import com.dev.identity.dto.response.RoleResponse;
import com.dev.identity.entity.Role;
import com.dev.identity.mapper.RoleMapper;
import com.dev.identity.repository.PermissionRepository;
import com.dev.identity.repository.RoleRepository;
import com.dev.identity.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {

    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    @Override
    public RoleResponse create(RoleRequest request) {
        if (roleRepository.existsByName(request.getName()))
            throw new CustomException(new ErrorModel(400, Message.Role.ROLE_ALREADY_EXISTED));

        try {
            var role = roleMapper.toRole(request);

            var permissions = permissionRepository.findAllById(request.getPermissions());
            role.setPermissions(new HashSet<>(permissions));
            log.info("Creating role ...");
            return roleMapper.toRoleResponse(roleRepository.save(role));
        } catch (Exception e) {
            log.error("Error creating role!", e);
            throw new CustomException(new ErrorModel(400, Message.Role.CREATE_FAILED));
        }

    }

    @Override
    public List<RoleResponse> getAll() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    @Override
    public RoleResponse update(RoleRequest request, String id) {

        Role role = findById(id);
        if (roleRepository.existsByNameAndIdNot(request.getName(), id))
            throw new CustomException(new ErrorModel(400, Message.Role.ROLE_ALREADY_EXISTED));

        try {
            roleMapper.updateRole(role, request);
            var permissions = permissionRepository.findAllById(request.getPermissions());
            role.setPermissions(new HashSet<>(permissions));
            log.info("Updating role ...");
            return roleMapper.toRoleResponse(roleRepository.save(role));
        } catch (Exception e) {
            log.error("Error updating role!", e);
            throw new CustomException(new ErrorModel(400, Message.Role.UPDATE_FAILED));
        }
    }

    @Override
    public void delete(String id) {
        log.info("Deleting role ...");
        roleRepository.delete(findById(id));
    }

    private Role findById(String id) {
        return roleRepository.findById(id).orElseThrow(() -> new CustomException(new ErrorModel(400, Message.Role.ROLE_DOES_NOT_EXITED)));
    }
}
