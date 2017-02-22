package com.goqual.a10k.model.entity;

import android.view.View;

/**
 * Created by hanwool on 2017. 2. 22..
 */

public class DialogModel {
    public String title;
    public String message;
    public String editTextHint;
    public String editTextMessage;
    public boolean isEditable;
    public boolean isSetPositiveButton;
    public boolean isSetNegativeButton;

    public DialogModel() {
        isEditable = false;
        isSetPositiveButton = true;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    public boolean isSetPositiveButton() {
        return isSetPositiveButton;
    }

    public void setPositiveButton(boolean setPositiveButton) {
        isSetPositiveButton = setPositiveButton;
    }

    public boolean isSetNegativeButton() {
        return isSetNegativeButton;
    }

    public void setNegativeButton(boolean setNegativeButton) {
        isSetNegativeButton = setNegativeButton;
    }

    public String getEditTextHint() {
        return editTextHint;
    }

    public void setEditTextHint(String editTextHint) {
        this.editTextHint = editTextHint;
    }

    public String getEditTextMessage() {
        return editTextMessage;
    }

    public void setEditTextMessage(String editTextMessage) {
        this.editTextMessage = editTextMessage;
    }

    public void setSetPositiveButton(boolean setPositiveButton) {
        isSetPositiveButton = setPositiveButton;
    }

    public void setSetNegativeButton(boolean setNegativeButton) {
        isSetNegativeButton = setNegativeButton;
    }
}
