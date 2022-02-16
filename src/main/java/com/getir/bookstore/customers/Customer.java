package com.getir.bookstore.customers;

import com.getir.bookstore.common.AbstractBaseCollection;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@NoArgsConstructor
@Data
public class Customer extends AbstractBaseCollection implements Serializable, UserDetails {

    @Builder
    public Customer(LocalDateTime createdAt, LocalDateTime updatedAt, String id, String name, String email, int age, String password) {
        super(createdAt, updatedAt);
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.password = password;
    }

    @Id
    private String id;

    private String name;

    @Indexed(unique=true)
    private String email;

    private String password;

    private int age;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
