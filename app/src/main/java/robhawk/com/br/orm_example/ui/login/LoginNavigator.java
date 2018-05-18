package robhawk.com.br.orm_example.ui.login;

import robhawk.com.br.orm_example.data.model.User;

public interface LoginNavigator {
    void showInvalidUser();

    void startTaskActivity(User auth);

    void startRegisterActivity(User user);
}
