package sample.service;

import sample.model.User;
import sample.repository.UserDummyRepo;

public class UserService {
    private UserDummyRepo userDummyRepo;

    public UserService() {
        userDummyRepo = new UserDummyRepo();
    }

    public boolean validateLogin(User user) {
       return userDummyRepo.validateLogin(user);
    }
}
