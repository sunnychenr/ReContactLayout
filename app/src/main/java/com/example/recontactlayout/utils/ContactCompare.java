package com.example.recontactlayout.utils;

import com.example.recontactlayout.databeans.ContactInfo;

import java.util.Comparator;

/**
 * Created by ChenR on 2017/11/15.
 */

public class ContactCompare implements Comparator<ContactInfo> {
    @Override
    public int compare(ContactInfo o1, ContactInfo o2) {
        char ch1 = o1.getSortKey().toCharArray()[0];
        char ch2 = o2.getSortKey().toCharArray()[0];

        if (ch1 > ch2) return 1;
        if (ch1 < ch2) return -1;
        return 0;
    }
}
