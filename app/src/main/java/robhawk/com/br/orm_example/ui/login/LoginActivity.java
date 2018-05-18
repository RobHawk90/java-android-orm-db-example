package robhawk.com.br.orm_example.ui.login;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import robhawk.com.br.orm_example.R;
import robhawk.com.br.orm_example.data.model.User;
import robhawk.com.br.orm_example.databinding.ActivityLoginBinding;
import robhawk.com.br.orm_example.ui.task.TaskActivity;
import robhawk.com.br.orm_example.ui.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity implements LoginNavigator {

    private LoginViewModel mVM;
    private ActivityLoginBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind();
        setup();
    }

    private void bind() {
        mVM = ViewModelProviders.of(this).get(LoginViewModel.class);
        mVM.setNavigator(this);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mBinding.setVm(mVM);
    }

    private void setup() {
        mBinding.password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                    mVM.authenticate();
                return false;
            }
        });
    }

    public void showInvalidUser() {
        Toast.makeText(this, R.string.invalid_email_password, Toast.LENGTH_SHORT).show();
    }

    public void startTaskActivity(User user) {
        Intent task = TaskActivity.getIntent(this, user);
        startActivity(task);
        finish();
    }

    public void startRegisterActivity(User user) {
        Intent register = RegisterActivity.getIntent(this, user);
        startActivity(register);
    }
}
