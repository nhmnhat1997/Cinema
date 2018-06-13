package mnhat.whatever.com.cinema;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class CreateMovie extends AppCompatActivity {
    String myFormat = "dd/MM/yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

    EditText filmName, date, description;
    Spinner gerne;
    Button choosePicture;
    String userChoosenTask;

    public static final int PICK_IMAGE_CAMERA = 200;
    public static final int PICK_IMAGE_GALLERY = 300;


    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener releaseDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            myCalendar.set(Calendar.YEAR,i);
            myCalendar.set(Calendar.MONTH,i1);
            myCalendar.set(Calendar.DAY_OF_MONTH,i2);
            updateDate(date);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_movie);
        getSupportActionBar().setTitle("Thêm thông tin phim");

        filmName = (EditText) findViewById(R.id.edtFilmName);
        date = (EditText) findViewById(R.id.edtReleaseDate);
        description = (EditText) findViewById(R.id.edtDescription);
        gerne = (Spinner) findViewById(R.id.spGerne);
        choosePicture = (Button) findViewById(R.id.btnChoosePicture);

        Date current = Calendar.getInstance().getTime();
        String currentDate = sdf.format(current);
        date.setText(currentDate);

        findViewById(R.id.linear).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });

        choosePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();

            }
        });

        String[] bTypeSpinner = new String[] {
                "Hành động", "Tâm lý", "Kinh dị", "Khoa học viễn tưởng", "Hài"
        };
        ArrayAdapter<String> gerneAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, bTypeSpinner);
        gerneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        gerne.setAdapter(gerneAdapter);

        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    hideKeyboard(view);
                    DatePickerDialog dialog = new DatePickerDialog(CreateMovie.this,releaseDate,myCalendar.get(Calendar.YEAR),
                            myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH));
                    dialog.show();
                }
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(view);
                DatePickerDialog dialog = new DatePickerDialog(CreateMovie.this,releaseDate,myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });



    }


    public void updateDate(EditText Time){
        Time.setText(sdf.format(myCalendar.getTime()));
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library"};
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateMovie.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(CreateMovie.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    /*if(result)
                        cameraIntent();*/
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    /*if(result)
                        galleryIntent();*/
                }
            }
        });
        builder.show();
    }

}
