package mnhat.whatever.com.cinema;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {
    public boolean checkValidate(EditText name, EditText date){
        if (name.getText().toString().equals("")){
            return false;
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
            return false;
        }

        return true;
    }
}