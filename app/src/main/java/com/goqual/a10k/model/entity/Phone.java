package com.goqual.a10k.model.entity;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.viewholders.PhoneBookViewHolder;

import org.parceler.Parcel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by hanwool on 2017. 3. 21..
 */

@Parcel
public class Phone {

    private String displayName;
    private String number;
    private String photoUriString;
    private int _userid = -1;
    private int _connectionid = -1;

    public Phone() {
    }

    public Phone(String displayName, String number, String photoUriString) {
        this.displayName = displayName;
        this.number = number;
        this.photoUriString = photoUriString;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int get_userid() {
        return _userid;
    }

    public void set_userid(int _userid) {
        this._userid = _userid;
    }

    public int get_connectionid() {
        return _connectionid;
    }

    public void set_connectionid(int _connectionid) {
        this._connectionid = _connectionid;
    }

    public String getPhotoUriString() {
        return photoUriString;
    }

    public void setPhotoUriString(String photoUriString) {
        this.photoUriString = photoUriString;
    }

    public Drawable getPhoto(PhoneBookViewHolder view) {
        if(this.photoUriString != null) {
            try (InputStream is = view.getContext().getContentResolver().openInputStream(Uri.parse(this.photoUriString))) {
                return Drawable.createFromStream(is, this.photoUriString);
            } catch (IOException e) {
                LogUtil.e(this.getClass().getSimpleName(), e.getMessage(), e);
                return view.getContext().getDrawable(R.drawable.ic_user);
            }
        }
        else {
            return view.getContext().getDrawable(R.drawable.ic_user);
        }
    }
}
