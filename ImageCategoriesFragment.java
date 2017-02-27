package com.brainstorm.hardik.allwishas.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brainstorm.hardik.allwishas.R;
import com.brainstorm.hardik.allwishas.activity.MainActivity;
import com.brainstorm.hardik.allwishas.adapter.HomeAdapter;
import com.brainstorm.hardik.allwishas.adapter.ImageCategoriesAdapter;

import static com.brainstorm.hardik.allwishas.adapter.HomeAdapter.imageDataArrayList;

/**
 * Created by user on 2/17/2017.
 */

public class ImageCategoriesFragment extends Fragment {

    LinearLayout text;
    RecyclerView image_categories_list;
    TextView image_categories_list_not_found;

    ImageCategoriesAdapter imageCategoriesAdapter;

    String sub_category_id,category_id,sub_category_title,cover_img;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.image_categories_fragment, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            sub_category_id = getArguments().getString("sub_category_id");
            category_id = getArguments().getString("category_id");
            sub_category_title = getArguments().getString("sub_category_title");
            cover_img = getArguments().getString("cover_img");
        }
        Log.e("TAG Bundle", sub_category_id + "" + sub_category_title + "" + category_id + "" + cover_img);

        MainActivity.lblTitle.setText(""+sub_category_title);
        MainActivity.menu.setVisibility(View.GONE);
        MainActivity.back.setVisibility(View.GONE);
        MainActivity.home.setVisibility(View.VISIBLE);
        MainActivity.lblTitle.setVisibility(View.VISIBLE);
        MainActivity.searchbar.setVisibility(View.GONE);
        MainActivity.search.setVisibility(View.GONE);
        MainActivity.cross.setVisibility(View.GONE);
        MainActivity.blank.setVisibility(View.VISIBLE);

        text = (LinearLayout) view.findViewById(R.id.text);

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new TextCategoriesFragment();
                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });

        image_categories_list = (RecyclerView) view.findViewById(R.id.image_categories_list);
        image_categories_list_not_found = (TextView) view.findViewById(R.id.image_categories_list_not_found);

        image_categories_list.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        image_categories_list.setLayoutManager(layoutManager);
        if (imageDataArrayList.size() == 0) {
            image_categories_list.setVisibility(View.GONE);
            image_categories_list_not_found.setVisibility(View.VISIBLE);
        } else {
            image_categories_list.setVisibility(View.VISIBLE);
            image_categories_list_not_found.setVisibility(View.GONE);

            imageCategoriesAdapter = new ImageCategoriesAdapter(getContext(), ImageCategoriesFragment.this, imageDataArrayList);
            image_categories_list.setAdapter(imageCategoriesAdapter);

        }
       /* imageCategoriesAdapter = new ImageCategoriesAdapter(getContext());
        image_categories_list.setAdapter(imageCategoriesAdapter);*/

        Log.e("TAG Image array",imageDataArrayList.toString());

        return  view;
    }

}
