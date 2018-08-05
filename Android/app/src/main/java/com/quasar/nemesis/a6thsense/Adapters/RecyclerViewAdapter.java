package com.quasar.nemesis.a6thsense.Adapters;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.quasar.nemesis.a6thsense.R;

import java.util.ArrayList;

/**
 * Created by vipla on 19-03-2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>  {
    public ArrayList<String> myValues;
    public Bitmap[] myImages;
    public RecyclerViewAdapter (ArrayList<String> myValues,Bitmap[] myImages){
        this.myValues= myValues;
        this.myImages=myImages;
    }
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.MyViewHolder holder, int position) {
       String[] stamp=myValues.get(position).split(" ");
        holder.date.setText("Date:"+stamp[0]);
        holder.time.setText("Time:"+stamp[1]);
        holder.image.setImageBitmap(myImages[position]);

    }

    @Override
    public int getItemCount() {
        return myValues.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView date;
        private TextView time;
        private ImageView image;
        public MyViewHolder(View itemView) {
            super(itemView);
            date = (TextView)itemView.findViewById(R.id.date);
            time=(TextView)itemView.findViewById(R.id.time);
            image=(ImageView)itemView.findViewById(R.id.photo);
        }
    }
}
