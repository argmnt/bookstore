package com.getir.bookstore.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AbstractBaseCollection {

    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now().atZone(ZoneId.systemDefault())
            .withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime().truncatedTo(ChronoUnit.SECONDS);


    @LastModifiedDate
    private LocalDateTime updatedAt = LocalDateTime.now().atZone(ZoneId.systemDefault())
            .withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime().truncatedTo(ChronoUnit.SECONDS);
}
