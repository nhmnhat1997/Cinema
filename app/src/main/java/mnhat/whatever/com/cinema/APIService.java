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
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface APIService {
    @Multipart
    @POST("/api/cinema/")
    Call<ResponseBody> uploadFileWithPartMap(
            @Header("x-access-token") String token,
            @PartMap() Map<String, RequestBody> partMap,
            @Part MultipartBody.Part file);


    @GET("/api/cinema/")
    Call<FilmData> getFilmData();

    @POST("/api/auth/signup/")
    Call<SignUpData> signUpInfo(@Body SignUpData data);

    @POST("/api/auth/signin/")
    @FormUrlEncoded
    Call<SignInResponse> singIn(@Field("email") String email,
                                @Field("password") String password);

    @GET("/api/user/{id}")
    Call<UserProfileData> getProfileInfo(@Header("x-access-token") String token, @Path("id") String creatorId);

    @PUT("/api/user/{id}/change-password/")
    @FormUrlEncoded
    Call<Password> changePass(@Header("x-access-token") String token,
                              @Path("id") String userId,
                              @Field("currentPass") String oldPassword,
                              @Field("newPass") String password);

    @Multipart
    @PUT("/api/v1/users/")
    Call<ResponseBody> updateFullProfile(
            @Header("x-access-token") String token,
            @PartMap() Map<String, RequestBody> partMap,
            @Part MultipartBody.Part file);

    @Multipart
    @PUT("/api/user/{id}/")
    Call<ResponseBody> updateProfile(
            @Header("x-access-token") String token,
            @Path("id") String userId,
            @PartMap() Map<String, RequestBody> partMap);

    @Multipart
    @PUT("/api/user/{id}/change-avatar/")
    Call<ResponseBody> updateAvatar(
            @Header("x-access-token") String token,
            @Path("id") String userId,
            @PartMap() Map<String, RequestBody> partMap,
            @Part MultipartBody.Part file);

    @POST("/api/auth/reset-password")
    @FormUrlEncoded
    Call<ResetPassData> resetPass(@Field("email") String email);
}
