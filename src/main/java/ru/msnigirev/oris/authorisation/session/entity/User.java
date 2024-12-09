package ru.msnigirev.oris.authorisation.session.entity;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    private String username;

    private String password;

    private String sessionId;

    private String email;

    private String phoneNumber;
}
