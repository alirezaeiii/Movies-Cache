package com.zattoo.movies.ui.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import com.zattoo.movies.MoviesApp;
import com.zattoo.movies.R;
import com.zattoo.movies.utils.DataUtilsKt;
import com.zattoo.movies.utils.NetworkUtils;
import com.zattoo.movies.data.home.Movie;
import com.zattoo.movies.data.MovieListEntity;
import com.zattoo.movies.data.MovieListOffers;
import com.zattoo.movies.data.MovieService;
import com.zattoo.movies.databinding.FragmentHomeBinding;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;

public class HomeFragment extends Fragment {

    final static int ANIMATION_DURATION = 1000;

    @Inject
    HomeAdapter adapter;
    @Inject
    NetworkUtils networkUtils;
    @Inject
    MovieService movieService;

    private FragmentHomeBinding binding;
    private Result.Success success;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MoviesApp) getContext().getApplicationContext()).getApplicationComponent()
                .inject(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUiElements();
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchData();
        handleNetwork();
    }

    private void initUiElements() {
        adapter = new HomeAdapter();
        binding.recyclerView.setAdapter(adapter);
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchData();
        });
    }

    private void handleLoading() {
        showLoading(true);
    }

    private void handleResults(Result.Success result) {
        showLoading(false);
        if (result.movies.isEmpty()) {
            handleError();
        } else {
            this.success = result;
            adapter.setList(this);
        }
    }

    private void handleError(final String error) {
        handleError(false, error);
    }

    private void handleError() {
        handleError(true, "");
    }

    private void handleError(final Boolean isEmptyList, final String error) {
        showLoading(false);
        if (isEmptyList) {
            final String errorMessage = getString(R.string.empty_list);
            showEmptyList(errorMessage);
        } else {
            showError(error);
        }
    }

    private void showEmptyList(String message) {
        binding.recyclerView.setVisibility(View.GONE);
        binding.emptyView.setVisibility(View.VISIBLE);
        binding.emptyView.setText(message);
    }

    private void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    private void showLoading(boolean isLoading) {
        binding.swipeRefreshLayout.setRefreshing(isLoading);
    }

    private void handleNetwork() {
        networkUtils.getNetworkLiveData().observe(this, isConnected -> {
            if (!isConnected) {
                binding.textViewNetworkStatus.setText(getString(R.string.text_no_connectivity));
                binding.networkStatusLayout.setVisibility(View.VISIBLE);
                binding.networkStatusLayout.setBackgroundColor(ResourcesCompat.getColor(
                        getResources(),
                        R.color.colorStatusNotConnected,
                        null
                ));
                binding.swipeRefreshLayout.setRefreshing(false);
            } else {
                fetchData();
                binding.textViewNetworkStatus.setText(getString(R.string.text_connectivity));
                binding.networkStatusLayout.setBackgroundColor(ResourcesCompat.getColor(
                        getResources(), R.color.colorStatusConnected, null
                ));

                binding.networkStatusLayout.animate().alpha(1f)
                        .setStartDelay(ANIMATION_DURATION)
                        .setDuration(ANIMATION_DURATION)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                binding.networkStatusLayout.setVisibility(View.GONE);
                            }
                        });
            }
        });
    }

    private void fetchData() {
        new FetchDataAsyncTask().execute();
    }

    List<Movie> getMovies() {
        return success.movies;
    }

    class FetchDataAsyncTask extends AsyncTask<Void, Void, Result> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            handleLoading();
        }

        @Override
        protected Result doInBackground(Void... voids) {
            try {
                Response<MovieListEntity> responseMovieDataList = DataUtilsKt.fetchMovieList(movieService);
                Response<MovieListOffers> responseMovieListOffers = DataUtilsKt.fetchMovieListOffers(movieService);
                if (responseMovieDataList.isSuccessful() && responseMovieListOffers.isSuccessful()) {
                    return new Result.Success(DataUtilsKt.createMovies(
                            responseMovieDataList.body().getMovie_data(),
                            responseMovieListOffers.body()));
                } else {
                    return new Result.Failure("fetchMovieList or fetchMovieListOffers failed");
                }

            } catch (Exception exception) {
                return new Result.Failure(exception.getMessage());
            }
        }

        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            if (result instanceof Result.Success) {
                final Result.Success success = (Result.Success) result;
                handleResults(success);
            } else if (result instanceof Result.Failure) {
                final Result.Failure failure = (Result.Failure) result;
                handleError(failure.message);
            }
        }
    }
}
