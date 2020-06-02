package com.minor.unclutter.Adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.minor.unclutter.Model.MssageInfoModel;
import com.minor.unclutter.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    List<MssageInfoModel> mssageInfoModels;

    public RecyclerViewAdapter(List<MssageInfoModel> mssageInfoModels) {
        this.mssageInfoModels = mssageInfoModels;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.message_info_layout, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        myViewHolder.sender.setText(mssageInfoModels.get(i).getSender());
        final String s = mssageInfoModels.get(i).getMessage();
        s.replaceAll("\n", "");
        String m = "";
        if (s.length() > 85)
            m = s.substring(0, 82);
        myViewHolder.message.setText(m + "...");
        myViewHolder.day.setText(mssageInfoModels.get(i).getDay());
        myViewHolder.imageView.setText(mssageInfoModels.get(i).getSender());
        final String finalM = m;
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mssageInfoModels.get(i).isOpen()) {
                    myViewHolder.message.setMaxLines(2);
                    myViewHolder.message.setText(finalM + "...");
                    mssageInfoModels.get(i).setOpen(false);
                } else {
                    myViewHolder.message.setMaxLines(100);
                    myViewHolder.message.setText(s);
                    mssageInfoModels.get(i).setOpen(true);
                }

            }
        });
        myViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mssageInfoModels.remove(i);
                notifyDataSetChanged();
            }
        });
        switch (i % 5) {
            case 0:
                myViewHolder.imageView.setBackgroundColor(0xfff0e675);
                break;
            case 1:
                myViewHolder.imageView.setBackgroundColor(0xffee8f61);
                break;
            case 2:
                myViewHolder.imageView.setBackgroundColor(0xffbf406b);
                break;
            case 3:
                myViewHolder.imageView.setBackgroundColor(0xff692a70);
                break;
            case 4:
                myViewHolder.imageView.setBackgroundColor(0xff692a70);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mssageInfoModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView sender, message, day;
        TextView imageView;
        ImageView delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            sender = itemView.findViewById(R.id.sender);
            message = itemView.findViewById(R.id.message);
            day = itemView.findViewById(R.id.day);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
