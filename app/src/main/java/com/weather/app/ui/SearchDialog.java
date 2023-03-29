package com.weather.app.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.weather.app.MainApplication;
import com.weather.app.ui.adapter.SearchAdapter;
import com.weather.app.view_model.SearchViewModel;
import com.weather.app.data.model.City;
import com.weather.app.data.model.Response;
import com.weather.app.databinding.DialogSearchBinding;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by ridhim on 19,March,2023
 */
public class SearchDialog extends BottomSheetDialogFragment implements SearchAdapter.SearchAdapterCallback {

    @Inject
    ViewModelProvider.Factory factory;
    private DialogSearchBinding binding;
    private SearchViewModel viewModel;
    private SearchAdapter adapter;
    private SearchDialogCallback listener;

    /**
     * The onAttach() method is called when the dialog is attached to the parent context.
     * This method is used to inject dependencies and set the SearchDialogCallback listener.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        injectDialog();
        super.onAttach(context);

        if (context instanceof SearchDialogCallback) {
            listener = (SearchDialogCallback) context;
        } else throw new RuntimeException("Should implement SearchDialogCallback");

    }

    /**
     * The onCreateDialog() method is called when the dialog is being created.
     * Here, the BottomSheetDialog is created and set to expanded state.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog =  (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        return dialog;
    }

    /**
     *  The onCreateView() method is called when the dialog's view is being created.
     *  Here, the binding is inflated, ViewModel, adapter and listeners are initialized
     *  and data is subscribed to.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogSearchBinding.inflate(inflater, container, false);

        initViewModel();
        initListeners();
        initAdapter();
        subscribeData();

        return binding.getRoot();
    }

    /**
     * The onDetach() method is called when the dialog is detached from the parent context.
     * This method is used to set the SearchDialogCallback listener to null.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


    /**
     * Initalization methods
     */
    //The injectDialog() method is used to inject dependencies into the SearchDialog.
    private void injectDialog() {
        MainApplication application = (MainApplication) getActivity().getApplication();
        application.appComponent.inject(this);
    }

    /**
     * The initViewModel() method is used to initialize the ViewModel.
     */
    private void initViewModel() {
        viewModel = new ViewModelProvider(this, factory).get(SearchViewModel.class);
    }

    /**
     * The initListeners() method is used to initialize the listeners.
     */
    private void initListeners() {
        // Set a TextWatcher on the search EditText to listen for changes in the query.
        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                viewModel.onSearchQueryChanged(editable.toString());
            }
        });
    }

    /**
     * The initAdapter() method is used to initialize the adapter.
     */
    private void initAdapter() {
        adapter = new SearchAdapter(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    /**
     * The subscribeData() method is used to subscribe to the data in the ViewModel.
     * Here, the response and navigation event LiveData are observed.
     */
    private void subscribeData() {
        viewModel.getResponse().observe(this, this::checkResponse);

        viewModel.getNavigationEvent().observe(this, city -> {
            listener.onCitySelected(city);
            dismiss();
        });
    }

    /**
     * Checks the response status and renders the appropriate UI state.
     *
     * @param response the response object containing the status and data/error.
     */
    private void checkResponse(Response<List<City>> response) {
        switch (response.status) {
            case LOADING:
                renderLoadingState();
                break;
            case SUCCESS:
                renderSuccessState(response.data);
                break;
            case ERROR:
                renderErrorState(response.error);
                break;
        }
    }

    /**
     * Shows the loading state UI.
     */
    private void renderLoadingState() {
        binding.recyclerView.setVisibility(View.GONE);
        binding.progress.setVisibility(View.VISIBLE);
    }

    /**
     * Shows the success state UI and sets the data on the adapter.
     *
     * @param items the list of data to be displayed.
     */
    private void renderSuccessState(List<City> items) {
        binding.recyclerView.setVisibility(View.VISIBLE);
        binding.progress.setVisibility(View.GONE);
        adapter.setItems(items);
    }

    /**
     * Shows the error state UI, logs the error and displays a snackbar.
     *
     * @param throwable the error that occurred.
     */
    private void renderErrorState(Throwable throwable) {
        binding.recyclerView.setVisibility(View.VISIBLE);
        binding.progress.setVisibility(View.GONE);
        Log.e(SearchDialog.class.getName(), throwable.getMessage());
        Snackbar.make(binding.getRoot(), "Can't load data", Snackbar.LENGTH_LONG).show();
    }

    /**
     * SearchAdapter callback
     */
    @Override
    public void onItemClick(City city) {
        viewModel.saveSelectedCity(city);
    }

    interface SearchDialogCallback {
        void onCitySelected(City city);
    }
}
