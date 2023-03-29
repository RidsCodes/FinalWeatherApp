package com.weather.app.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.weather.app.data.model.DailyForecast;
import com.weather.app.data.model.Description;
import com.weather.app.data.model.Info;
import com.weather.app.data.model.Weather;
import com.weather.app.databinding.ItemForecastBinding;
import com.weather.app.utills.DataUtils;
import com.weather.app.utills.ImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ridhim on 12,March,2023
 */
public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {

    private final ArrayList<DailyForecast> items = new ArrayList<>();
    private final Context context;

    public ForecastAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemForecastBinding binding = ItemForecastBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DailyForecast item = items.get(position);
        Description weatherDesc = item.getWeather().get(0);
        Info weatherInfo = item.getInfo();

        String date = DataUtils.getDate(item.getDate());
        String minTemp = DataUtils.formatKelvinToCelsius(weatherInfo.getTempMin());
        String maxTemp = DataUtils.formatKelvinToCelsius(weatherInfo.getTempMax());
        String humidity = DataUtils.formatHumidity(weatherInfo.getHumidity());
        String wind = item.getWind().getSpeed().toString();

        holder.binding.dateTv.setText(date);
        holder.binding.shortDeskTv.setText(weatherDesc.getDescription());
        holder.binding.minTempTv.setText(minTemp);
        holder.binding.maxTempTv.setText(maxTemp);
        holder.binding.humidityTv.setText(humidity);
        holder.binding.windTv.setText(wind);

        ImageUtils.loadWeatherIcon(holder.binding.iconIv, weatherDesc.getIcon());

        bindHourlyForecast(holder.binding.recyclerView, item.getHourlyForecast());

        holder.binding.getRoot().setOnClickListener(view -> {
            holder.binding.additionalGroup.setVisibility(
                    holder.binding.additionalGroup.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE
            );
        });
    }

    private void bindHourlyForecast(RecyclerView recycler, List<Weather> items) {
        recycler.setAdapter(new ForecastHourlyAdapter(items));
        recycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recycler.setHasFixedSize(true);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setItems(List<DailyForecast> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemForecastBinding binding;

        public ViewHolder(@NonNull ItemForecastBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
