//회원가입 요청 dto

package kr.adapterz.springboot.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegisterRequest {
    private String email;
    private String password;
    private String nickname;
    private String profileImage;
}