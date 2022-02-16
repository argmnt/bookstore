package com.getir.bookstore.jwt;


import lombok.Data;

import java.io.Serializable;

@Data
public class JWTToken implements Serializable {
    private String token;
}
