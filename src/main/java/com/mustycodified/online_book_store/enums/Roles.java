package com.mustycodified.online_book_store.enums;

import com.google.common.collect.Sets;
import lombok.Getter;

import java.util.Set;

import static com.mustycodified.online_book_store.enums.UserPermissions.*;


@Getter
public enum Roles {
    USER(Sets.newHashSet(USER_READ, USER_EDIT)),
    ADMIN(Sets.newHashSet(USER_DELETE, USER_READ, USER_EDIT));
    private final Set<UserPermissions> permissions;

    Roles(Set<UserPermissions> permissions){
        this.permissions = permissions;
    }
}
