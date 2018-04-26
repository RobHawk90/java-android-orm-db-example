package robhawk.com.br.orm_example.ui;

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

public class LoginActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    private UserDao mUserDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        mUserDao = new UserDao(this);
    }

    private void initViews() {
        mEmail = findViewById(R.id.activity_login_email);
        mPassword = findViewById(R.id.activity_login_password);

        mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                    authenticate(textView);
                return false;
            }
        });
    }

    public void authenticate(View view) {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        User user = mUserDao.auth(email, password);

        if (user == null)
            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        else
            startTaskActivity(user);
    }

    private void startTaskActivity(User user) {
        Intent task = TaskActivity.getIntent(this, user);
        startActivity(task);
        finish();
    }

    public void register(View view) {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        startRegisterActivity(email, password);
    }

    private void startRegisterActivity(String email, String password) {
        Intent register = RegisterActivity.getIntent(this, email, password);
        startActivity(register);
    }
}
