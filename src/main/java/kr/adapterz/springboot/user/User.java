package kr.adapterz.springboot.user;

import lombok.Getter;
import lombok.Setter;

@Getter
public class User {
    @Setter
    private Long id;

    private String email;
    private String password;
    private String profileImage;
    private String nickname;
    private Boolean deleted;

    public User(String email, String password, String nickname, String profileImage){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.deleted = false;
    }

    // 회원정보(닉네임, 프로필 이미지) 수정
    public void updateProfile(String nickname, String profileImage){
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    //비밀번호 변경
    public void changePassword(String newPassword){
        this.password = newPassword;
    }

    //사용자 탈퇴
    public void delete(){
        this.deleted = true;
    }
}
