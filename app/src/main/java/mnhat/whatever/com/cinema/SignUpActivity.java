package mnhat.whatever.com.cinema;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    EditText edt_userName,edt_email,edt_password,edt_rePassword;
    IconTextView icUsername,icMail,icPass1,icPass2;
    TextView tSignUp;
    Button btn_signUp, btn_signIn;
    Utility util = new Utility();
    APIService mAPIService;
    Animation up, down, left, right;
    String e,p;
    ScrollView screen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edt_userName = (EditText) findViewById(R.id.et_Username);
        edt_email = (EditText) findViewById(R.id.et_Email);
        edt_password = (EditText) findViewById(R.id.et_Password);
        edt_rePassword = (EditText) findViewById(R.id.et_ReEnterPass);
        btn_signUp = (Button) findViewById(R.id.btn_SignUp);
        btn_signIn = (Button) findViewById(R.id.btn_SignIn);
        icUsername = (IconTextView) findViewById(R.id.tv_user_icon);
        icMail = (IconTextView) findViewById(R.id.tv_mail_icon);
        icPass1 = (IconTextView) findViewById(R.id.tv_lock_icon1);
        icPass2 = (IconTextView) findViewById(R.id.tv_lock_icon2);
        tSignUp = (TextView) findViewById(R.id.tv_sign_up);
        screen = (ScrollView) findViewById(R.id.screen);

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
        btn_signIn.setAnimation(up);

        mAPIService = APIUtils.getAPIService();

        screen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard(view);
                return true;
            }
        });

        btn_signIn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        view.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        {
                            Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                    // Your action here on button click
                    case MotionEvent.ACTION_CANCEL: {
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return true;
            }
        });

        btn_signUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        view.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:{
                        int validate = util.checkSignUpValidate(edt_userName,edt_email,edt_password,edt_rePassword);
                        switch (validate) {
                            case 1:{
                                Toast t = Toast.makeText(SignUpActivity.this,"Vui lòng nhập Username",Toast.LENGTH_LONG);
                                t.getView().setBackgroundColor(R.drawable.toast);
                                t.show();
                                break;
                            }

                            case 2:{
                                Toast t =Toast.makeText(SignUpActivity.this,"Vui lòng nhập Email",Toast.LENGTH_LONG);
                                t.getView().setBackgroundColor(R.drawable.toast);
                                t.show();
                                break;
                            }

                            case 21:{
                                Toast t = Toast.makeText(SignUpActivity.this,"Email không hợp lệ. Vui lòng nhập lại",Toast.LENGTH_LONG);
                                t.getView().setBackgroundColor(R.drawable.toast);
                                t.show();
                                break;
                            }

                            case 3:{
                                Toast t =Toast.makeText(SignUpActivity.this,"Vui lòng nhập Password",Toast.LENGTH_LONG);
                                t.getView().setBackgroundColor(R.drawable.toast);
                                t.show();
                                break;
                            }

                            case 4:{
                                Toast t = Toast.makeText(SignUpActivity.this,"Vui lòng nhập Password lần hai",Toast.LENGTH_LONG);
                                t.getView().setBackgroundColor(R.drawable.toast);
                                t.show();
                                break;
                            }

                            case 34:{
                                Toast t = Toast.makeText(SignUpActivity.this,"2 Password không giống nhau. Vui lòng nhập lại",Toast.LENGTH_LONG);
                                t.getView().setBackgroundColor(R.drawable.toast);
                                t.show();
                                break;
                            }
                            case 0:{
                                signUP(edt_userName,edt_email,edt_password);
                            }
                        }
                    }
                    // Your action here on button click
                    case MotionEvent.ACTION_CANCEL: {
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return true;
            }
        });

    }

    public void signUP(final EditText username, final EditText email, final EditText password){
        SignUpData data = new SignUpData();
        data.setEmail(email.getText().toString());
        data.setUsername(username.getText().toString());
        data.setPassword(password.getText().toString());
        final ProgressDialog loadDialog = new ProgressDialog(SignUpActivity.this,R.style.AlertDialogCustom);
        loadDialog.setMessage("Loading");
        loadDialog.show();
        mAPIService.signUpInfo(data).enqueue(new Callback<SignUpData>() {
            @Override
            public void onResponse(Call<SignUpData> call, Response<SignUpData> response) {
                if (response.isSuccessful()){
                    Log.e("onResponse", response.message() + "__" + response.toString());
                    Toast t = Toast.makeText(SignUpActivity.this," Đăng ký thành công!",Toast.LENGTH_LONG);
                    t.getView().setBackgroundColor(R.drawable.toast);
                    t.show();
                    loadDialog.dismiss();
                    goSignIn(email,password);

                }
                else {
                    Log.e("onResponse", response.message() + "__" + response.toString());
                    Toast t = Toast.makeText(SignUpActivity.this,"Tài khoản đã tồn tại. Vui lòng kiểm tra lại",Toast.LENGTH_LONG);
                    t.getView().setBackgroundColor(R.drawable.toast);
                    t.show();
                    loadDialog.dismiss();

                }
            }

            @Override
            public void onFailure(Call<SignUpData> call, Throwable t) {

            }
        });
    }

    public void goSignIn(final EditText email, final EditText password){
        final ProgressDialog loadDialog = new ProgressDialog(SignUpActivity.this,R.style.AlertDialogCustom);
        loadDialog.setMessage("Loading");
        loadDialog.show();
        mAPIService.singIn(email.getText().toString(),password.getText().toString()).enqueue(new Callback<SignInResponse>() {
            @Override
            public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {
                if (response.isSuccessful()){
                    Log.e("onResponse", response.message() + "__" + response.toString());
                    //Toast.makeText(SignUpActivity.this," Đăng nhập thành công!",Toast.LENGTH_LONG).show();

                    Boolean isLogin = true;
                    String token = response.body().getToken();
                    e = email.getText().toString();
                    p = password.getText().toString();
                    String userId = response.body().getUser().getId();
                    SharedPreferences pre = getSharedPreferences("access_token",MODE_PRIVATE);
                    SharedPreferences.Editor editor = pre.edit();
                    editor.putString("token",token);
                    editor.putBoolean("isLogin",isLogin);
                    editor.putString("userId", userId);
                    editor.commit();
                    SharedPreferences pre_signIn = getSharedPreferences("signInLog",MODE_PRIVATE);
                    SharedPreferences.Editor logIn = pre_signIn.edit();
                    logIn.putString("email",e);
                    logIn.putString("password",p);
                    logIn.commit();
                    Intent intent = new Intent(SignUpActivity.this, ListFilmActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    loadDialog.dismiss();
                    startActivity(intent);
                    finish();

                }
                else{
                }
            }

            @Override
            public void onFailure(Call<SignInResponse> call, Throwable t) {

            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
