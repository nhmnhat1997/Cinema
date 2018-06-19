package mnhat.whatever.com.cinema;

import com.google.gson.annotations.SerializedName;

public class ResetPassData {
    @SerializedName("email")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
