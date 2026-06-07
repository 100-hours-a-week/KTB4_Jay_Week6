package kr.adapterz.springboot.user;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepository {

    private final Map<Long, User> store = new HashMap<>();
    private long sequence = 1L;

    public User save(User user) {
        user.setId(sequence);
        store.put(sequence, user);
        sequence ++;

        return user;
    }
    public List<User> findAll() {
        return new ArrayList<>(store.values());
    }
    public boolean existsByEmail(String email){
        return store.values().stream()
                .anyMatch(user ->  user.getEmail().equals(email));
    }
    public boolean existsByNickname(String nickname){
        return store.values().stream()
                .anyMatch(user -> user.getNickname().equals(nickname));
    }
    public Optional<User> findByEmail(String email){
        return store.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }
    public Optional<User> findById(Long id){
        return store.values().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }
}
