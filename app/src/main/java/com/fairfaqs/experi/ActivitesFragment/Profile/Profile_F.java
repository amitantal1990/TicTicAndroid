package com.fairfaqs.experi.ActivitesFragment.Profile;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.fairfaqs.experi.ActivitesFragment.Accounts.Login_A;
import com.fairfaqs.experi.ApiClasses.ApiLinks;
import com.fairfaqs.experi.ApiClasses.ApiRequest;
import com.fairfaqs.experi.Interfaces.Callback;
import com.google.android.material.tabs.TabLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fairfaqs.experi.ActivitesFragment.Chat.Chat_Activity;
import com.fairfaqs.experi.ActivitesFragment.Following_F;
import com.fairfaqs.experi.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.fairfaqs.experi.ActivitesFragment.Profile.Liked_Videos.Liked_Video_F;
import com.fairfaqs.experi.ActivitesFragment.Profile.UserVideos.UserVideo_F;
import com.fairfaqs.experi.Models.PrivacyPolicySettingModel;
import com.fairfaqs.experi.Models.PushNotificationSettingModel;
import com.fairfaqs.experi.R;
import com.fairfaqs.experi.ActivitesFragment.See_Full_Image_F;
import com.fairfaqs.experi.Interfaces.API_CallBack;
import com.fairfaqs.experi.Interfaces.Fragment_Callback;
import com.fairfaqs.experi.SimpleClasses.Functions;
import com.fairfaqs.experi.SimpleClasses.Variables;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;


// This is the profile screen which is show in 5 tab as well as it is also call
// when we see the profile of other users

public class Profile_F extends RootFragment implements View.OnClickListener {

    View view;
    Context context;


    public TextView follow_unfollow_btn;
    // public  TextView username2_txt,video_count_txt;
    public TextView username2_txt;
    public ImageView imageView;
    public TextView follow_count_txt, fans_count_txt, heart_count_txt;

    ImageView back_btn, message_btn;

    String user_id, user_name, user_pic;

    Bundle bundle;

    protected TabLayout tabLayout;

    protected ViewPager pager;

    private ViewPagerAdapter adapter;

    public boolean isdataload = false;


    RelativeLayout tabs_main_layout;

    LinearLayout tab_praivacy_likes;
    ConstraintLayout top_layout;

    public String pic_url;


    public Profile_F() {

    }


    Fragment_Callback fragment_callback;

    @SuppressLint("ValidFragment")
    public Profile_F(Fragment_Callback fragment_callback) {
        this.fragment_callback = fragment_callback;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_profile, container, false);
        context = getContext();
        getActivity();
        bundle = getArguments();
        if (bundle != null) {
            user_id = bundle.getString("user_id");
            user_name = bundle.getString("user_name");
            user_pic = bundle.getString("user_pic");
        }


        return init();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_image:
                OpenfullsizeImage(pic_url);
                break;

            case R.id.follow_unfollow_btn:

                if (Functions.getSharedPreference(context).getBoolean(Variables.islogin, false))
                    if (follow_unfollow_btn.getText().toString().trim().equals("Following")) {
                        showUnfollowDialog();
                    } else {
                        Follow_unFollow_User();
                    }

                else {

                    Intent intent = new Intent(getActivity(), Login_A.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_top);
                }
                break;

            case R.id.message_btn:
                Open_Setting();
                break;

            case R.id.following_layout:
                Open_Following();
                break;

            case R.id.fans_layout:
                Open_Followers();
                break;

            case R.id.back_btn:
                getActivity().onBackPressed();
                break;
        }
    }

    private void showUnfollowDialog() {

        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.unfollow_dialog_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        TextView text =  dialog.findViewById(R.id.txtTitle);
        String txt1 = "Are you sure? You want to\n unfollow ";
        String txt2 = username2_txt.getText().toString();

        //String txtFinal =txt2;
        SpannableString txtSpannable = new SpannableString(txt2);
        SpannableStringBuilder builder=new SpannableStringBuilder();
        builder.append(txt1);
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        txtSpannable.setSpan(boldSpan, 0, txt2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(txtSpannable);
       // builder.append(string2);

        text.setText(builder, TextView.BufferType.SPANNABLE);

        Button dialogButton =dialog.findViewById(R.id.btnUnfollow);
        dialogButton.setOnClickListener(v -> {
            Follow_unFollow_User();
            dialog.dismiss();
        });


        TextView dialogButton2 =  dialog.findViewById(R.id.btnCancel);
        dialogButton2.setOnClickListener(v -> dialog.dismiss());

        dialog.show();


    }


    public View init() {

        //username=view.findViewById(R.id.username);
        username2_txt = view.findViewById(R.id.username2_txt);
        imageView = view.findViewById(R.id.user_image);
        imageView.setOnClickListener(this);
        tab_praivacy_likes = view.findViewById(R.id.tab_privacy_likes);
        //video_count_txt=view.findViewById(R.id.video_count_txt);

        follow_count_txt = view.findViewById(R.id.follow_count_txt);
        fans_count_txt = view.findViewById(R.id.fan_count_txt);
        heart_count_txt = view.findViewById(R.id.heart_count_txt);


        message_btn = view.findViewById(R.id.message_btn);
        message_btn.setOnClickListener(this);

        back_btn = view.findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);

        follow_unfollow_btn = view.findViewById(R.id.follow_unfollow_btn);
        follow_unfollow_btn.setOnClickListener(this);


        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        pager = view.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(2);


        tabs_main_layout = view.findViewById(R.id.tabs_main_layout);
        top_layout = view.findViewById(R.id.top_layout);


        ViewTreeObserver observer = top_layout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                final int height = top_layout.getMeasuredHeight();

                top_layout.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);

                ViewTreeObserver observer = tabs_main_layout.getViewTreeObserver();
                observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {

                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tabs_main_layout.getLayoutParams();
                        params.height = (int) (tabs_main_layout.getMeasuredHeight() + height);
                        tabs_main_layout.setLayoutParams(params);
                        tabs_main_layout.getViewTreeObserver().removeGlobalOnLayoutListener(
                                this);

                    }
                });

            }
        });


        view.findViewById(R.id.following_layout).setOnClickListener(this);
        view.findViewById(R.id.fans_layout).setOnClickListener(this);

        isdataload = true;


        if (user_id != null)
            setupTabIcons();


        Call_Api_For_get_Allvideos();


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        if (is_run_first_time) {

            Call_Api_For_get_Allvideos();

        }

    }

    private void setupTabIcons() {

        adapter = new ViewPagerAdapter(getResources(), getChildFragmentManager());
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);

        View view1 = LayoutInflater.from(context).inflate(R.layout.item_tabs_profile_menu, null);
        ImageView imageView1 = view1.findViewById(R.id.image);
        imageView1.setImageDrawable(getResources().getDrawable(R.drawable.ic_feed_discover_icon_new));
        tabLayout.getTabAt(0).setCustomView(view1);

        View view2 = LayoutInflater.from(context).inflate(R.layout.item_tabs_profile_menu, null);
        ImageView imageView2 = view2.findViewById(R.id.image);
        imageView2.setImageDrawable(getResources().getDrawable(R.drawable.ic_liked_video_gray));
        tabLayout.getTabAt(1).setCustomView(view2);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {


            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View v = tab.getCustomView();
                ImageView image = v.findViewById(R.id.image);

                switch (tab.getPosition()) {
                    case 0:

                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_feed_discover_icon_new));
                        break;

                    case 1:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart_hidden_sel_icon_new));
                        break;
                }
                tab.setCustomView(v);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View v = tab.getCustomView();
                ImageView image = v.findViewById(R.id.image);

                switch (tab.getPosition()) {
                    case 0:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_my_video_gray));
                        break;
                    case 1:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_liked_video_gray));
                        break;
                }

                tab.setCustomView(v);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });


    }


    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final Resources resources;

        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();


        public ViewPagerAdapter(final Resources resources, FragmentManager fm) {
            super(fm);
            this.resources = resources;
        }

        @Override
        public Fragment getItem(int position) {
            final Fragment result;
            switch (position) {
                case 0:
                    result = new UserVideo_F(false, user_id);
                    break;
                case 1:
                    result = new Liked_Video_F(false, user_id);
                    break;

                default:
                    result = null;
                    break;
            }

            return result;
        }

        @Override
        public int getCount() {
            return 2;
        }


        @Override
        public CharSequence getPageTitle(final int position) {
            return null;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }


        /**
         * Get the Fragment by position
         *
         * @param position tab position of the fragment
         * @return
         */
        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }


    }


    boolean is_run_first_time = false;

    private void Call_Api_For_get_Allvideos() {

        if (bundle == null) {
            user_id = Functions.getSharedPreference(context).getString(Variables.u_id, "0");
        }

        JSONObject parameters = new JSONObject();
        try {

            if (Functions.getSharedPreference(context).getBoolean(Variables.islogin, false) && user_id != null) {
                parameters.put("user_id", Functions.getSharedPreference(context).getString(Variables.u_id, ""));
                parameters.put("other_user_id", user_id);
            } else if (user_id != null) {
                parameters.put("user_id", user_id);
            } else {
                parameters.put("username", user_name);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        ApiRequest.Call_Api(getActivity(), ApiLinks.showUserDetail, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                is_run_first_time = true;
                Parse_data(resp);
            }
        });


    }

    public void Parse_data(String responce) {

        try {
            JSONObject jsonObject = new JSONObject(responce);
            String code = jsonObject.optString("code");
            if (code.equals("200")) {
                JSONObject msg = jsonObject.optJSONObject("msg");

                JSONObject User = msg.optJSONObject("User");
                JSONObject push_notification_setting = msg.optJSONObject("PushNotification");
                JSONObject privacy_policy_setting = msg.optJSONObject("PrivacySetting");

                if (user_id == null) {
                    user_id = User.optString("id");
                    setupTabIcons();
                }

                String first_name = User.optString("first_name", "");
                String last_name = User.optString("last_name", "");

//                if(first_name.equalsIgnoreCase("") && last_name.equalsIgnoreCase("")){
//                    username.setText(User.optString("username"));
//                }
//                else {
//                    username.setText(first_name + " " +last_name);
//                }

                username2_txt.setText(User.optString("username"));

                pic_url = User.optString("profile_pic");
                if (!pic_url.contains(Variables.http)) {
                    pic_url = ApiLinks.base_url + pic_url;
                }

                if (pic_url != null && !pic_url.equals(""))
                    Picasso.get()
                            .load(pic_url)
                            .placeholder(context.getResources().getDrawable(R.drawable.profile_image_placeholder))
                            .resize(200, 200).centerCrop().into(imageView);

                follow_count_txt.setText(User.optString("following_count"));
                fans_count_txt.setText(User.optString("followers_count"));
                heart_count_txt.setText(User.optString("likes_count"));

                PushNotificationSettingModel pushNotificationSetting_model = new PushNotificationSettingModel();
                pushNotificationSetting_model.setComments("" + push_notification_setting.optString("comments"));
                pushNotificationSetting_model.setLikes("" + push_notification_setting.optString("likes"));
                pushNotificationSetting_model.setNewfollowers("" + push_notification_setting.optString("new_followers"));
                pushNotificationSetting_model.setMentions("" + push_notification_setting.optString("mentions"));
                pushNotificationSetting_model.setDirectmessage("" + push_notification_setting.optString("direct_messages"));
                pushNotificationSetting_model.setVideoupdates("" + push_notification_setting.optString("video_updates"));


                PrivacyPolicySettingModel privacyPolicySetting_model = new PrivacyPolicySettingModel();
                privacyPolicySetting_model.setVideos_download("" + privacy_policy_setting.optString("videos_download"));
                privacyPolicySetting_model.setDirect_message("" + privacy_policy_setting.optString("direct_message"));
                privacyPolicySetting_model.setDuet("" + privacy_policy_setting.optString("duet"));
                privacyPolicySetting_model.setLiked_videos("" + privacy_policy_setting.optString("liked_videos"));
                privacyPolicySetting_model.setVideo_comment("" + privacy_policy_setting.optString("video_comment"));

                if (Functions.isShowContentPrivacy(privacyPolicySetting_model.getLiked_videos(),
                        privacy_policy_setting.optString("liked_videos").toLowerCase().equalsIgnoreCase("friends"))) {

                    tabLayout.getTabAt(1).view.setClickable(true);
                    tab_praivacy_likes.setVisibility(View.VISIBLE);
                } else {
                    tabLayout.getTabAt(1).view.setClickable(false);
                    tab_praivacy_likes.setVisibility(View.GONE);
                }


                if (Functions.isShowContentPrivacy(privacyPolicySetting_model.getDirect_message(),
                        User.optString("button").toLowerCase().equalsIgnoreCase("friends"))) {
                    message_btn.setVisibility(View.VISIBLE);
                } else {
                    message_btn.setVisibility(View.GONE);
                }


                String follow_status = User.optString("button");
                if (!User.optString("id").
                        equals(Functions.getSharedPreference(context).getString(Variables.u_id, ""))) {

                    follow_unfollow_btn.setVisibility(View.VISIBLE);

                    if (follow_status.equalsIgnoreCase("following")) {
                        //  follow_unfollow_btn.setText("UnFollow");
                        follow_unfollow_btn.setText("Following");
                        follow_unfollow_btn.setTextColor(getResources().getColor(R.color.darkgray));
                        follow_unfollow_btn.setBackground(getResources().getDrawable(R.drawable.rectangle_round));


                    } else if (follow_status.equalsIgnoreCase("friends")) {
                        follow_unfollow_btn.setVisibility(View.GONE);
                    } else {
                        follow_unfollow_btn.setText("Follow");
                        follow_unfollow_btn.setTextColor(getResources().getColor(R.color.white));
                        follow_unfollow_btn.setBackground(getResources().getDrawable(R.drawable.d_round_gray_background_2));

                    }


                }


                //video_count_txt.setText(User.optString("video_count")+" videos");
                String verified = User.optString("verified");
                if (verified != null && verified.equalsIgnoreCase("1")) {
                    view.findViewById(R.id.varified_btn).setVisibility(View.VISIBLE);
                }


            } else {
                Functions.show_toast(getActivity(), jsonObject.optString("msg"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void Open_Setting() {

        if (Functions.getSharedPreference(context).getBoolean(Variables.islogin, false)) {
            Open_Chat_F();
        } else {
            Intent intent = new Intent(getActivity(), Login_A.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_top);
        }

    }


    public void Follow_unFollow_User() {


        Functions.Call_Api_For_Follow_or_unFollow(getActivity(),
                Functions.getSharedPreference(context).getString(Variables.u_id, ""),
                user_id,
                new API_CallBack() {
                    @Override
                    public void ArrayData(ArrayList arrayList) {
                    }

                    @Override
                    public void OnSuccess(String responce) {

                        Call_Api_For_get_Allvideos();
                    }

                    @Override
                    public void OnFail(String responce) {

                    }

                });


    }


    //this method will get the big size of profile image.
    public void OpenfullsizeImage(String url) {
        See_Full_Image_F see_image_f = new See_Full_Image_F();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        Bundle args = new Bundle();
        args.putSerializable("image_url", url);
        see_image_f.setArguments(args);
        transaction.addToBackStack(null);

        View view = getActivity().findViewById(R.id.MainMenuFragment);
        if (view != null)
            transaction.replace(R.id.MainMenuFragment, see_image_f).commit();
        else
            transaction.replace(R.id.Profile_F, see_image_f).commit();


    }


    public void Open_Chat_F() {

        Chat_Activity chat_activity = new Chat_Activity(new Fragment_Callback() {
            @Override
            public void Responce(Bundle bundle) {

            }
        });
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        Bundle args = new Bundle();
        args.putString("user_id", user_id);
        args.putString("user_name", user_name);
        args.putString("user_pic", user_pic);
        chat_activity.setArguments(args);
        transaction.addToBackStack(null);

        View view = getActivity().findViewById(R.id.MainMenuFragment);
        if (view != null)
            transaction.replace(R.id.MainMenuFragment, chat_activity).commit();
        else
            transaction.replace(R.id.Profile_F, chat_activity).commit();

    }


    public void Open_Following() {

        Following_F following_f = new Following_F();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        Bundle args = new Bundle();
        args.putString("id", user_id);
        args.putString("from_where", "following");
        following_f.setArguments(args);
        transaction.addToBackStack(null);


        View view = getActivity().findViewById(R.id.MainMenuFragment);

        if (view != null)
            transaction.replace(R.id.MainMenuFragment, following_f).commit();
        else
            transaction.replace(R.id.Profile_F, following_f).commit();


    }

    public void Open_Followers() {

        Following_F following_f = new Following_F();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        Bundle args = new Bundle();
        args.putString("id", user_id);
        args.putString("from_where", "fan");
        following_f.setArguments(args);
        transaction.addToBackStack(null);


        View view = getActivity().findViewById(R.id.MainMenuFragment);

        if (view != null)
            transaction.replace(R.id.MainMenuFragment, following_f).commit();
        else
            transaction.replace(R.id.Profile_F, following_f).commit();


    }


    @Override
    public void onDetach() {
        super.onDetach();


        if (fragment_callback != null)
            fragment_callback.Responce(new Bundle());

        Functions.deleteCache(context);

    }


}
