package com.dev.identity.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {
    Integer id;
    String name;
    String description;
    Set<PermissionResponse> permissions;
}
