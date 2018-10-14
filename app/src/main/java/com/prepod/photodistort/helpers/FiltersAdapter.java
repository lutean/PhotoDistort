package com.prepod.photodistort.helpers;

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
import com.bumptech.glide.signature.ObjectKey;
import com.prepod.photodistort.R;
import com.prepod.photodistort.models.FilterItem;

import java.util.List;

public class FiltersAdapter extends RecyclerView.Adapter<FiltersAdapter.FiltersViewHolder> {

    private List<FilterItem> filterList;
    private RequestManager mGlide;

    public FiltersAdapter(Context context, List<FilterItem> filterList) {
        this.filterList = filterList;
        mGlide = Glide.with(context);

    }

    @NonNull
    @Override
    public FiltersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_filters_item, null, false);
        return new FiltersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FiltersViewHolder filtersViewHolder, int i) {
        mGlide.load(filterList.get(i).getImage()).apply(new RequestOptions().centerCrop()
                .signature(new ObjectKey(System.currentTimeMillis()))
                .override(256, 256)).into(filtersViewHolder.filterImage);
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    class FiltersViewHolder extends RecyclerView.ViewHolder{

        ImageView filterImage;

        public FiltersViewHolder(@NonNull View itemView) {
            super(itemView);
            filterImage = itemView.findViewById(R.id.image_filters);
        }
    }
}
