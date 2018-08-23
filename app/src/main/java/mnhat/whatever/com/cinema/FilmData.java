package mnhat.whatever.com.cinema;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FilmData {
    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("films")
    private ArrayList<Movie> movies;

    public class Movie {
        @SerializedName("_id")
        private String id;

        @SerializedName("name")
        private String title;

        @SerializedName("genre")
        private String genre;

        @SerializedName("releaseDate")
        private long date;

        @SerializedName("content")
        private String description;

        @SerializedName("posterURL")
        private String cover;

        @SerializedName("creatorId")
        private String creatorId;

        @SerializedName("createdDate")
        private long createdAt;

        @SerializedName("__v")
        private int v;

        public String getCreatorId() {
            return creatorId;
        }

        public void setCreatorId(String creatorId) {
            this.creatorId = creatorId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getGenre() {
            return genre;
        }

        public void setGenre(String genre) {
            this.genre = genre;
        }

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public long getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(long createdAt) {
            this.createdAt = createdAt;
        }

        public int getV() {
            return v;
        }

        public void setV(int v) {
            this.v = v;
        }

        public Movie clone(){
            Movie p = new Movie();
            p.setId(this.id);
            p.setTitle(this.title);
            p.setGenre(this.genre);
            p.setDate(this.date);
            p.setDescription(this.description);
            p.setCover(this.cover);
            p.setCreatedAt(this.createdAt);
            p.setV(this.v);
            p.setCreatorId(this.creatorId);
            return p;
        }
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

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }

}
