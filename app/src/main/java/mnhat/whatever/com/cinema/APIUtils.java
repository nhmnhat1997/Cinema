package mnhat.whatever.com.cinema;

public class APIUtils {
    private APIUtils(){}

    public static final String BASE_URL = "https://nam-cinema.herokuapp.com/";

    public static APIService getAPIService() {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
