package com.fairfaqs.experi.ActivitesFragment.notificationTab;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.fairfaqs.experi.ActivitesFragment.Profile.Liked_Videos.Liked_Video_F;
import com.fairfaqs.experi.ActivitesFragment.Profile.Profile_F;
import com.fairfaqs.experi.ActivitesFragment.Profile.UserVideos.UserVideo_F;
import com.fairfaqs.experi.ActivitesFragment.Search.Search_HashTags_F;
import com.fairfaqs.experi.ActivitesFragment.Search.Search_Sound_F;
import com.fairfaqs.experi.ActivitesFragment.Search.Search_User_F;
import com.fairfaqs.experi.ActivitesFragment.Search.Search_Video_F;
import com.fairfaqs.experi.ActivitesFragment.WatchVideos_F;
import com.fairfaqs.experi.Adapters.Notification_Adapter;
import com.fairfaqs.experi.Adapters.ViewPagerAdapter;
import com.fairfaqs.experi.ApiClasses.ApiLinks;
import com.fairfaqs.experi.ApiClasses.ApiRequest;
import com.fairfaqs.experi.Interfaces.Callback;
import com.fairfaqs.experi.Main_Menu.MainMenuFragment;
import com.fairfaqs.experi.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.fairfaqs.experi.Models.Notification_Get_Set;
import com.fairfaqs.experi.R;
import com.fairfaqs.experi.SimpleClasses.Functions;
import com.fairfaqs.experi.SimpleClasses.Variables;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Notification_F_New extends RootFragment  {

    View view;
    Context context;


    public Notification_F_New() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notification_new, container, false);
        context = getContext();
     //   Set_tabs();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Set_tabs();
    }



    protected TabLayout tabLayout;
    protected ViewPager menu_pager;
    ViewPagerAdapter adapter;
    public void Set_tabs(){

        adapter = new ViewPagerAdapter(getChildFragmentManager());
        menu_pager = view.findViewById(R.id.viewpager);
        menu_pager.setOffscreenPageLimit(2);
        tabLayout = view.findViewById(R.id.tabs);

        adapter.addFrag( new Notification_F(),"Notification");
        adapter.addFrag(new Inbox_F(),"Messages");
        menu_pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(menu_pager);

    }





}
