package hello.demo.service;

import hello.demo.domain.User;
import hello.demo.repository.MemoryUserRepository;
import hello.demo.repository.UserRepository;

public class SignUpServiceImpl implements SignUpService{

    private final UserRepository userRepository = new MemoryUserRepository();

    @Override
    public void join(User user) {
        userRepository.save(user);
    }

    @Override
    public User findUser(Long id) {
        return userRepository.findById(id);
    }
}
