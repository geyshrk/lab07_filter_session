package ru.msnigirev.oris.authorisation.session.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {
    private String username;

    private String email;

    private String phoneNumber;
}
