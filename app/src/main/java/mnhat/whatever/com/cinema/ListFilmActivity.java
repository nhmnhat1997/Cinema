package mnhat.whatever.com.cinema;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListFilmActivity extends AppCompatActivity {
    private FilmDataAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private APIService mService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_film);
        getSupportActionBar().setTitle("Danh sách phim");
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
