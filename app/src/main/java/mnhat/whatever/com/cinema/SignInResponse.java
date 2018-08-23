package mnhat.whatever.com.cinema;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignInResponse {
    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("user")
    @Expose
    private User user;

    class User{
        @SerializedName("_id")
        @Expose
        private String id;

        @SerializedName("email")
        @Expose
        private String email;

        @SerializedName("name")
        @Expose
        private String username;

        @SerializedName("avatarURL")
        @Expose
        private String avatar;

        @SerializedName("phone")
        @Expose
        private String phone;

        @SerializedName("provider")
        @Expose
        private String provider;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getProvider() {
            return provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }
    }

    @SerializedName("token")
    @Expose
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
