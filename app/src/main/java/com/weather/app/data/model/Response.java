package com.weather.app.data.model;

import static com.weather.app.data.model.Status.ERROR;
import static com.weather.app.data.model.Status.LOADING;
import static com.weather.app.data.model.Status.SUCCESS;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by ridhim on 19,March,2023
 */
public class Response<T> {

    public final Status status;

    @Nullable
    public final T data;

    @Nullable
    public final Throwable error;

    private Response(Status status, @Nullable T data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static <T> Response<T> loading() {
        return new Response<>(LOADING, null, null);
    }

    public static <T> Response<T> success(@NonNull T data) {
        return new Response<>(SUCCESS, data, null);
    }

    public static <T> Response<T> error(@NonNull Throwable error) {
        return new Response<>(ERROR, null, error);
    }
}
