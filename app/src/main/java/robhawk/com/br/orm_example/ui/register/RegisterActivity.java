package robhawk.com.br.orm_example.ui.register;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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
import robhawk.com.br.orm_example.databinding.ActivityRegisterBinding;
import robhawk.com.br.orm_example.ui.task.TaskActivity;

public class RegisterActivity extends AppCompatActivity implements RegisterNavigator {

    private ActivityRegisterBinding mBinding;
    private RegisterViewModel mVM;

    public static Intent getIntent(Context context, User user) {
        Intent intent = new Intent(context, RegisterActivity.class);
        intent.putExtra("user", user);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind();
        setup();
    }

    private void bind() {
        mVM = ViewModelProviders.of(this).get(RegisterViewModel.class);
        mVM.setNavigator(this);
        mVM.setUser(getIntentData());
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        mBinding.setVm(mVM);
    }

    private User getIntentData() {
        return (User) getIntent().getSerializableExtra("user");
    }

    private void setup() {
        mBinding.passwordConfirm.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                    mVM.register();
                return false;
            }
        });
    }

    @Override
    public void showEmailExists(String email) {
        String msg = getResources().getString(R.string.email_x_already_exists);
        Toast.makeText(this, String.format(msg, email), Toast.LENGTH_SHORT).show();
    }

    public void startTaskActivity(User user) {
        Intent task = TaskActivity.getIntent(this, user);
        startActivity(task);
        finish();
    }

    @Override
    public void showSaveUserError(User user) {
        String msg = getString(R.string.register_error_verify_informations);
        Toast.makeText(this, String.format(msg, user.name), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showWrongPassword() {
        Toast.makeText(this, R.string.passwords_dont_match, Toast.LENGTH_SHORT).show();
    }
}
