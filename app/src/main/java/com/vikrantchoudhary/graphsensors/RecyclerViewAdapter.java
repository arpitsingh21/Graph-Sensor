package com.vikrantchoudhary.graphsensors; /**
 * Created by db2admin on 8/2/2016.
 */

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikrantchoudhary.graphsensors.bean.SensorBean;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private List<SensorBean> sensorList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, subtitle;
        public ImageView iconSensor;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            subtitle = (TextView) view.findViewById(R.id.subTitle);
            iconSensor = (ImageView) view.findViewById(R.id.list_image);
        }
    }


    public RecyclerViewAdapter(List<SensorBean> list) {
        this.sensorList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SensorBean  movie = sensorList.get(position);
        holder.title.setText(movie.getTitle());
        holder.subtitle.setText(movie.getSubtitle());
        holder.iconSensor.setImageResource(movie.getPhoto());
    }

    @Override
    public int getItemCount() {
        return sensorList.size();
    }
}
