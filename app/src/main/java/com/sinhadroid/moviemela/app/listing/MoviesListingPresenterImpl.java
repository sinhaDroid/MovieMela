package com.sinhadroid.moviemela.app.listing;

import com.sinhadroid.moviemela.app.Movie;
import com.sinhadroid.moviemela.app.util.RxUtils;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author arun
 */
class MoviesListingPresenterImpl implements MoviesListingPresenter
{
    private MoviesListingView view;
    private MoviesListingInteractor moviesInteractor;
    private Subscription fetchSubscription;

    MoviesListingPresenterImpl(MoviesListingInteractor interactor)
    {
        moviesInteractor = interactor;
    }

    @Override
    public void setView(MoviesListingView view)
    {
        this.view = view;
        displayMovies();
    }

    @Override
    public void destroy()
    {
        view = null;
        RxUtils.unsubscribe(fetchSubscription);
    }

    @Override
    public void displayMovies()
    {
        showLoading();
        fetchSubscription = moviesInteractor.fetchMovies().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onMovieFetchSuccess, this::onMovieFetchFailed);
    }

    private void showLoading()
    {
        if (isViewAttached())
        {
            view.loadingStarted();
        }
    }

    private void onMovieFetchSuccess(List<Movie> movies)
    {
        if (isViewAttached())
        {
            view.showMovies(movies);
        }
    }

    private void onMovieFetchFailed(Throwable e)
    {
        view.loadingFailed(e.getMessage());
    }

    private boolean isViewAttached()
    {
        return view != null;
    }
}
