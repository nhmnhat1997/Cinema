package mnhat.whatever.com.cinema;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreateMovieActivity extends AppCompatActivity {
    String myFormat = "dd/MM/yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
    Utility util = new Utility();

    private APIService mAPIService;
    private File mImageFile;



    EditText filmName, date, description;
    Spinner gerne;
    Button choosePicture, post;
    ImageView picture;
    Bitmap oriPic, newPic;
    String userChoosenTask;

    public static final int CAMERA = 1;
    public static final int GALLERY = 0;


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
        post = (Button) findViewById(R.id.post);
        picture = (ImageView) findViewById(R.id.filmPicture);

        oriPic = ((BitmapDrawable) picture.getDrawable()).getBitmap();

        Date current = Calendar.getInstance().getTime();
        String currentDate = sdf.format(current);
        date.setText(currentDate);

        findViewById(R.id.linear).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard(view);
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
                }
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideKeyboard(view);
                DatePickerDialog dialog = new DatePickerDialog(CreateMovieActivity.this,releaseDate,myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (util.checkValidate(filmName,date) == false){
                    Toast.makeText(CreateMovieActivity.this,"Hãy nhập đúng và đủ thông tin",Toast.LENGTH_SHORT).show();
                    return;
                }
                newPic = ((BitmapDrawable)picture.getDrawable()).getBitmap();

                if (oriPic.sameAs(newPic)){
                    Toast.makeText(CreateMovieActivity.this,"Hãy chọn ảnh cho phim",Toast.LENGTH_SHORT).show();
                    return;
                }
                createMovie();

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
            AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
            pictureDialog.setTitle("Select Action");
            String[] pictureDialogItems = {
                    "Select photo from gallery",
                    "Capture photo from camera" };
            pictureDialog.setItems(pictureDialogItems,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    choosePhotoFromGallary();
                                    break;
                                case 1:
                                    takePhotoFromCamera();
                                    break;
                            }
                        }
                    });
            pictureDialog.show();

    }
    public void choosePhotoFromGallary() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},GALLERY);
            return;
        }
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }
    private void takePhotoFromCamera() {
        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},CAMERA);
            return;
        }
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    picture.setImageBitmap(bitmap);
                    saveImage(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(CreateMovieActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            saveImage(thumbnail);
            picture.setImageBitmap(thumbnail);
            Toast.makeText(CreateMovieActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }
    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + "/image");
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());
            mImageFile = f;
            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    public void createMovie(){
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), mImageFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("cover", mImageFile.getName(), reqFile);

// create a map of data to pass along
        RequestBody title = RequestBody.create(MediaType.parse("text/plain"), filmName.getText().toString());
        RequestBody genre = RequestBody.create(MediaType.parse("text/plain"),gerne.getSelectedItem().toString());
        RequestBody release = RequestBody.create(MediaType.parse("text/plain"), date.getText().toString());
        RequestBody descript = RequestBody.create(MediaType.parse("text/plain"), description.getText().toString());

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("title", title);
        map.put("genre", genre);
        map.put("release", release);
        map.put("description", descript);

        mAPIService = APIUtils.getAPIService();
        mAPIService.uploadFileWithPartMap( map, body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("onResponse", response.message() + "__" + response.toString());
                Toast.makeText(CreateMovieActivity.this,"Thành công!",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });



    }
}
