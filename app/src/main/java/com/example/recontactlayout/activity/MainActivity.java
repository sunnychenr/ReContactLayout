package com.example.recontactlayout.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.recontactlayout.R;
import com.example.recontactlayout.adapter.ContactsAdapter;
import com.example.recontactlayout.databeans.ContactInfo;
import com.example.recontactlayout.utils.PreferencesManager;

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
    @BindView(R.id.tv_enter_search)
    TextView tvEnterSearch;

    private List<ContactInfo> mContactModel = new ArrayList<>();
    private ContactsAdapter mContactAdapter;

    private LinearLayoutManager mLinearLayoutManager;
    private GridLayoutManager mGridLayoutManager;

    private Thread mQueryContacts = null;

    private boolean isListLayout;

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
        rvContacts.setItemAnimator(new DefaultItemAnimator());

        isListLayout = PreferencesManager.getInstance().getContactsLayoutType();

        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mGridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        setContactLayout();

        initData();
    }

    private void setContactLayout() {
        if (isListLayout) {
            rvContacts.setLayoutManager(mLinearLayoutManager);
        } else {
            rvContacts.setLayoutManager(mGridLayoutManager);
        }
        mContactAdapter.setListLayout(isListLayout);
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

    @OnClick({R.id.tv_enter_search, R.id.btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_enter_search:{

            }
            break;
            case R.id.btn:{
                isListLayout = !isListLayout;
                setContactLayout();
                PreferencesManager.getInstance().setContactsLayoutType(isListLayout);
            }
            break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

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

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.READ_CONTACTS}, 1024);
                return;
            }

            mEmptyContacts.clear();
            String[] projection = new String[]{
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts.SORT_KEY_PRIMARY,
                    ContactsContract.Contacts.LOOKUP_KEY,
                    ContactsContract.Contacts.PHOTO_ID,
                    ContactsContract.Contacts.PHOTO_URI
            };
            Cursor mContactsCursor = mContentResolver.query(
                    ContactsContract.Contacts.CONTENT_URI,
                    projection,
//                    null,
                    null,
                    null,
                    ContactsContract.Contacts.SORT_KEY_PRIMARY);

            if (mContactsCursor != null && mContactsCursor.getCount() > 0) {
                while (mContactsCursor.moveToNext()) {

                    int idIndex = getColumnIndex(mContactsCursor, ContactsContract.Contacts._ID);
                    int nameIndex = getColumnIndex(mContactsCursor, ContactsContract.Contacts.DISPLAY_NAME);
                    int lookupKeyIndex = getColumnIndex(mContactsCursor, ContactsContract.Contacts.LOOKUP_KEY);
                    int photoIdIndex = getColumnIndex(mContactsCursor, ContactsContract.Contacts.PHOTO_ID);
                    int photoUriIndex = getColumnIndex(mContactsCursor, ContactsContract.Contacts.PHOTO_URI);
                    int sortKeyIndex = getColumnIndex(mContactsCursor, ContactsContract.Contacts.SORT_KEY_PRIMARY);

                    long id = mContactsCursor.getLong(idIndex);
                    String name = mContactsCursor.getString(nameIndex);
                    String lookupKey = mContactsCursor.getString(lookupKeyIndex);
                    String photoId = mContactsCursor.getString(photoIdIndex);
                    String photoUri = mContactsCursor.getString(photoUriIndex);
                    String sortKey = mContactsCursor.getString(sortKeyIndex);

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
