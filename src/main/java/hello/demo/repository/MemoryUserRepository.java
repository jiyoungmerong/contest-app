package hello.demo.repository;

import hello.demo.domain.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryUserRepository implements UserRepository{

    private static Map<Long, User> store = new HashMap<>();

    @Override
    public void save(User user) {
        store.put(user.getId(), user);
    }

    @Override
    public User findById(Long id) {
        return store.get(id);
    }

    @Override
    public User findByName(String name) {
        return store.get(name);
    }
}
