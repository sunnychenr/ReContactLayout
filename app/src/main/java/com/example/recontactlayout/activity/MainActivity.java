package com.example.recontactlayout.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.recontactlayout.databeans.ContactInfo;
import com.example.recontactlayout.adapter.ContactsAdapter;
import com.example.recontactlayout.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    @BindView(R.id.btn)
    Button btn;
    @BindView(R.id.rv_contacts)
    RecyclerView rvContacts;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;

    private List<ContactInfo> mContactModel = new ArrayList<>();
    private ContactsAdapter mContactAdapter;

    private Thread mQueryContacts = null;

    private Handler mhandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            pbLoading.setVisibility(View.GONE);
            rvContacts.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mContactAdapter = new ContactsAdapter(mContactModel, this);
        rvContacts.setAdapter(mContactAdapter);
        rvContacts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvContacts.setItemAnimator(new DefaultItemAnimator());
        rvContacts.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        initData();
    }

    private void initData() {
        if (mQueryContacts == null || !mQueryContacts.isAlive()) {
            mQueryContacts = new Thread(new ReadContactsRunn(getContentResolver(), mContactModel));
            mQueryContacts.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mQueryContacts != null && mQueryContacts.isAlive()) {
            mQueryContacts.interrupt();
            mQueryContacts = null;
        }
    }

    @OnClick(R.id.btn)
    public void onViewClicked() {

    }

    private class ReadContactsRunn implements Runnable {

        private ContentResolver mContentResolver;
        private List<ContactInfo> mEmptyContacts;

        public ReadContactsRunn(ContentResolver resolver, List<ContactInfo> emptyContacts) {
            this.mContentResolver = resolver;
            this.mEmptyContacts = emptyContacts;
        }

        @Override
        public void run() {

            if (mEmptyContacts == null) return;

            mEmptyContacts.clear();

            String [] projection = new String[] {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts.LOOKUP_KEY,
                    ContactsContract.Contacts.SORT_KEY_PRIMARY,
                    ContactsContract.Contacts.PHOTO_ID,
                    ContactsContract.Contacts.PHOTO_URI,
            };

            Cursor mContactsCursor = mContentResolver.query(ContactsContract.Contacts.CONTENT_URI, projection, null, null, null);

            if (mContactsCursor != null && mContactsCursor.getCount() > 0) {
                while (mContactsCursor.moveToNext()) {

                    int idIndex = getColumnIndex(mContactsCursor, ContactsContract.Contacts._ID);
                    int nameIndex = getColumnIndex(mContactsCursor, ContactsContract.Contacts.DISPLAY_NAME);
                    int lookupKeyIndex = getColumnIndex(mContactsCursor, ContactsContract.Contacts.LOOKUP_KEY);
                    int sortKeyIndex = getColumnIndex(mContactsCursor, ContactsContract.Contacts.SORT_KEY_PRIMARY);
                    int photoIdIndex = getColumnIndex(mContactsCursor, ContactsContract.Contacts.PHOTO_ID);
                    int photoUriIndex = getColumnIndex(mContactsCursor, ContactsContract.Contacts.PHOTO_URI);

                    long id = mContactsCursor.getLong(idIndex);
                    String name = mContactsCursor.getString(nameIndex);
                    String lookupKey = mContactsCursor.getString(lookupKeyIndex);
                    String sortKey = mContactsCursor.getString(sortKeyIndex);
                    String photoId = mContactsCursor.getString(photoIdIndex);
                    String photoUri = mContactsCursor.getString(photoUriIndex);

                    ContactInfo info = new ContactInfo();
                    info.setId(id);
                    info.setLookupKey(lookupKey);
                    info.setName(name);
                    info.setPhotoSaveId(photoId);
                    info.setPhotoUri(photoUri);
                    info.setSortKey(sortKey);

                    Log.d("chenr", "contact item: " + info.toString());

                    mEmptyContacts.add(info);
                }
                mhandler.sendEmptyMessage(0);
                mContactsCursor.close();
            }

        }

        private int getColumnIndex(Cursor cursor, String id) {
            return cursor.getColumnIndex(id);
        }
    }
}
