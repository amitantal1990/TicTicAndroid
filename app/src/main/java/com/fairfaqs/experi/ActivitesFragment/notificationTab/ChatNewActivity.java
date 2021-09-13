package com.fairfaqs.experi.ActivitesFragment.notificationTab;

import static com.fairfaqs.experi.Main_Menu.MainMenuActivity.intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import com.fairfaqs.experi.ActivitesFragment.Chat.Chat_Activity;
import com.fairfaqs.experi.Interfaces.Fragment_Callback;
import com.fairfaqs.experi.R;

public class ChatNewActivity extends AppCompatActivity {
ProgressBar progress_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_new);
        progress_bar=findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.VISIBLE);
        Bundle bundle = getIntent().getExtras();
        String user_id = bundle.getString("user_id");
        String user_name = bundle.getString("user_name");
        String user_pic = bundle.getString("user_pic");

        new Handler().postDelayed(() -> {
            Chat_Activity chat_activity = new Chat_Activity(bundle1 -> {

            });
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);

            Bundle args = new Bundle();
            args.putString("user_id",user_id);
            args.putString("user_name", user_name);
            args.putString("user_pic", user_pic);
            progress_bar.setVisibility(View.GONE);
            chat_activity.setArguments(args);
            transaction.addToBackStack(null);
            transaction.replace(R.id.MainMenuFragment, chat_activity).commit();
        },2000);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
