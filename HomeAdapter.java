package com.brainstorm.hardik.allwishas.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brainstorm.hardik.allwishas.R;
import com.brainstorm.hardik.allwishas.activity.MainActivity;
import com.brainstorm.hardik.allwishas.constants.AppGlobal;
import com.brainstorm.hardik.allwishas.constants.WsConstant;
import com.brainstorm.hardik.allwishas.fragment.ImageCategoriesFragment;
import com.brainstorm.hardik.allwishas.model.AllDataRespones;
import com.brainstorm.hardik.allwishas.model.ImageData;
import com.brainstorm.hardik.allwishas.model.SubCategoriesData;
import com.brainstorm.hardik.allwishas.model.TextData;
import com.google.android.gms.vision.text.Text;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by user on 2/17/2017.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    Context context;
    Fragment fragment;

    ArrayList<SubCategoriesData> subCategoriesDataArrayList = new ArrayList<SubCategoriesData>();

    public static ArrayList<TextData> textDataArrayList = new ArrayList<TextData>();
    public static ArrayList<ImageData> imageDataArrayList = new ArrayList<ImageData>();

    public HomeAdapter(Context context, Fragment fragment, ArrayList<SubCategoriesData> subCategoriesDataArrayList) {
        this.context = context;
        this.fragment = fragment;
        this.subCategoriesDataArrayList = subCategoriesDataArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_fragment, parent, false);

        return new ViewHolder(itemView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout select_category;
        TextView sub_category_title;
        ImageView cover_image;

        public ViewHolder(View itemView) {
            super(itemView);

            select_category = (LinearLayout) itemView.findViewById(R.id.select_category);
            sub_category_title = (TextView) itemView.findViewById(R.id.sub_category_title);
            cover_image = (ImageView) itemView.findViewById(R.id.cover_image);

        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.sub_category_title.setText(subCategoriesDataArrayList.get(position).getSub_category_title());

        Picasso.with(context.getApplicationContext()).load(subCategoriesDataArrayList.get(position).getCover_img()).into(holder.cover_image);

        holder.select_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CallAllData(subCategoriesDataArrayList.get(position).getSub_category_id());

            }
        });

    }

    private void CallAllData(String sub_category_id) {

        AppGlobal.showProgressDialog(context);

        RequestParams params=new RequestParams();

        int position = 0;
        params.put(WsConstant.sub_category_id,sub_category_id);

        Log.e("TAG",params.toString());
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(context, WsConstant.WS_ROOT + WsConstant.REQ_ALL_DATA, params, new AsyncHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
            {
                AppGlobal.dismissProgressDialog();

                String responseString = new String(responseBody);
                AllDataRespones holder= new GsonBuilder().create().fromJson(responseString,AllDataRespones.class);

                Log.e("Respones", responseString );

                if(holder.getStatus().equals("true"))
                {
                    textDataArrayList = holder.getText();
                    imageDataArrayList = holder.getImage();
                    Log.e("TAG", "Sub__Categories Response Success..." );
                    Log.e("TAG Sub__Categories text Size", String.valueOf(textDataArrayList.size()));
                    Log.e("TAG Sub__Categories image Size", String.valueOf(imageDataArrayList.size()));

                    Fragment fragment = new ImageCategoriesFragment();
                    Bundle bundle = new Bundle();
                    //AppConstant.cardid = viewCardDataArrayList.get(position).getCard_id();
                    int position = 0;
                    bundle.putString("sub_category_id",subCategoriesDataArrayList.get(position).getSub_category_id());
                    bundle.putString("category_id",subCategoriesDataArrayList.get(position).getCategory_id());
                    bundle.putString("sub_category_title",subCategoriesDataArrayList.get(position).getSub_category_title());
                    bundle.putString("cover_img",subCategoriesDataArrayList.get(position).getCover_img());
                    fragment.setArguments(bundle);
                    ((MainActivity) context).switchFragment(fragment);

                }
                else
                {
                    //MyUtils.ShowAlert(getActivity(),getResources().getString(R.string.text_alert),data.getMsg());
                    Toast.makeText(context,holder.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
            {
                AppGlobal.dismissProgressDialog();
                Log.e("TAG", "Sub__Categories Response Failure..." );
                //Toast.makeText(getActivity(),"Response Failure...", Toast.LENGTH_SHORT).show();
                ///MyUtils.ShowAlert(getActivity(),getResources().getString(R.string.text_alert),getResources().getString(R.string.error_server));
            }
        });

    }

    @Override
    public int getItemCount() {
        return subCategoriesDataArrayList.size();
    }

    public void setFilter(List<SubCategoriesData> filteredModelList) {
        subCategoriesDataArrayList = new ArrayList<>();
        subCategoriesDataArrayList.addAll(filteredModelList);
        notifyDataSetChanged();
    }

}
