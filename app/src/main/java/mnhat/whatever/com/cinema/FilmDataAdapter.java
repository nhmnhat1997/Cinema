package mnhat.whatever.com.cinema;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class FilmDataAdapter extends RecyclerView.Adapter<FilmDataAdapter.ViewHolder> {
    private List<FilmData.Movie> mItems;
    private Context mContext;
    private PostItemListener mItemListener;
    String domain = "https://nam-cinema.herokuapp.com/";


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView titleTv;
        PostItemListener mItemListener;
        TextView txvName,txvGerne,txvDate;
        ImageView cover;


        public ViewHolder(View itemView, PostItemListener postItemListener) {
            super(itemView);
            txvName = (TextView) itemView.findViewById(R.id.tv_title);
            txvGerne = (TextView) itemView.findViewById(R.id.tv_genre);
            txvDate = (TextView) itemView.findViewById(R.id.tv_release);
            cover = (ImageView) itemView.findViewById(R.id.img_cover);


            this.mItemListener = postItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            FilmData.Movie item = getItem(getAdapterPosition());

            notifyDataSetChanged();
        }
    }

    public FilmDataAdapter(Context context, List<FilmData.Movie> data, PostItemListener itemListener) {
        mItems = data;
        mContext = context;
        mItemListener = itemListener;
    }

    @Override
    public FilmDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_film, parent, false);

        ViewHolder viewHolder = new ViewHolder(postView, this.mItemListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FilmDataAdapter.ViewHolder holder, int position) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.choose_film);

        FilmData.Movie item = mItems.get(position);
        TextView name = holder.txvName;
        TextView gerne = holder.txvGerne;
        TextView date = holder.txvDate;
        ImageView cover = holder.cover;
        Glide.with(mContext).setDefaultRequestOptions(requestOptions).load(domain + item.getCover()).into(cover);
        name.setText(item.getTitle());
        gerne.setText(item.getGenre());
        date.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateData(List<FilmData.Movie> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    public void clear(List<FilmData.Movie> items) {
        items.clear();
        notifyDataSetChanged();
    }



    private FilmData.Movie getItem(int adapterPosition) {
        return mItems.get(adapterPosition);
    }

    public interface PostItemListener {
        void onPostClick(long id);
    }
}


