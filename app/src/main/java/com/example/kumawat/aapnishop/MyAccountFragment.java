package com.example.kumawat.aapnishop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Kumawat on 4/21/2018.
 */

public class MyAccountFragment extends Fragment {
    TextView shopName, shopAddress;
    ImageView profile_photo;
    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_my_account, null);

        mToolbar = view.findViewById(R.id.toolbar_my_account);
        mToolbar.setTitle("Profile");
        ((MainActivity)getActivity()).setSupportActionBar(mToolbar);

        shopName = view.findViewById(R.id.shop_name);
        shopAddress = view.findViewById(R.id.shop_address);
        return view;
    }
}
