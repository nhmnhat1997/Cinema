package mnhat.whatever.com.cinema;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowProfileActivity extends AppCompatActivity {

    private UserFilmDataAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private APIService mService;
    private File mImageFile;

    public static final int CAMERA = 1;
    public static final int GALLERY = 0;


    TextView userName, email, phoneNum;
    LinearLayout editPhoneNum, editUsername, basicInfo, mail, phone, btn;
    Button changePassword, signOut;
    CircleImageView avatar;
    Animation up, down, left, right;
    Activity mActivity;
    SwipeRefreshLayout swipeRefreshLayout;

    String domain = "https://nam-cinema.herokuapp.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);

        mActivity = ShowProfileActivity.this;

        userName = (TextView) findViewById(R.id.tvUsername);
        email = (TextView) findViewById(R.id.tv_email);
        phoneNum = (TextView) findViewById(R.id.tv_phone);

        editUsername = (LinearLayout) findViewById(R.id.layout_changeName);
        editPhoneNum = (LinearLayout) findViewById(R.id.layout_changePhone);
        basicInfo = (LinearLayout) findViewById(R.id.layout_basicInfo);
        mail = (LinearLayout) findViewById(R.id.layout_mail);
        phone = (LinearLayout) findViewById(R.id.layout_phone);
        btn = (LinearLayout) findViewById(R.id.layout_button);

        changePassword = (Button) findViewById(R.id.btn_changePass);
        signOut = (Button) findViewById(R.id.btn_signOut);

        avatar = (CircleImageView) findViewById(R.id.imgv_avatar);

        mRecyclerView = (RecyclerView) findViewById(R.id.rvUserListFilm);

        up = AnimationUtils.loadAnimation(ShowProfileActivity.this, R.anim.up);
        down = AnimationUtils.loadAnimation(ShowProfileActivity.this, R.anim.down);
        left = AnimationUtils.loadAnimation(ShowProfileActivity.this, R.anim.left);
        right = AnimationUtils.loadAnimation(ShowProfileActivity.this, R.anim.right);

        avatar.setAnimation(down);
        basicInfo.setAnimation(down);
        mRecyclerView.setAnimation(left);
        phone.setAnimation(left);
        mail.setAnimation(left);
        basicInfo.setAnimation(down);

        SharedPreferences pre = getSharedPreferences("access_token", MODE_PRIVATE);

        mService = APIUtils.getAPIService();
        mAdapter = new UserFilmDataAdapter(this, new ArrayList<FilmData.Movie>(0), new UserFilmDataAdapter.PostItemListener() {

            @Override
            public void onPostClick(long id) {
                Toast.makeText(ShowProfileActivity.this, "Post id is" + id, Toast.LENGTH_SHORT).show();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ShowProfileActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        //RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
        //mRecyclerView.addItemDecoration(itemDecoration);

        loadProfile();
        loadList();

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        editUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = LayoutInflater.from(ShowProfileActivity.this).inflate(R.layout.dialog_change_username, null);
                final EditText edtU = v.findViewById(R.id.edt_username);
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ShowProfileActivity.this, R.style.AlertDialogCustom))
                        .setView(v)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).setCancelable(false);
                final AlertDialog dialog = builder.create();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                    }
                });
                edtU.setText(userName.getText());
                edtU.setSelection(edtU.getText().toString().length());
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (edtU.getText().toString().equals("")){
                                    Toast t = Toast.makeText(mActivity, "Vui lòng nhập username mới.", Toast.LENGTH_LONG);
                                    t.getView().setBackgroundColor(R.drawable.toast);
                                    t.show();
                                    return;
                                }
                                updateProfile(null,edtU,dialog);
                                userName.setText(edtU.getText().toString());
                            }
                        });
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(edtU, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
                dialog.show();
            }
        });

        editPhoneNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = LayoutInflater.from(ShowProfileActivity.this).inflate(R.layout.dialog_change_phone, null);
                final EditText edtPhone = v.findViewById(R.id.edt_phoneNum);
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ShowProfileActivity.this, R.style.AlertDialogCustom))
                        .setView(v)
                        .setPositiveButton("OK", null)
                        .setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).setCancelable(false);
                final AlertDialog dialog = builder.create();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                    }
                });
                edtPhone.setText(phoneNum.getText());
                edtPhone.setSelection(edtPhone.getText().toString().length());
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (edtPhone.getText().toString().equals("")){
                                    Toast t = Toast.makeText(mActivity, "Vui lòng nhập số điện thoại mới.", Toast.LENGTH_LONG);
                                    t.getView().setBackgroundColor(R.drawable.toast);
                                    t.show();
                                    return;
                                }
                                updateProfile(edtPhone,null,dialog);
                                phoneNum.setText(edtPhone.getText().toString());
                            }
                        });
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(edtPhone, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
                dialog.show();
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = LayoutInflater.from(ShowProfileActivity.this).inflate(R.layout.dialog_change_password, null);
                final EditText edtCurrPass = v.findViewById(R.id.edt_currentPass);
                final EditText edtNewPass1 = v.findViewById(R.id.edt_newPass);
                final EditText edtNewPass2 = v.findViewById(R.id.edt_newPass2);
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ShowProfileActivity.this, R.style.AlertDialogCustom))
                        .setView(v)
                        .setPositiveButton("OK", null)
                        .setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).setCancelable(false);
                final AlertDialog dialog = builder.create();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                    }
                });
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SharedPreferences pre_signIn = getSharedPreferences("signInLog", MODE_PRIVATE);
                                if (edtCurrPass.getText().toString().equals("")) {
                                    Toast t = Toast.makeText(mActivity, "Vui lòng nhập password hiện tại.", Toast.LENGTH_LONG);
                                    t.getView().setBackgroundColor(R.drawable.toast);
                                    t.show();
                                    return;
                                } else if (edtNewPass1.getText().toString().equals("")) {
                                    Toast t = Toast.makeText(mActivity, "Vui lòng nhập password mới.", Toast.LENGTH_LONG);
                                    t.getView().setBackgroundColor(R.drawable.toast);
                                    t.show();
                                    return;
                                } else if (edtNewPass2.getText().toString().equals("")) {
                                    Toast t = Toast.makeText(mActivity, "Vui lòng lại password mới.", Toast.LENGTH_LONG);
                                    t.getView().setBackgroundColor(R.drawable.toast);
                                    t.show();
                                    return;
                                } else if (!edtCurrPass.getText().toString().equals("") && !edtNewPass1.getText().toString().equals("") && !edtNewPass2.getText().toString().equals("")) {
                                    if (edtCurrPass.getText().toString().equals(edtNewPass1.getText().toString())) {
                                        Toast t = Toast.makeText(mActivity, "Password hiện tại và password mới giống nhau. Vui lòng kiểm tra lại", Toast.LENGTH_LONG);
                                        t.getView().setBackgroundColor(R.drawable.toast);
                                        t.show();
                                        return;
                                    }
                                    if (!edtNewPass1.getText().toString().equals(edtNewPass2.getText().toString())) {
                                        Toast t = Toast.makeText(mActivity, "2 Password mới khác nhau. Vui lòng kiểm tra lại", Toast.LENGTH_LONG);
                                        t.getView().setBackgroundColor(R.drawable.toast);
                                        t.show();
                                        return;
                                    }
                                }
                                changePass(edtCurrPass, edtNewPass1, dialog);

                            }
                        });
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(edtCurrPass, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
                dialog.show();

            }
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ShowProfileActivity.this, R.style.AlertDialogCustom))
                        .setMessage("Bạn có chắc chắn muốn đăng xuất ?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SharedPreferences pre = getSharedPreferences("access_token", MODE_PRIVATE);
                                SharedPreferences.Editor editor = pre.edit();
                                editor.clear();
                                editor.commit();
                                Intent intent = new Intent(ShowProfileActivity.this, SignInActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }).setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                    }
                });
                dialog.show();


            }
        });
        /*swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadProfile();
                loadList();
            }
        });*/
    }

    private void selectImage() {
        android.app.AlertDialog.Builder pictureDialog = new android.app.AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
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
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY);
            return;
        }
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA);
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
                    avatar.setImageBitmap(bitmap);
                    mImageFile = new File(getPath(this, contentURI));
                    updateAvatar(userName,phoneNum);
                    //saveImage(bitmap,requestCode);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast t = Toast.makeText(ShowProfileActivity.this, "Thất bại!", Toast.LENGTH_SHORT);
                    t.getView().setBackgroundColor(R.drawable.toast);
                    t.show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            saveImage(thumbnail, requestCode);
            avatar.setImageBitmap(thumbnail);
            updateAvatar(userName,phoneNum);
            Toast t = Toast.makeText(ShowProfileActivity.this, "Ảnh đã lưu!", Toast.LENGTH_SHORT);
            t.getView().setBackgroundColor(R.drawable.toast);
            t.show();
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
                final String[] selectionArgs = new String[]{
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
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
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

    public void loadList() {
        mService.getFilmData().enqueue(new Callback<FilmData>() {
            @Override
            public void onResponse(Call<FilmData> call, Response<FilmData> response) {
                if (response.isSuccessful()) {
                    List<FilmData.Movie> temp = response.body().getMovies();
                    Collections.reverse(temp);
                    mAdapter.updateData(temp);
                    //swipeRefreshLayout.setRefreshing(false);

                } else {
                    Toast.makeText(ShowProfileActivity.this, response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<FilmData> call, Throwable t) {
                t.printStackTrace();
                Log.d("List Film", "error loading from API");
            }
        });
    }

    public void changePass(EditText oldPass, EditText newPass, final AlertDialog dialog) {
        SharedPreferences pre = getSharedPreferences("access_token", MODE_PRIVATE);
        String token = pre.getString("token", "");
        final ProgressDialog loadDialog = new ProgressDialog(ShowProfileActivity.this, R.style.AlertDialogCustom);
        loadDialog.setMessage("Loading");
        loadDialog.show();
        mService.changePass(token, oldPass.getText().toString(), newPass.getText().toString()).enqueue(new Callback<Password>() {
            @Override
            public void onResponse(Call<Password> call, Response<Password> response) {
                if (response.isSuccessful()) {
                    //SharedPreferences pre_signIn = getSharedPreferences("signInLog",MODE_PRIVATE);
                    //SharedPreferences.Editor logIn = pre_signIn.edit();
                    Toast t = Toast.makeText(mActivity, "Đổi mật khẩu thành công", Toast.LENGTH_LONG);
                    t.getView().setBackgroundColor(R.drawable.toast);
                    t.show();
                    loadDialog.dismiss();
                    dialog.dismiss();
                } else {
                    Toast t = Toast.makeText(mActivity, "Mật khẩu hiện tại không chính xác. Vui lòng kiểm tra lại.", Toast.LENGTH_LONG);
                    t.getView().setBackgroundColor(R.drawable.toast);
                    t.show();
                    loadDialog.dismiss();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Password> call, Throwable t) {

            }
        });
    }

    public void loadProfile() {
        SharedPreferences pre = getSharedPreferences("access_token", MODE_PRIVATE);
        String token = pre.getString("token", "");
        mService.getProfileInfo(token).enqueue(new Callback<UserProfileData>() {
            @Override
            public void onResponse(Call<UserProfileData> call, Response<UserProfileData> response) {
                if (response.isSuccessful()) {
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.user);
                    userName.setText(response.body().getUser().getUsername());
                    email.setText(response.body().getUser().getEmail());
                    phoneNum.setText(response.body().getUser().getPhone());
                    Glide.with(mActivity).setDefaultRequestOptions(requestOptions).load(domain + response.body().getUser().getAvatar()).into(avatar);
                } else {
                    Toast.makeText(ShowProfileActivity.this, response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileData> call, Throwable t) {

            }
        });
    }

    public void updateProfile(EditText phoneNum, EditText userName, final AlertDialog dialog) {
        final ProgressDialog loadDialog = new ProgressDialog(ShowProfileActivity.this, R.style.AlertDialogCustom);
        loadDialog.setMessage("Loading");
        loadDialog.show();
        HashMap<String, RequestBody> map = new HashMap<>();
        if (userName != null) {
            RequestBody username = RequestBody.create(MediaType.parse("text/plain"), userName.getText().toString());
            map.put("username", username);
        } else {
            RequestBody username = RequestBody.create(MediaType.parse("text/plain"), "");
            map.put("username", username);
        }

        if (phoneNum != null) {
            RequestBody phone = RequestBody.create(MediaType.parse("text/plain"), phoneNum.getText().toString());
            map.put("phone", phone);
        } else {
            RequestBody phone = RequestBody.create(MediaType.parse("text/plain"), "");
            map.put("phone", phone);
        }


        SharedPreferences pre = getSharedPreferences("access_token", MODE_PRIVATE);
        String token = pre.getString("token", "");
        mService.updateProfile(token, map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.e("onResponse", response.message() + "__" + response.toString());
                    Toast t = Toast.makeText(ShowProfileActivity.this, "Thành công!", Toast.LENGTH_LONG);
                    t.getView().setBackgroundColor(R.drawable.toast);
                    t.show();
                    loadDialog.dismiss();
                    dialog.dismiss();
                } else {
                    Toast t = Toast.makeText(ShowProfileActivity.this, response.message(), Toast.LENGTH_LONG);
                    t.getView().setBackgroundColor(R.drawable.toast);
                    t.show();
                    loadDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

        public void updateAvatar(TextView userName, TextView phoneNum){
            final ProgressDialog loadDialog = new ProgressDialog(ShowProfileActivity.this,R.style.AlertDialogCustom);
            loadDialog.setMessage("Loading");
            loadDialog.show();
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), mImageFile);
            MultipartBody.Part body = MultipartBody.Part.createFormData("cover", mImageFile.getName(), reqFile);

            RequestBody username = RequestBody.create(MediaType.parse("text/plain"), userName.getText().toString());
            RequestBody phone = RequestBody.create(MediaType.parse("text/plain"),phoneNum.getText().toString());


            HashMap<String, RequestBody> map = new HashMap<>();
            map.put("username", username);
            map.put("phone", phone);
            SharedPreferences pre = getSharedPreferences("access_token",MODE_PRIVATE);
            String token = pre.getString("token","");
            mService.uploadFileWithPartMap(token ,map, body).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()) {
                        Log.e("onResponse", response.message() + "__" + response.toString());
                        Toast t = Toast.makeText(ShowProfileActivity.this, "Thành công!", Toast.LENGTH_LONG);
                        t.getView().setBackgroundColor(R.drawable.toast);
                        t.show();
                        loadDialog.dismiss();
                        mActivity.finish();
                    }
                    else{
                        Toast t = Toast.makeText(ShowProfileActivity.this, response.message(), Toast.LENGTH_LONG);
                        t.getView().setBackgroundColor(R.drawable.toast);
                        t.show();
                        loadDialog.dismiss();
                        return;
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });




        //x-access-token
    }
}
