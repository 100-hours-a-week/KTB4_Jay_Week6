package kr.adapterz.springboot.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdatePassRequest {
    private Long userId;
    private String newpassword;
    private String newpasswordCheck;
}
