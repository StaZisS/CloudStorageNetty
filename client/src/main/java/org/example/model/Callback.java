package org.example.model;

public interface Callback {
    void onSuccess();

    void onFailure(Object errorMessage);
}
