package mnhat.whatever.com.cinema;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Password {
    @SerializedName("oldPassword")
    @Expose
    private String oldPassword;

    @SerializedName("password")
    @Expose
    private String password;

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
