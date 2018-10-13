package com.prepod.photodistort;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anton on 13.10.2018.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.RecyclerViewHolder>  {

    interface GalleryInteractionListener{
        void onImageClick(int position);
    }

    private List<ImageItem> imageItems = new ArrayList<>();
    private GalleryInteractionListener galleryInteractionListener;

    public GalleryAdapter(List<ImageItem> imageItems, GalleryInteractionListener galleryInteractionListener) {
        this.imageItems = imageItems;
        this.galleryInteractionListener = galleryInteractionListener;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_item, null, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int i) {
        recyclerViewHolder.cardImage.setImageURI(imageItems.get(i).getImageUri());
    }

    @Override
    public int getItemCount() {
        return imageItems.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder{

        public ImageView cardImage;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            cardImage = itemView.findViewById(R.id.image_item);
            cardImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    galleryInteractionListener.onImageClick(getAdapterPosition());
                }
            });
        }
    }
}
