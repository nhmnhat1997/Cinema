package mnhat.whatever.com.cinema;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    EditText email, password;
    Button signIn, signUp;
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

        mAPIService = APIUtils.getAPIService();

        signIn.setOnClickListener(new View.OnClickListener() {
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
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intent);
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
