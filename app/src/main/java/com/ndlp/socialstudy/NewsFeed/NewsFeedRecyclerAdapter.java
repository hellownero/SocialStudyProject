package com.ndlp.socialstudy.NewsFeed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ndlp.socialstudy.R;
import com.ndlp.socialstudy.Umfragen.AktuelleUmfragenAnzeigen.BasicUmfragenRecyclerAdapter;

import java.util.ArrayList;

/**
 * Created by ndlp on 04.02.2018.
 */

public class NewsFeedRecyclerAdapter extends RecyclerView.Adapter<NewsFeedRecyclerAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<NewsFeedObject> newsFeedObjectArrayList;

    public NewsFeedRecyclerAdapter(Context context, ArrayList<NewsFeedObject> newsFeedObjectArrayList){
        this.context = context;
        this.newsFeedObjectArrayList = newsFeedObjectArrayList;
    }

    public void setContext(Context context){
        this.context = context;
    }

    public void setNewsFeedList(ArrayList<NewsFeedObject> newsFeedObjectArrayList){
        this.newsFeedObjectArrayList = newsFeedObjectArrayList;
    }

    @Override
    public NewsFeedRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.umfrage_item, null);

        return new NewsFeedRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NewsFeedRecyclerAdapter.MyViewHolder holder, int position) {

        NewsFeedObject currentObject = newsFeedObjectArrayList.get(position);

        String user = currentObject.getUser();
        String category = currentObject.getCategory();
        String uploaddate = currentObject.getUploaddate();
        String uploadtime = currentObject.getUploadtime();
        String topic = currentObject.getTopic();



        holder.tv_header.setText(user + ", am: " + uploaddate + ", " + uploadtime);
        holder.tv_topic.setText(currentObject.getTopic());

        holder.tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You clicked on more", Toast.LENGTH_LONG).show();
            }
        });

        holder.iv_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You clicked on Options", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsFeedObjectArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_topic;
        ImageView iv_options;
        TextView tv_header;
        TextView tv_more;
        ImageView iv_image;


        public MyViewHolder(View itemView) {
            super(itemView);

            tv_header = (TextView) itemView.findViewById(R.id.tv_umfrageitemheader);
            iv_options = (ImageView) itemView.findViewById(R.id.umfrageitemoptions);
            tv_topic = (TextView) itemView.findViewById(R.id.tv_umfrageitemtopic);
            tv_more = (TextView) itemView.findViewById(R.id.tv_umfrageitemmore);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_umfragenImage);

        }

    }
}
