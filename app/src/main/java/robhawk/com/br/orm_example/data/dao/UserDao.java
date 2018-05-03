package robhawk.com.br.orm_example.data.dao;

import robhawk.com.br.orm_example.data.model.User;
import robhawk.com.br.orm_example.orm.annotation.Query;
import robhawk.com.br.orm_example.orm.reflection.Dao;

public interface UserDao extends Dao<User> {

    @Query("SELECT * FROM user WHERE email = ? AND password = ?")
    User findByEmailAndPassword(String email, String password);

    @Query("SELECT * FROM user WHERE email = ?")
    User findByEmail(String email);

}
