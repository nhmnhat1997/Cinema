package mnhat.whatever.com.cinema

import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_film_detail.*
import mnhat.whatever.com.cinema.R.id.*
import retrofit2.Call
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.security.auth.callback.Callback

class FilmDetailActivity : AppCompatActivity() {
    var df: DateFormat = SimpleDateFormat("dd/MM/yyyy")
    var mService: APIService = APIUtils.getAPIService()
    var gson: Gson = Gson()
    val domain: String = "https://cinema-web-training.herokuapp.com/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_detail)

        supportActionBar!!.setTitle("Chi tiết phim")
        var pre: SharedPreferences = getSharedPreferences("access_token", MODE_PRIVATE)

        var data: String = intent.getStringExtra("data")
        var movie: FilmData.Movie = gson.fromJson(data,FilmData.Movie::class.java)
        tv_Title.text = movie.title
        tv_Genre.text = movie.genre
        tv_ReleaseDate.text = df.format(movie.date)
        tv_Descript.text = movie.description
        tv_CreateAt.text = df.format(movie.createdAt)
        var req: RequestOptions = RequestOptions()
        req.placeholder(R.drawable.choose_film)
        Glide.with(this).setDefaultRequestOptions(req).load(domain + movie.cover).into(img_cover)
        val token = pre.getString("token", "")
        if (movie.creatorId.equals("")){
            tv_user.visibility = View.GONE
        }
        else {
            mService.getProfileInfo(token, movie.creatorId).enqueue(object : retrofit2.Callback<UserProfileData> {
                override fun onFailure(call: Call<UserProfileData>?, t: Throwable?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onResponse(call: Call<UserProfileData>?, response: Response<UserProfileData>?) {
                    tv_user.text = response!!.body()!!.user.username
                }

            })
        }


    }
}
