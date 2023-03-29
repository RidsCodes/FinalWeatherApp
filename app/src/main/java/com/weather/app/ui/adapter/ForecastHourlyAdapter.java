package com.weather.app.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.weather.app.data.model.Description;
import com.weather.app.data.model.Info;
import com.weather.app.data.model.Weather;
import com.weather.app.databinding.ItemHourlyForecastBinding;
import com.weather.app.utills.DataUtils;
import com.weather.app.utills.ImageUtils;

import java.util.List;

/**
 * Created by ridhim on 19,March,2023
 */
public class ForecastHourlyAdapter extends RecyclerView.Adapter<ForecastHourlyAdapter.ViewHolder> {

    private final List<Weather> items;

    public ForecastHourlyAdapter(List<Weather> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHourlyForecastBinding binding = ItemHourlyForecastBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Weather item = items.get(position);
        Description weatherDesc = item.getWeather().get(0);
        Info weatherInfo = item.getInfo();

        String time = DataUtils.getTime(item.getDate());
        String temp = DataUtils.formatKelvinToCelsius(weatherInfo.getTemp());

        holder.binding.timeTv.setText(time);
        holder.binding.tempTv.setText(temp);

        ImageUtils.loadWeatherIcon(holder.binding.iconIv, weatherDesc.getIcon());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemHourlyForecastBinding binding;

        public ViewHolder(@NonNull ItemHourlyForecastBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
