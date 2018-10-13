package com.prepod.photodistort;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.List;

/**
 * Created by Anton on 13.10.2018.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.RecyclerViewHolder>  {

    interface GalleryInteractionListener{
        void onImageClick(ImageItem imageItem);
    }

    private List<ImageItem> imageItems;
    private GalleryInteractionListener galleryInteractionListener;
    private RequestManager mGlide;

    public GalleryAdapter(Context context, List<ImageItem> imageItems, GalleryInteractionListener galleryInteractionListener) {
        this.imageItems = imageItems;
        this.galleryInteractionListener = galleryInteractionListener;
        mGlide = Glide.with(context);
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_item, null, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int i) {
        File file = new File(imageItems.get(i).getImageThumbPath());
        mGlide.load(file).apply(new RequestOptions().centerCrop()
                .override(256, 256)).into(recyclerViewHolder.cardImage);
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
                    galleryInteractionListener.onImageClick(imageItems.get(getAdapterPosition()));
                }
            });
        }
    }
}
