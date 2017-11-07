package com.example.recontactlayout.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.recontactlayout.R;
import com.example.recontactlayout.utils.Utils;
import com.example.recontactlayout.databeans.ContactInfo;

import java.util.List;

/**
 * Created by ChenR on 2017/11/6.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactHolder> {

    private List<ContactInfo> model = null;
    private Context mContext;

    public ContactsAdapter(List<ContactInfo> model, Context context) {
        this.model = model;
        this.mContext = context;
    }

    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = View.inflate(mContext, R.layout.item_list_contact, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        root.setLayoutParams(params);
        return new ContactHolder(root);
    }

    @Override
    public void onBindViewHolder(ContactHolder holder, int position) {
        ContactInfo info = model.get(position);
        if (info != null) {
            Bitmap photo = Utils.getContactPhoto(mContext, info.getPhotoSaveId());
            if (photo != null) {
                holder.photo.setImageBitmap(photo);
            } else {
                holder.photo.setImageResource(R.mipmap.ic_launcher);
            }

            String name = info.getName();
            holder.name.setText(name);
        }
    }

    @Override
    public int getItemCount() {
        return model == null ? 0 : model.size();
    }

    class ContactHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private ImageView photo;

        public ContactHolder(View itemView) {
            super(itemView);

            photo = (ImageView) itemView.findViewById(R.id.iv_contact_photo);
            name = (TextView) itemView.findViewById(R.id.tv_contact_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("chenr", "item root onClick");
                }
            });
        }
    }
}
