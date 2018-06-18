package mnhat.whatever.com.cinema;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface APIService {
    @Multipart
    @POST("/api/v1/movies/")
    Call<ResponseBody> uploadFileWithPartMap(
            @Header("x-access-token") String token,
            @PartMap() Map<String, RequestBody> partMap,
            @Part MultipartBody.Part file);


    @GET("/api/v1/movies/")
    Call<FilmData> getFilmData();

    @POST("/api/v1/auth/sign-up/")
    Call<SignUpData> signUpInfo(@Body SignUpData data);

    @POST("/api/v1/auth/sign-in/")
    @FormUrlEncoded
    Call<SignInResponse> singIn(@Field("email") String email,
                                @Field("password") String password);

    @GET("/api/v1/users/")
    Call<UserProfileData> getProfileInfo(@Header("x-access-token") String token);

    @POST("/api/v1/auth/change-password/")
    @FormUrlEncoded
    Call<Password> changePass(@Header("x-access-token") String token,
                              @Field("oldPassword") String oldPassword,
                              @Field("password") String password);

}
