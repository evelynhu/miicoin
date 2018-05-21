package io.mii.coin.features.main;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.mii.coin.R;
import io.mii.coin.data.local.PreferencesHelper;
import io.mii.coin.data.model.present.CryptoSummary;
import io.mii.coin.features.base.BaseFragment;
import io.mii.coin.features.common.ErrorView;
import io.mii.coin.features.detail.DetailActivity;
import io.mii.coin.injection.component.FragmentComponent;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class FavoriteFragment extends BaseFragment implements MainCryptoView, ErrorView.ErrorListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private io.mii.coin.features.main.FavoriteFragment.OnFragmentInteractionListener mListener;


    private static final int CRYPTO_LIMIT = 20;

    @Inject
    MainCryptoAdapter favoriteCryptoAdapter;
    @Inject
    MainPresenter favoritePresenter;

    @BindView(R.id.view_error)
    ErrorView errorView;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    @BindView(R.id.recycler_crypto)
    RecyclerView cryptoRecycler;

    @BindView(R.id.swipe_to_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    PreferencesHelper mPreferencesHelper;


    public FavoriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriteFragment newInstance(String param1, String param2) {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        mPreferencesHelper = new PreferencesHelper(getActivity().getApplicationContext());
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.primary);
        swipeRefreshLayout.setColorSchemeResources(R.color.white);
        swipeRefreshLayout.setOnRefreshListener(() -> fetchData());

        cryptoRecycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        cryptoRecycler.setAdapter(favoriteCryptoAdapter);

        errorView.setErrorListener(this);

        fetchData();
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cryptoClicked();
    }

    private void cryptoClicked() {
        Disposable disposable =
                favoriteCryptoAdapter
                        .getCryptoClick()
                        .subscribe(
                                crypto ->
                                        startActivity(DetailActivity.getStartIntent(getActivity().getApplicationContext(), crypto.name, crypto.id)),
                                throwable -> {
                                    Timber.e(throwable, "Crypto click failed");
                                    Toast.makeText(
                                            getActivity(),
                                            R.string.error_something_bad_happened,
                                            Toast.LENGTH_LONG)
                                            .show();
                                });
        favoritePresenter.addDisposable(disposable);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_favorite;
    }

    @Override
    protected void inject(FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    protected void attachView() {
        favoritePresenter.attachView(this);
    }

    @Override
    protected void detachPresenter() {
        favoritePresenter.detachView();
    }

    @Override
    public void showCryptoSummary(List<CryptoSummary> crypto) {
        favoriteCryptoAdapter.setCrypto(crypto);
        cryptoRecycler.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProgress(boolean show) {
        if (show) {
            if (cryptoRecycler.getVisibility() == View.VISIBLE
                    && favoriteCryptoAdapter.getItemCount() > 0) {
                swipeRefreshLayout.setRefreshing(true);
            } else {
                progressBar.setVisibility(View.VISIBLE);

                cryptoRecycler.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.GONE);
            }

            errorView.setVisibility(View.GONE);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showError(Throwable error) {
        cryptoRecycler.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        Timber.e(error, "There was an error retrieving the crypto");
    }

    @Override
    public void onReloadData() {
        fetchData();
    }

    private void fetchData() {
        favoritePresenter.getFavorites(CRYPTO_LIMIT, mPreferencesHelper);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFavoriteFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FavoriteFragment.OnFragmentInteractionListener) {
            mListener = (FavoriteFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFavoriteFragmentInteraction(Uri uri);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            fetchData();
        }
    }
}
