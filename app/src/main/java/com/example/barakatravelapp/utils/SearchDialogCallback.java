package com.example.barakatravelapp.utils;


import java.util.List;

public interface SearchDialogCallback {

       void filterOnMethodCallback(String searchKey, int priceFrom, int priceTo, String typeSearch, List<Integer> amentiesIds);
}
