package robhawk.com.br.orm_example.ui;

import android.content.Context;
import android.content.Intent;
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
import robhawk.com.br.orm_example.orm.reflection.DaoFactory;

public class RegisterActivity extends AppCompatActivity {

    private EditText mName;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mPasswordConfirm;
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
        setContentView(R.layout.activity_register);
        initViews();
        initIntent();
        mUserDao = DaoFactory.create(UserDao.class);
    }

    private void initIntent() {
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");
        mEmail.setText(email);
        mPassword.setText(password);
    }

    private void initViews() {
        mName = findViewById(R.id.activity_register_name);
        mEmail = findViewById(R.id.activity_register_email);
        mPassword = findViewById(R.id.activity_register_password);
        mPasswordConfirm = findViewById(R.id.activity_register_password_confirm);

        mPasswordConfirm.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                    register(textView);
                return false;
            }
        });
    }

    public void register(View view) {
        String name = mName.getText().toString();
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        String passwordConfirm = mPasswordConfirm.getText().toString();

        if (isWrongPassword(password, passwordConfirm)) return;
        if (isEmailExists(email)) return;

        User user = new User(name, email, password);
        if (mUserDao.insert(user))
            startTaskActivity(user);
        else
            Toast.makeText(this, name + ", can't register you now, please verify your information.", Toast.LENGTH_SHORT).show();
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
