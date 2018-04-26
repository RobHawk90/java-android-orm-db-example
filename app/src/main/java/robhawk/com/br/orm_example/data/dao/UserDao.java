package robhawk.com.br.orm_example.data.dao;

import java.util.concurrent.atomic.AtomicInteger;

import robhawk.com.br.orm_example.data.model.User;
import robhawk.com.br.orm_example.util.Repository;

public class UserDao {
    private static final AtomicInteger KEY = new AtomicInteger();
    private static final Repository<User> REPOSITORY = new Repository<>();

    public void insert(User user) {
        user.id = KEY.incrementAndGet();
        REPOSITORY.append(user.id, user);
    }

    public User auth(String email, String password) {
        for (User user : REPOSITORY.listAll())
            if (email.equals(user.email) && password.equals(user.password))
                return user;
        return null;
    }

    public boolean isEmailExists(String email) {
        for (User user : REPOSITORY.listAll())
            if (user.email.equals(email))
                return true;
        return false;
    }

    public User findById(int id) {
        for (User user : REPOSITORY.listAll())
            if (user.id == id)
                return user;
        return null;
    }
}
