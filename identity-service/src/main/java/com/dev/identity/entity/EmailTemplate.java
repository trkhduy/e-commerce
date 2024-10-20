package com.dev.identity.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Lob
    String content;
    String type;
    String subject;
}

