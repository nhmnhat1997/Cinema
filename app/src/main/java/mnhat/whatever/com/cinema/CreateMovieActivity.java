package mnhat.whatever.com.cinema;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
    TextView tFilmName,tGenre,tDate,tDescription;
    ImageView picture;
    Bitmap oriPic, newPic;
    String userChoosenTask;
    Animation up, down, left, right;

    public static final int CAMERA = 1;
    public static final int GALLERY = 0;

    Activity mActivity;

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

        mActivity = this;

        filmName = (EditText) findViewById(R.id.edtFilmName);
        date = (EditText) findViewById(R.id.edtReleaseDate);
        description = (EditText) findViewById(R.id.edtDescription);
        gerne = (Spinner) findViewById(R.id.spGerne);
        choosePicture = (Button) findViewById(R.id.btnChoosePicture);
        post = (Button) findViewById(R.id.post);
        picture = (ImageView) findViewById(R.id.filmPicture);
        tFilmName = (TextView) findViewById(R.id.tvwFilmName);
        tGenre = (TextView) findViewById(R.id.tvwGerne);
        tDate = (TextView) findViewById(R.id.tvwReleaseDate);
        tDescription = (TextView) findViewById(R.id.tvwDescription);

        up = AnimationUtils.loadAnimation(CreateMovieActivity.this,R.anim.up);
        down = AnimationUtils.loadAnimation(CreateMovieActivity.this,R.anim.down);
        left = AnimationUtils.loadAnimation(CreateMovieActivity.this,R.anim.left);
        right = AnimationUtils.loadAnimation(CreateMovieActivity.this,R.anim.right);

        picture.setAnimation(down);
        choosePicture.setAnimation(down);
        post.setAnimation(up);
        tFilmName.setAnimation(left);
        tGenre.setAnimation(left);
        tDate.setAnimation(left);
        tDescription.setAnimation(left);
        filmName.setAnimation(right);
        gerne.setAnimation(right);
        date.setAnimation(right);
        description.setAnimation(right);


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

        choosePicture.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        view.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:{
                        selectImage();
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


        post.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        view.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:{
                        newPic = ((BitmapDrawable)picture.getDrawable()).getBitmap();
                        if (oriPic.sameAs(newPic)){
                            Toast.makeText(CreateMovieActivity.this,"Hãy chọn ảnh cho phim",Toast.LENGTH_SHORT).show();
                            view.getBackground().clearColorFilter();
                            view.invalidate();
                            break;
                        }

                        if (util.checkFilmValidate(filmName,date) == 1){
                            Toast.makeText(CreateMovieActivity.this,"Hãy nhập tên phim",Toast.LENGTH_SHORT).show();
                            view.getBackground().clearColorFilter();
                            view.invalidate();
                            break;
                        }
                        if (util.checkFilmValidate(filmName,date) == 2){
                            Toast.makeText(CreateMovieActivity.this,"Nhập thời gian đúng định dạng.",Toast.LENGTH_SHORT).show();
                            view.getBackground().clearColorFilter();
                            view.invalidate();
                            break;
                        }
                        if (util.checkFilmValidate(filmName,date) == 0){
                            createMovie();
                        }
                    }
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
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},CAMERA);
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
                    mImageFile = new File(getPath(this,contentURI));
                    //saveImage(bitmap,requestCode);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(CreateMovieActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            saveImage(thumbnail,requestCode);
            picture.setImageBitmap(thumbnail);
            Toast.makeText(CreateMovieActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }
    public String saveImage(Bitmap myBitmap, int requestCode) {
        if (requestCode == CAMERA) {
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
        }
        return "";
    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public void createMovie(){
        final ProgressDialog loadDialog = new ProgressDialog(CreateMovieActivity.this,R.style.AlertDialogCustom);
        loadDialog.setMessage("Loading");
        loadDialog.show();
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
        SharedPreferences pre = getSharedPreferences("access_token",MODE_PRIVATE);
        String token = pre.getString("token","");
        mAPIService = APIUtils.getAPIService();
        mAPIService.uploadFileWithPartMap(token ,map, body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Log.e("onResponse", response.message() + "__" + response.toString());
                    Toast.makeText(CreateMovieActivity.this, "Thành công!", Toast.LENGTH_LONG).show();
                    loadDialog.dismiss();
                    mActivity.finish();
                }
                else{
                    Toast.makeText(CreateMovieActivity.this, response.message(), Toast.LENGTH_LONG).show();
                    loadDialog.dismiss();
                    return;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });



    }
}
