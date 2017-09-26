package com.example.android.newsapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<ArticleModel> modelList;
    private static final String baseUrl =
            " https://newsapi.org/v1/articles?source=techcrunch&apiKey=9f5c862d1f4f4ef791ad40180322d26f";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.news_list);
        modelList = new ArrayList<>();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parent = new JSONObject(response);
                    JSONArray articleArray = parent.getJSONArray("articles");
                    for(int i=0;i<parent.length();i++){
                        JSONObject eachArticle = articleArray.getJSONObject(i);
                        ArticleModel model = new ArticleModel(eachArticle.getString("author"),
                                eachArticle.getString("title"),eachArticle.getString("description"),
                                eachArticle.getString("urlToImage"),eachArticle.getString("publishedAt"));
                        modelList.add(model);
                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    recyclerView.setAdapter(new NewsAdapter(modelList,MainActivity.this));

                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public class NewsAdapter extends RecyclerView.Adapter<NewsHolder>{

        private List<ArticleModel> list;
        private Context c;

        public NewsAdapter(List<ArticleModel> list, Context c) {
            this.list = list;
            this.c = c;
        }

        @Override
        public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_news_items,parent,false);
            return new NewsHolder(mView);
        }

        @Override
        public void onBindViewHolder(NewsHolder holder, int position) {
            ArticleModel model = list.get(position);

            holder.titleText.setText(model.getTitle());
            holder.descText.setText(model.getDescription());
            holder.aurthorText.setText(model.getAuthor());
            holder.timeText.setText(model.getPublishedAt());
            holder.setUrlToImage(model.getUrlToImage(),c);

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
    public class NewsHolder extends RecyclerView.ViewHolder{
        TextView titleText,descText,aurthorText,timeText;
        ImageView imageView;

        public NewsHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.news_title);
            descText = itemView.findViewById(R.id.news_description);
            aurthorText = itemView.findViewById(R.id.news_aurthor);
            timeText = itemView.findViewById(R.id.news_time);

        }

        void setUrlToImage(String urlToImage,Context c){
            if(urlToImage!=null){
                imageView = itemView.findViewById(R.id.news_image);
                Picasso.with(c).load(urlToImage).into(imageView);
            }
        }
    }
}
