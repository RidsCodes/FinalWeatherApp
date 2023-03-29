package com.weather.app.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.weather.app.data.model.City;
import com.weather.app.databinding.ItemSearchBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ridhim on 19,March,2023
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private final List<City> items = new ArrayList<>();
    private final SearchAdapterCallback listener;

    public SearchAdapter(SearchAdapterCallback listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSearchBinding binding = ItemSearchBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        City item = items.get(position);
        String location = item.getName() + ", " + item.getCountry();

        holder.binding.locationTv.setText(location);
        holder.binding.getRoot().setOnClickListener(view -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setItems(List<City> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public interface SearchAdapterCallback {
        void onItemClick(City city);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemSearchBinding binding;

        public ViewHolder(@NonNull ItemSearchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
