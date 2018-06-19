package mnhat.whatever.com.cinema;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class Utility {
    public int checkFilmValidate(EditText name, EditText date){
        if (name.getText().toString().equals("")){
            return 1;
        }
        boolean validDate = false;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date in = dateFormat.parse(date.getText().toString());
            String out = dateFormat.format(in);
            if (date.getText().toString().equals(out))
                validDate = true;
        } catch (Exception ignore) {}
        if (validDate != true){
            return 2;
        }

        return 0;
    }

    public int checkSignUpValidate(EditText username, EditText email, EditText password, EditText rePassword){
        if (username.getText().toString().equals("")){
            return 1;
        }
        if (email.getText().toString().equals("")){
            return 2;
        }
        else {
            Matcher matcher = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(email.getText().toString());
            if (!matcher.find()){
                return 21;
            }
        }

        if (password.getText().toString().equals("")){
            return 3;
        }

        if (rePassword.getText().toString().equals("")){
            return 4;
        }

        if (!password.getText().toString().equals(rePassword.getText().toString())){
            return 34;
        }
        return 0;
    }

    public int checkSignInValidate(EditText email, EditText password){
        if (email.getText().toString().equals("")){
            return 1;
        }
        else {
            Matcher matcher = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(email.getText().toString());
            if (!matcher.find()){
                return 11;
            }
        }
        if (password.getText().toString().equals("")){
            return 2;
        }
        return 0;
    }

    public int checkEmail(String email){
        if (email.equals("")){
            return 1;
        }
        else {
            Matcher matcher = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(email);
            if (!matcher.find()){
                return 2;
            }
        }
        return 0;
    }

}
