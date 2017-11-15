package com.example.recontactlayout.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.example.recontactlayout.ApplicationEx;
import com.example.recontactlayout.R;

/**
 * Created by ChenR on 2017/11/6.
 */

public class Utils {

    public static Bitmap getContactPhoto (Context context, String photoId) {
        Bitmap photo = null;

        if (context != null && !TextUtils.isEmpty(photoId)) {
            String[] projection = new String[]{ContactsContract.Data.DATA15};
            String selection = ContactsContract.Data._ID + " = " + photoId;
            Cursor cur = null;
            try {
                cur = context.getContentResolver().query(
                        ContactsContract.Data.CONTENT_URI, projection, selection, null, null);
                if (cur != null && cur.moveToFirst()) {
                    byte[] contactIcon = cur.getBlob(0);
                    if (contactIcon == null) {
                        return null;
                    } else {
                        photo = BitmapFactory.decodeByteArray(contactIcon, 0, contactIcon.length);
                    }
                    cur.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return photo;
    }

    public static int[] getScreenSize () {
        Context context = ApplicationEx.getInstance().getApplicationContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);

        return new int[]{metrics.widthPixels, metrics.heightPixels};
    }

    public static int getBackgroundColor(int position) {
        int color = R.color.color_FFFF5604;
        int i = position % 7;

        switch (i) {
            case 0:
                color = R.color.color_FFFF5604;
                break;
            case 1:
                color = R.color.color_FF0097A8;
                break;
            case 2:
                color = R.color.color_FF6735BB;
                break;
            case 3:
                color = R.color.color_FF009E54;
                break;
            case 4:
                color = R.color.color_FFEC1761;
                break;
            case 5:
                color = R.color.color_FF465EA1;
                break;
            case 6:
                color = R.color.color_FF4E92CF;
                break;
        }

        return color;
    }

}
