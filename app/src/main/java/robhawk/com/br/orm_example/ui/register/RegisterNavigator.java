package robhawk.com.br.orm_example.ui.register;

import robhawk.com.br.orm_example.data.model.User;

interface RegisterNavigator {
    void showEmailExists(String email);

    void startTaskActivity(User user);

    void showSaveUserError(User user);

    void showWrongPassword();
}
