package com.example.android.newsapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SourceActivity extends AppCompatActivity {

    private static final String baseUrl = "https://newsapi.org/v1/sources";
    RecyclerView sourceList ;
    List<SourceModel> sourceModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source);

        sourceList = (RecyclerView) findViewById(R.id.source_list);

        sourceModelList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Toast.makeText(SourceActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                    JSONObject parent = new JSONObject(response);
                    JSONArray sourceArray = parent.getJSONArray("sources");
                    for (int i = 0;i<parent.length();i++){
                        JSONObject singleSource = sourceArray.getJSONObject(i);
                        SourceModel sourceModel = new SourceModel(singleSource.getString("id"),
                                singleSource.getString("name"),singleSource.getString("description"),
                                singleSource.getString("category"),singleSource.getString("language"),
                                singleSource.getString("country"));
                        sourceModelList.add(sourceModel);
                    }
                    sourceList.setLayoutManager(new LinearLayoutManager(SourceActivity.this));
                    sourceList.setAdapter(new SourceAdapter(sourceModelList,SourceActivity.this));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SourceActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SourceActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public class SourceAdapter extends RecyclerView.Adapter<SourceHolder>{

        List<SourceModel> modelList;
        Context c;

        public SourceAdapter(List<SourceModel> modelList,Context c) {
            this.modelList = modelList;
            this.c = c;
        }

        @Override
        public SourceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_source_items,parent,false);
            return new SourceHolder(mView);
        }

        @Override
        public void onBindViewHolder(SourceHolder holder, int position) {
            SourceModel model = modelList.get(position);
            Toast.makeText(SourceActivity.this, "List Size ="+modelList.size(), Toast.LENGTH_SHORT).show();
            holder.title.setText(model.getName());
            holder.desc.setText(model.getDescription());
            holder.category.setText(model.getCategory());
            holder.country.setText(model.getCountry());
        }

        @Override
        public int getItemCount() {
            return sourceModelList.size();
        }
    }

    public class SourceHolder extends RecyclerView.ViewHolder{

        TextView title,desc,category,country;

        public SourceHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.source_title);
            desc = itemView.findViewById(R.id.source_description);
            category = itemView.findViewById(R.id.source_category);
            country = itemView.findViewById(R.id.source_country);
        }
    }
}
