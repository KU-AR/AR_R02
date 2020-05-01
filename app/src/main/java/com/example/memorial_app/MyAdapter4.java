package com.example.memorial_app;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.util.Base64DataException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Base64;
import java.util.List;


public class MyAdapter4 extends RecyclerView.Adapter<MyAdapter4.ViewHolder> {

    //private onItemClickListener listener;

    private List<Integer> iId;
    private List<String> iNickname;
    private List<Integer> iAge;
    private List<String> iJob;
    private List<String> iMemory;
    private List<String> iTime;
    private List<String> iEmotion;
    private List<String> iImages;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder{

        // each data item is just a string in this case
        ImageView imageView;
        TextView textView;
        TextView captionView;

        ViewHolder(View v) {
            super(v);
            imageView = v.findViewById(R.id.image_view);
            textView = v.findViewById(R.id.text_view);
            captionView = v.findViewById(R.id.caption_view);
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    MyAdapter4(List<Integer> itemId, List<String> itemNickNames, List<Integer> itemAge, List<String> itemJob, List<String> itemMemory, List<String> itemTime, List<String> itemEmotion, List<String> itemImages) {
        this.iId = itemId;
        this.iNickname = itemNickNames;
        this.iAge = itemAge;
        this.iJob = itemJob;
        this.iMemory = itemMemory;
        this.iTime = itemTime;
        this.iEmotion = itemEmotion;
        this.iImages = itemImages;
    }

/*
    public interface onItemClickListener{
        void onClick(View view, int id);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        this.listener = listener;
    }
*/

    // Create new views (invoked by the layout manager)
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view3, parent, false);

        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Bitmap bitmapImage = null;
        //decodeする

        //説明文サイズ
        holder.captionView.setTextSize(10);

        try{
            //byte[] byteImage = Base64.getDecoder().decode(this.iImages.get(position - 1));
            byte[] byteImage = android.util.Base64.decode(this.iImages.get(position), android.util.Base64.DEFAULT);
            bitmapImage = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
        }
        catch(Exception e){
            System.out.println(e);
        }
        holder.imageView.setImageBitmap(bitmapImage);
        holder.textView.setText(iNickname.get(position));
        holder.captionView.setText(iMemory.get(position));
/*        holder.imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                listener.onClick(view, iId.get(position));
            }
        });
        holder.textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                listener.onClick(view, iId.get(position));
            }
        });
        holder.captionView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                listener.onClick(view, iId.get(position));
            }
        });*/
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return iId.size();
    }

    public void refreshAdapter(List<Integer> itemId, List<String> itemNickNames, List<Integer> itemAge, List<String> itemJob, List<String> itemMemory, List<String> itemTime, List<String> itemEmotion, List<String> itemImages){
        this.iId.clear();
        this.iNickname.clear();
        this.iAge.clear();
        this.iJob.clear();
        this.iMemory.clear();
        this.iTime.clear();
        this.iEmotion.clear();
        this.iImages.clear();
        this.iId = itemId;
        this.iNickname = itemNickNames;
        this.iAge = itemAge;
        this.iJob = itemJob;
        this.iMemory = itemMemory;
        this.iTime = itemTime;
        this.iEmotion = itemEmotion;
        this.iImages = itemImages;
    }
}