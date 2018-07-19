package com.example.tmh.asyntask;

import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHoder> {
    private List<String> mListImage;

    public Adapter(List<String> mListImage) {
        this.mListImage = mListImage;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHoder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycleview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        holder.mImage.setImageBitmap(BitmapFactory.decodeFile(mListImage.get(position)));
    }

    @Override
    public int getItemCount() {
        return mListImage.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        private ImageView mImage;

        public ViewHoder(View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.image_itemView);
        }
    }
}
