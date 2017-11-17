package com.example.recontactlayout.databeans;

/**
 * Created by ChenR on 2017/11/6.
 */

public class ContactInfo {

    private long id;
    private String name;
    private String sort_key;
    private String photoUri;
    private String photo_save_id;
    private String look_up_key;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortKey() {
        return sort_key;
    }

    public void setSortKey(String sortKey) {
        this.sort_key = sortKey;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getPhotoSaveId() {
        return photo_save_id;
    }

    public void setPhotoSaveId(String photoSaveId) {
        this.photo_save_id = photoSaveId;
    }

    public String getLookupKey() {
        return look_up_key;
    }

    public void setLookupKey(String lookupKey) {
        this.look_up_key = lookupKey;
    }

    @Override
    public String toString() {
        return "ContactInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sort_key='" + sort_key + '\'' +
                ", photoUri='" + photoUri + '\'' +
                ", photo_save_id='" + photo_save_id + '\'' +
                ", look_up_key='" + look_up_key + '\'' +
                '}';
    }
}
