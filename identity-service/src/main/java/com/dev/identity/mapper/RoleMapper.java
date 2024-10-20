package com.dev.identity.mapper;

import com.dev.identity.dto.request.RoleRequest;
import com.dev.identity.dto.response.RoleResponse;
import com.dev.identity.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);

    @Mapping(target = "permissions", ignore = true)
    void updateRole(@MappingTarget Role role, RoleRequest request);

}
