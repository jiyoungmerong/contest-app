package hello.demo.service;

import hello.demo.domain.User;
import hello.demo.repository.MemoryUserRepository;
import hello.demo.repository.UserRepository;

public class SignInServiceImpl implements SignInService{

    private final UserRepository userRepository = new MemoryUserRepository();

    @Override
    public User logIn(Long id, String pw) {
        User user = userRepository.findById(id);
        if (user.getPw() == pw) {
            return  user;
        }
        else {
            return null;
        }
    }
}
