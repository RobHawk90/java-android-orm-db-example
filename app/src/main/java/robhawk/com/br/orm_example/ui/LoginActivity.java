package robhawk.com.br.orm_example.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import robhawk.com.br.orm_example.R;
import robhawk.com.br.orm_example.data.dao.UserDao;
import robhawk.com.br.orm_example.data.model.User;
import robhawk.com.br.orm_example.databinding.ActivityLoginBinding;
import robhawk.com.br.orm_example.orm.reflection.DaoFactory;

public class LoginActivity extends AppCompatActivity {

    private UserDao mUserDao;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUser = new User();
        mUserDao = DaoFactory.create(UserDao.class);

        ActivityLoginBinding dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        dataBinding.setUser(mUser);

        initViews();
    }

    private void initViews() {
        EditText password = findViewById(R.id.activity_login_password);
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                    authenticate(textView);
                return false;
            }
        });
    }

    public void authenticate(View view) {
        User authUser = mUserDao.findByEmailAndPassword(mUser.email, mUser.password);

        if (authUser == null)
            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        else
            startTaskActivity(authUser);
    }

    private void startTaskActivity(User user) {
        Intent task = TaskActivity.getIntent(this, user);
        startActivity(task);
        finish();
    }

    public void register(View view) {
        startRegisterActivity(mUser.email, mUser.password);
    }

    private void startRegisterActivity(String email, String password) {
        Intent register = RegisterActivity.getIntent(this, email, password);
        startActivity(register);
    }
}
