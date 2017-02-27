package com.brainstorm.hardik.allwishas.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.brainstorm.hardik.allwishas.R;
import com.brainstorm.hardik.allwishas.activity.MainActivity;
import com.brainstorm.hardik.allwishas.adapter.HomeAdapter;
import com.brainstorm.hardik.allwishas.constants.AppGlobal;
import com.brainstorm.hardik.allwishas.constants.WsConstant;
import com.brainstorm.hardik.allwishas.model.MainCategoriesData;
import com.brainstorm.hardik.allwishas.model.MainCategoriesRespones;
import com.brainstorm.hardik.allwishas.model.SubCategoriesData;
import com.brainstorm.hardik.allwishas.model.SubCategoriesRespones;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by user on 2/17/2017.
 */

public class HomeFragment extends Fragment {

    AdView adView;
    InterstitialAd mInterstitialAd;

    RecyclerView home_categories_list;
    TextView home_categories_list_not_found;

    HomeAdapter homeAdapter;

    public static ArrayList<MainCategoriesData> mainCategoriesDataArrayList = new ArrayList<MainCategoriesData>();
    public static ArrayList<SubCategoriesData> subCategoriesDataArrayList = new ArrayList<SubCategoriesData>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        //Automatic popping up keyboard on start Activity
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        //avoid automatically appear android keyboard when activity start
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        MainActivity.lblTitle.setText(""+"Home");
        MainActivity.menu.setVisibility(View.VISIBLE);
        MainActivity.back.setVisibility(View.GONE);
        MainActivity.home.setVisibility(View.GONE);
        MainActivity.lblTitle.setVisibility(View.VISIBLE);
        MainActivity.searchbar.setVisibility(View.GONE);
        MainActivity.search.setVisibility(View.VISIBLE);
        MainActivity.cross.setVisibility(View.GONE);
        MainActivity.blank.setVisibility(View.GONE);

        MainActivity.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.lblTitle.setVisibility(View.GONE);
                MainActivity.searchbar.setVisibility(View.VISIBLE);
                MainActivity.search.setVisibility(View.GONE);
                MainActivity.cross.setVisibility(View.VISIBLE);
            }
        });

        MainActivity.cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.lblTitle.setVisibility(View.VISIBLE);
                MainActivity.searchbar.setVisibility(View.GONE);
                MainActivity.search.setVisibility(View.VISIBLE);
                MainActivity.cross.setVisibility(View.GONE);
            }
        });

        MainActivity.searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //  adapter.getFilter().filter(s.toString());
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (MainActivity.searchbar.getText().length() > 0) {
                    if (subCategoriesDataArrayList.size() > 0) {

                        final List<SubCategoriesData> filteredModelList = filter(subCategoriesDataArrayList, s.toString());
                        homeAdapter.setFilter(filteredModelList);
                        home_categories_list.setVisibility(View.VISIBLE);
                        home_categories_list_not_found.setVisibility(View.GONE);

                    }
                    else {
                        home_categories_list.setVisibility(View.GONE);
                        home_categories_list_not_found.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    homeAdapter = new HomeAdapter(getContext(), HomeFragment.this, subCategoriesDataArrayList);
                    home_categories_list.setAdapter(homeAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /*MobileAds.initialize(getActivity().getApplicationContext(), "ca-app-pub-5640916454188115~2303788786");
        adView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);*/

       /* mInterstitialAd = new InterstitialAd(getActivity());
        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        adRequest = new AdRequest.Builder().build();
        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });*/

        MainCategories();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent =  new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"add what a subject you want");
                String shareMessage="message body";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                //shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/path-to-your-image.jpg"));
                startActivity(Intent.createChooser(shareIntent,"Sharing via"));

            }
        });

        home_categories_list = (RecyclerView) view.findViewById(R.id.home_categories_list);
        home_categories_list_not_found = (TextView) view.findViewById(R.id.home_categories_list_not_found);

        home_categories_list.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        home_categories_list.setLayoutManager(layoutManager);
        /*if (FindContactFragment.contactList.size() == 0) {
            creditcard_list.setVisibility(View.GONE);
            creditcard_list_not_found.setVisibility(View.VISIBLE);
        } else {
            creditcard_list.setVisibility(View.VISIBLE);
            creditcard_list_not_found.setVisibility(View.GONE);

            creditCardAdapter = new CreditCardAdapter(getContext(), CreditCardFragment.this, FindContactFragment.contactList);
            creditcard_list.setAdapter(creditCardAdapter);

        }*/
        /*homeAdapter = new HomeAdapter(getContext(), HomeFragment.this, subCategoriesDataArrayList);
        home_categories_list.setAdapter(homeAdapter);*/

        return  view;
    }

    private void MainCategories() {
        AppGlobal.showProgressDialog(getActivity());

        RequestParams params=new RequestParams();

        //params.put(Constants.json_data,String.valueOf(json));

        //Log.e("TAG",params.toString());
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(getActivity(), WsConstant.WS_ROOT + WsConstant.REQ_MAIN_CATEGORY, params, new AsyncHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
            {
                AppGlobal.dismissProgressDialog();

                String responseString = new String(responseBody);
                MainCategoriesRespones holder= new GsonBuilder().create().fromJson(responseString,MainCategoriesRespones.class);

                Log.e("Respones", responseString );

                if(holder.getStatus().equals("true"))
                {
                    mainCategoriesDataArrayList = holder.getRecord();
                    Log.e("TAG", "MainCategories Response Success..." );
                    Log.e("TAG MainCategories Size", String.valueOf(mainCategoriesDataArrayList.size()));

                    CallSubCategories();

                }
                else
                {
                    //MyUtils.ShowAlert(getActivity(),getResources().getString(R.string.text_alert),data.getMsg());
                    Toast.makeText(getActivity(),holder.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
            {
                AppGlobal.dismissProgressDialog();
                Log.e("TAG", "MainCategories Response Failure..." );
                //Toast.makeText(getActivity(),"Response Failure...", Toast.LENGTH_SHORT).show();
                ///MyUtils.ShowAlert(getActivity(),getResources().getString(R.string.text_alert),getResources().getString(R.string.error_server));
            }
        });

    }

    private void CallSubCategories() {
        AppGlobal.showProgressDialog(getActivity());

        RequestParams params=new RequestParams();
        params.put(WsConstant.category_id,"");
        Log.e("TAG SubCategories",params.toString());

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(getActivity(), WsConstant.WS_ROOT + WsConstant.REQ_SUB_CATEGORY, params, new AsyncHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
            {
                AppGlobal.dismissProgressDialog();

                String responseString = new String(responseBody);
                SubCategoriesRespones holder= new GsonBuilder().create().fromJson(responseString,SubCategoriesRespones.class);

                Log.e("Respones", responseString );

                if(holder.getStatus().equals("true"))
                {
                    subCategoriesDataArrayList = holder.getRecord();
                    Log.e("TAG", "SubCategories Response Success..." );
                    Log.e("TAG SubCategories Size", String.valueOf(subCategoriesDataArrayList.size()));

                    if (subCategoriesDataArrayList.size() == 0) {
                        home_categories_list.setVisibility(View.GONE);
                        home_categories_list_not_found.setVisibility(View.VISIBLE);
                    } else {
                        home_categories_list.setVisibility(View.VISIBLE);
                        home_categories_list_not_found.setVisibility(View.GONE);

                        homeAdapter = new HomeAdapter(getContext(), HomeFragment.this, subCategoriesDataArrayList);
                        home_categories_list.setAdapter(homeAdapter);

                    }

                }
                else
                {
                    //MyUtils.ShowAlert(getActivity(),getResources().getString(R.string.text_alert),data.getMsg());
                    Toast.makeText(getActivity(),holder.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
            {
                AppGlobal.dismissProgressDialog();
                Log.e("TAG", "SubCategories Response Failure..." );
                //Toast.makeText(getActivity(),"Response Failure...", Toast.LENGTH_SHORT).show();
                ///MyUtils.ShowAlert(getActivity(),getResources().getString(R.string.text_alert),getResources().getString(R.string.error_server));
            }
        });
    }

    private List<SubCategoriesData> filter(List<SubCategoriesData> models, String query) {
        query = query.toLowerCase();

        final List<SubCategoriesData> filteredModelList = new ArrayList<>();
        for ( SubCategoriesData model : models) {
            final String text = model.getSub_category_title().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        /*MobileAds.initialize(getActivity().getApplicationContext(), "ca-app-pub-5640916454188115~2303788786");
        adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);*/

    }

}
