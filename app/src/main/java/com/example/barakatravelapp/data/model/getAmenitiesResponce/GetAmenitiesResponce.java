
package com.example.barakatravelapp.data.model.getAmenitiesResponce;

import com.example.barakatravelapp.data.model.appSettingResponce.AppSetting;
import com.example.barakatravelapp.data.model.getHotelsResponce.GetAmenity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetAmenitiesResponce {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("amentities")
    @Expose
    private List<GetAmenity2> getAmenities = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<GetAmenity2> getGetAmenities() {
        return getAmenities;
    }

    public void setGetAmenities(List<GetAmenity2> getAmenities) {
        this.getAmenities = getAmenities;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
