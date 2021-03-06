package com.fairfaqs.experi.ActivitesFragment.Chat.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fairfaqs.experi.ActivitesFragment.Chat.ChatAdapter;
import com.fairfaqs.experi.ActivitesFragment.Chat.Chat_GetSet;
import com.fairfaqs.experi.R;

public class Chatviewholder extends RecyclerView.ViewHolder {
    public TextView message,datetxt,message_seen;
    public  View view;
    public Chatviewholder(View itemView) {
        super(itemView);
        view = itemView;
        this.message = view.findViewById(R.id.msgtxt);
        this.datetxt=view.findViewById(R.id.datetxt);
        message_seen=view.findViewById(R.id.message_seen);
    }

    public void bind(final Chat_GetSet item, final ChatAdapter.OnLongClickListener long_listener) {
        message.setOnLongClickListener(v -> {
                long_listener.onLongclick(item,v);
                return false;

        });
    }
}
