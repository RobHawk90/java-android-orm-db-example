package robhawk.com.br.orm_example.ui.login;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import robhawk.com.br.orm_example.data.dao.UserDao;
import robhawk.com.br.orm_example.data.model.User;
import robhawk.com.br.orm_example.orm.reflection.DaoFactory;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<User> user;
    private UserDao dao;
    private LoginNavigator navigator;

    public LoginViewModel() {
        user = new MutableLiveData<>();
        user.setValue(new User());
        dao = DaoFactory.create(UserDao.class);
    }

    public User getUser() {
        return user.getValue();
    }

    public void authenticate() {
        User user = getUser();
        User authUser = dao.findByEmailAndPassword(user.email, user.password);
        if (authUser == null)
            navigator.showInvalidUser();
        else
            navigator.startTaskActivity(authUser);
    }

    public void register() {
        navigator.startRegisterActivity(getUser());
    }

    public void setNavigator(LoginNavigator navigator) {
        this.navigator = navigator;
    }
}
