package kr.adapterz.springboot.user;

import kr.adapterz.springboot.global.exception.UserNotFoundException;
import kr.adapterz.springboot.user.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public RegisterResponse register(RegisterRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()){
            throw new IllegalArgumentException("empty_email");
        }
        if (request.getNickname() == null || request.getNickname().isBlank()){
            throw new IllegalArgumentException("empty_nickname");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()){
            throw new IllegalArgumentException("empty_password");
        }
        if (userRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("already_exist_email");
        }
        if (userRepository.existsByNickname(request.getNickname())){
            throw new IllegalArgumentException("already_exist_nickname");
        }
        User user = new User(
                request.getEmail(),
                request.getPassword(),
                request.getNickname(),
                request.getProfileImage()
        );

        User savedUser = userRepository.save(user);

        return new RegisterResponse(savedUser.getId());
    }

    public LoginResponse login(LoginRequest request){
        if (request.getEmail() == null || request.getEmail().isBlank()){
            throw new IllegalArgumentException("empty_email");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()){
            throw new IllegalArgumentException("empty_password");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("login_failed"));

        if(!user.getPassword().equals(request.getPassword())){
            throw new IllegalArgumentException("login_failed");
        }

        return new LoginResponse(
                "fake-user-Token_" + user.getId(),
                user.getId()
        );
    }

    public UserUpdateResponse update(UserUpdateRequest request){
        if (request.getNickname() == null || request.getNickname().isBlank()){
            throw new IllegalArgumentException("empty_nickname");
        }
        if (request.getProfileImage() == null || request.getProfileImage().isBlank()){
            throw new IllegalArgumentException("empty_profileImage");
        }
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("update_failed"));

        if (!user.getNickname().equals(request.getNickname())
                && userRepository.existsByNickname(request.getNickname())) {
            throw new IllegalArgumentException("already_exist_nickname");
        }

        user.updateProfile(request.getNickname(), request.getProfileImage());

        return new UserUpdateResponse(
                user.getNickname(),
                user.getProfileImage()
        );
    }

    public void deleteUser(UserDeleteRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException());

        user.delete();
    }

    public void updatePass(UserUpdatePassRequest request){
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(()-> new UserNotFoundException());

        if (user.getDeleted()){
            throw new IllegalArgumentException("탈퇴한 사용자입니다.");
        }
        user.changePassword(request.getNewpassword());
    }
}
