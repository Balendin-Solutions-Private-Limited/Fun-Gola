package com.thambola.fungola.Model;

import com.google.gson.annotations.SerializedName;

public class LoginError {

    @SerializedName("error")
    public String error;
    @SerializedName("error_description")
    public String error_description;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }
}
