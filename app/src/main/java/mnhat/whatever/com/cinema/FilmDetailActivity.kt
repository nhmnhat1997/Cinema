package mnhat.whatever.com.cinema

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_film_detail.*
import mnhat.whatever.com.cinema.R.id.*
import java.util.*

class FilmDetailActivity : AppCompatActivity() {

    var gson: Gson = Gson()
    val domain: String = "https://nam-cinema.herokuapp.com/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_detail)

        supportActionBar!!.setTitle("Chi tiết phim")

        var data: String = intent.getStringExtra("data")
        var movie: FilmData.Movie = gson.fromJson(data,FilmData.Movie::class.java)
        tv_Title.text = movie.title
        tv_Genre.text = movie.genre
        tv_ReleaseDate.text = movie.date
        tv_Descript.text = movie.description
        tv_CreateAt.text = movie.createdAt
        var req: RequestOptions = RequestOptions()
        req.placeholder(R.drawable.choose_film)
        Glide.with(this).setDefaultRequestOptions(req).load(domain + movie.cover).into(img_cover)
        tv_user.text = movie.creator.username


    }
}
