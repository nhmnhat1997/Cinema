package mnhat.whatever.com.cinema;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    EditText email, password;
    TextView tSignIn, tOr;
    IconTextView tEmail, tPassword;
    Button signIn, signUp;
    Animation up, down, left, right;
    Utility util = new Utility();
    APIService mAPIService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        SharedPreferences pre = getSharedPreferences("access_token",MODE_PRIVATE);
        if (pre.getBoolean("isLogin",false) == true)
        {
            Intent intent = new Intent(SignInActivity.this, ListFilmActivity.class);
            startActivity(intent);
            finish();
        }

        email = (EditText) findViewById(R.id.et_Email);
        password = (EditText) findViewById(R.id.et_Password);
        signIn = (Button) findViewById(R.id.btn_SignIn);
        signUp = (Button) findViewById(R.id.btn_SignUp);
        tSignIn = (TextView) findViewById(R.id.tv_sign_in);
        tOr = (TextView) findViewById(R.id.tv_or);
        tEmail = (IconTextView) findViewById(R.id.tv_mail_icon);
        tPassword = (IconTextView) findViewById(R.id.tv_lock_icon1);

        up = AnimationUtils.loadAnimation(SignInActivity.this,R.anim.up);
        down = AnimationUtils.loadAnimation(SignInActivity.this,R.anim.down);
        left = AnimationUtils.loadAnimation(SignInActivity.this,R.anim.left);
        right = AnimationUtils.loadAnimation(SignInActivity.this,R.anim.right);

        signIn.startAnimation(up);
        signUp.startAnimation(up);
        tOr.startAnimation(up);
        tSignIn.startAnimation(down);
        tEmail.startAnimation(left);
        tPassword.startAnimation(left);
        email.startAnimation(right);
        password.startAnimation(right);

        mAPIService = APIUtils.getAPIService();

        /*signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int valid = util.checkSignInValidate(email,password);
                switch (valid){
                    case 1:{
                        Toast.makeText(SignInActivity.this,"Vui lòng nhập Email",Toast.LENGTH_LONG).show();
                        return;
                    }
                    case 2:{
                        Toast.makeText(SignInActivity.this,"Vui lòng nhập Password",Toast.LENGTH_LONG).show();
                        return;
                    }
                    case 11:{
                        Toast.makeText(SignInActivity.this,"Email không đúng định dạng. Vui lòng kiểm tra lại",Toast.LENGTH_LONG).show();
                        return;
                    }
                    case 0:{
                        goSignIn(email,password);
                    }
                }
            }
        });*/

        signIn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        view.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:{
                        int valid = util.checkSignInValidate(email,password);
                        switch (valid){
                            case 1:{
                                Toast.makeText(SignInActivity.this,"Vui lòng nhập Email",Toast.LENGTH_LONG).show();
                                break;
                            }
                            case 2:{
                                Toast.makeText(SignInActivity.this,"Vui lòng nhập Password",Toast.LENGTH_LONG).show();
                                break;
                            }
                            case 11:{
                                Toast.makeText(SignInActivity.this,"Email không đúng định dạng. Vui lòng kiểm tra lại",Toast.LENGTH_LONG).show();
                                break;
                            }
                            case 0:{
                                goSignIn(email,password);
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


        signUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        view.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:{
                        Intent intent = new Intent(SignInActivity.this,SignUpActivity.class);
                        startActivity(intent);
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

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void goSignIn(EditText email, EditText password){
        mAPIService.singIn(email.getText().toString(),password.getText().toString()).enqueue(new Callback<SignInResponse>() {
            @Override
            public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {
                if (response.isSuccessful()){
                    Log.e("onResponse", response.message() + "__" + response.toString());
                    Toast.makeText(SignInActivity.this," Đăng nhập thành công!",Toast.LENGTH_LONG).show();

                    Boolean isLogin = true;
                    String token = response.body().getToken();
                    SharedPreferences pre = getSharedPreferences("access_token",MODE_PRIVATE);
                    SharedPreferences.Editor editor = pre.edit();
                    editor.putString("token",token);
                    editor.putBoolean("isLogin",isLogin);
                    editor.commit();
                    Intent intent = new Intent(SignInActivity.this, ListFilmActivity.class);
                    startActivity(intent);
                    finish();

                }
                else{
                    Log.e("onResponse", response.message() + "__" + response.toString());
                    Toast.makeText(SignInActivity.this,response.message(),Toast.LENGTH_LONG).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<SignInResponse> call, Throwable t) {

            }
        });
    }
}
