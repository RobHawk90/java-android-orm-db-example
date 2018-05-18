package robhawk.com.br.orm_example.ui;

import android.content.Context;
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
import robhawk.com.br.orm_example.databinding.ActivityRegisterBinding;
import robhawk.com.br.orm_example.orm.reflection.DaoFactory;

public class RegisterActivity extends AppCompatActivity {

    private User mUser;
    private UserDao mUserDao;

    public static Intent getIntent(Context context, String email, String password) {
        Intent intent = new Intent(context, RegisterActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUser = new User();
        mUserDao = DaoFactory.create(UserDao.class);

        ActivityRegisterBinding dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        dataBinding.setUser(mUser);

        initViews();
        initIntent();
    }

    private void initIntent() {
        Intent intent = getIntent();
        mUser.email = intent.getStringExtra("email");
        mUser.password = intent.getStringExtra("password");
    }

    private void initViews() {
        EditText passwordConfirm = findViewById(R.id.activity_register_password_confirm);

        passwordConfirm.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                    register(textView);
                return false;
            }
        });
    }

    public void register(View view) {
        if (isWrongPassword(mUser.password, mUser.passwordConfirm)) return;
        if (isEmailExists(mUser.email)) return;

        if (mUserDao.insert(mUser))
            startTaskActivity(mUser);
        else
            Toast.makeText(this, mUser.name + ", can't register you now, please verify your information.", Toast.LENGTH_SHORT).show();
    }

    private boolean isEmailExists(String email) {
        if (mUserDao.findByEmail(email) == null)
            return false;
        Toast.makeText(this, "An user was already registered as '" + email + "'", Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean isWrongPassword(String password, String passwordConfirm) {
        if (password.equals(passwordConfirm))
            return false;
        Toast.makeText(this, "Passwords don't math", Toast.LENGTH_SHORT).show();
        return true;
    }

    private void startTaskActivity(User user) {
        Intent task = TaskActivity.getIntent(this, user);
        startActivity(task);
        finish();
    }
}
