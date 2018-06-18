package mnhat.whatever.com.cinema;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.content.SharedPreferences;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListFilmActivity extends AppCompatActivity {
    private FilmDataAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private APIService mService;
    Utility util = new Utility();
    String token;

    FloatingActionButton addFilm, userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_film);
        getSupportActionBar().setTitle("Danh sách phim");

        SharedPreferences pre = getSharedPreferences("access_token",MODE_PRIVATE);
        if (pre.getBoolean("isLogin",false) == false)
        {
            Intent intent = new Intent(ListFilmActivity.this, SignInActivity.class);
            startActivity(intent);
        }

        addFilm = (FloatingActionButton) findViewById(R.id.fab_add_film);
        userProfile = (FloatingActionButton) findViewById(R.id.fab_user);
        mRecyclerView = (RecyclerView) findViewById(R.id.rvListFilm);
        mService = APIUtils.getAPIService();
        mAdapter = new FilmDataAdapter(this, new ArrayList<FilmData.Movie>(0), new FilmDataAdapter.PostItemListener() {

            @Override
            public void onPostClick(long id) {
                Toast.makeText(ListFilmActivity.this, "Post id is" + id, Toast.LENGTH_SHORT).show();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        loadList();

        addFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListFilmActivity.this, CreateMovieActivity.class);
                startActivity(intent);
            }
        });
        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 {
                    Intent intent = new Intent(ListFilmActivity.this, ShowProfileActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void loadList(){
        mService.getFilmData().enqueue(new Callback<FilmData>() {
            @Override
            public void onResponse(Call<FilmData> call, Response<FilmData> response) {
                if (response.isSuccessful()){
                    mAdapter.updateData(response.body().getMovies());


                }
            }

            @Override
            public void onFailure(Call<FilmData> call, Throwable t) {
                t.printStackTrace();
                Log.d("List Film", "error loading from API");
            }
        });
    }
}
