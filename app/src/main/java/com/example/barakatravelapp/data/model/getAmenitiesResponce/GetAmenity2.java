
package com.example.barakatravelapp.data.model.getAmenitiesResponce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetAmenity2 implements Serializable {
// وسائل الراحه amenities
    @SerializedName("id")
    @Expose
    private Integer amenitiesId;
    @SerializedName("name")
    @Expose
    private String name;

    public Integer getId() {
        return amenitiesId;
    }

    public void setId(Integer amenitiesId) {
        this.amenitiesId = amenitiesId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
