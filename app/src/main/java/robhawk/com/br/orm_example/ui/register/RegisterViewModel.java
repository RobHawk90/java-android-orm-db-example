package robhawk.com.br.orm_example.ui.register;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import robhawk.com.br.orm_example.data.dao.UserDao;
import robhawk.com.br.orm_example.data.model.User;
import robhawk.com.br.orm_example.orm.reflection.DaoFactory;

public class RegisterViewModel extends ViewModel {

    private final MutableLiveData<User> user;
    private final UserDao dao;
    private RegisterNavigator navigator;

    public RegisterViewModel() {
        user = new MutableLiveData<>();
        dao = DaoFactory.create(UserDao.class);
    }

    public void setNavigator(RegisterNavigator navigator) {
        this.navigator = navigator;
    }

    public void setUser(User user) {
        this.user.setValue(user);
    }

    public User getUser() {
        return user.getValue();
    }

    public void register() {
        User user = getUser();
        if (isEmailExists(user.email))
            navigator.showEmailExists(user.email);
        else if (user.isConfirmPassword()) {
            if (dao.insert(user))
                navigator.startTaskActivity(user);
            else
                navigator.showSaveUserError(user);
        } else
            navigator.showWrongPassword();

    }

    private boolean isEmailExists(String email) {
        User exists = dao.findByEmail(email);
        return exists != null;
    }
}
