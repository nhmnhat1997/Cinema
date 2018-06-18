package mnhat.whatever.com.cinema;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    EditText edt_userName,edt_email,edt_password,edt_rePassword;
    IconTextView icUsername,icMail,icPass1,icPass2;
    TextView tSignUp;
    Button btn_signUp;
    Utility util = new Utility();
    APIService mAPIService;
    Animation up, down, left, right;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edt_userName = (EditText) findViewById(R.id.et_Username);
        edt_email = (EditText) findViewById(R.id.et_Email);
        edt_password = (EditText) findViewById(R.id.et_Password);
        edt_rePassword = (EditText) findViewById(R.id.et_ReEnterPass);
        btn_signUp = (Button) findViewById(R.id.btn_SignUp);
        icUsername = (IconTextView) findViewById(R.id.tv_user_icon);
        icMail = (IconTextView) findViewById(R.id.tv_mail_icon);
        icPass1 = (IconTextView) findViewById(R.id.tv_lock_icon1);
        icPass2 = (IconTextView) findViewById(R.id.tv_lock_icon2);
        tSignUp = (TextView) findViewById(R.id.tv_sign_up);

        up = AnimationUtils.loadAnimation(SignUpActivity.this,R.anim.up);
        down = AnimationUtils.loadAnimation(SignUpActivity.this,R.anim.down);
        left = AnimationUtils.loadAnimation(SignUpActivity.this,R.anim.left);
        right = AnimationUtils.loadAnimation(SignUpActivity.this,R.anim.right);

        tSignUp.setAnimation(down);
        icUsername.setAnimation(left);
        icMail.setAnimation(left);
        icPass1.setAnimation(left);
        icPass2.setAnimation(left);
        edt_userName.setAnimation(right);
        edt_email.setAnimation(right);
        edt_password.setAnimation(right);
        edt_rePassword.setAnimation(right);
        btn_signUp.setAnimation(up);

        mAPIService = APIUtils.getAPIService();
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int validate = util.checkSignUpValidate(edt_userName,edt_email,edt_password,edt_rePassword);
                switch (validate) {
                    case 1:{
                        Toast.makeText(SignUpActivity.this,"Vui lòng nhập Username",Toast.LENGTH_LONG).show();
                        return;
                    }

                    case 2:{
                        Toast.makeText(SignUpActivity.this,"Vui lòng nhập Email",Toast.LENGTH_LONG).show();
                        return;
                    }

                    case 21:{
                        Toast.makeText(SignUpActivity.this,"Email không hợp lệ. Vui lòng nhập lại",Toast.LENGTH_LONG).show();
                        return;
                    }

                    case 3:{
                        Toast.makeText(SignUpActivity.this,"Vui lòng nhập Password",Toast.LENGTH_LONG).show();
                        return;
                    }

                    case 4:{
                        Toast.makeText(SignUpActivity.this,"Vui lòng nhập Password lần hai",Toast.LENGTH_LONG).show();
                        return;
                    }

                    case 34:{
                        Toast.makeText(SignUpActivity.this,"2 Password không giống nhau. Vui lòng nhập lại",Toast.LENGTH_LONG).show();
                        return;
                    }
                    case 0:{
                        signUP(edt_userName,edt_email,edt_password);
                    }
                }

            }
        });

    }

    public void signUP(EditText username, EditText email, EditText password){
        SignUpData data = new SignUpData();
        data.setEmail(email.getText().toString());
        data.setUsername(username.getText().toString());
        data.setPassword(password.getText().toString());
        mAPIService.signUpInfo(data).enqueue(new Callback<SignUpData>() {
            @Override
            public void onResponse(Call<SignUpData> call, Response<SignUpData> response) {
                if (response.isSuccessful()){
                    Log.e("onResponse", response.message() + "__" + response.toString());
                    Toast.makeText(SignUpActivity.this," Đăng ký thành công!",Toast.LENGTH_LONG).show();
                }
                else {
                    Log.e("onResponse", response.message() + "__" + response.toString());
                    Toast.makeText(SignUpActivity.this,response.message(),Toast.LENGTH_LONG).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<SignUpData> call, Throwable t) {

            }
        });
    }
}
