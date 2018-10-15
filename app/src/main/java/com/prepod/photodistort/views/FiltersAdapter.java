package com.prepod.photodistort.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    private OnFilterClickListener onFilterClickListener;
    public FiltersAdapter(Context context, List<FilterItem> filterList, OnFilterClickListener onFilterClickListener) {
        this.filterList = filterList;
        mGlide = Glide.with(context);
        this.onFilterClickListener = onFilterClickListener;
    }

    @NonNull
    @Override
    public FiltersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_image_item, null, false);
        return new FiltersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FiltersViewHolder filtersViewHolder, int i) {
        filtersViewHolder.filterTitle.setText(filterList.get(i).getTitle());
        mGlide.load(filterList.get(i).getImage()).apply(new RequestOptions().centerCrop()
                .signature(new ObjectKey(System.currentTimeMillis()))
                .override(256, 256)).into(filtersViewHolder.filterImage);
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public interface OnFilterClickListener {
        void onFilterClick(int pos);
    }

    class FiltersViewHolder extends RecyclerView.ViewHolder {

        ImageView filterImage;
        TextView filterTitle;

        public FiltersViewHolder(@NonNull View itemView) {
            super(itemView);
            filterImage = itemView.findViewById(R.id.image_item);
            filterTitle = itemView.findViewById(R.id.text_filters);
            filterTitle.setVisibility(View.VISIBLE);
            filterImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onFilterClickListener.onFilterClick(getAdapterPosition());
                }
            });
        }
    }
}
