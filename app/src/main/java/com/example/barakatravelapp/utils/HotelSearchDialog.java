package com.example.barakatravelapp.utils;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barakatravelapp.R;
import com.example.barakatravelapp.adapter.GetAmentiesAdapter;
import com.example.barakatravelapp.adapter.SpinnerAdapter;
import com.example.barakatravelapp.data.model.GeneralResposeData;
import com.example.barakatravelapp.data.model.getAmenitiesResponce.GetAmenitiesResponce;
import com.example.barakatravelapp.data.model.getAmenitiesResponce.GetAmenity2;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.barakatravelapp.data.api.ApiClient.getApiClient;
import static com.example.barakatravelapp.utils.HelperMethod.dismissProgressDialog;
import static com.example.barakatravelapp.utils.HelperMethod.showProgressDialog;
import static com.example.barakatravelapp.utils.HelperMethod.showToast;
import static com.example.barakatravelapp.utils.ToastCreator.onCreateErrorToast;
import static com.example.barakatravelapp.utils.ToastCreator.onCreateSuccessToast;
import static com.example.barakatravelapp.utils.network.InternetState.isConnected;
import static com.example.barakatravelapp.utils.validation.Validation.validationLength;
import static com.example.barakatravelapp.utils.validation.Validation.validationLengthZero;

public class HotelSearchDialog extends DialogFragment {

    private String flightOrHotel;
    private SearchDialogCallback showSearchDialog;
    private LinearLayout showRyclePart;
    private ImageView cancelBtn ;
    private TextView showPaddingPart;
    private Button filterBtn;
    private List<GeneralResposeData> pricingList = new ArrayList<GeneralResposeData>();
    private List<GetAmenity2> amentiesList = new ArrayList<GetAmenity2>();
    private SpinnerAdapter priceSpinnerAdapter;
    private int priceSelectedId1 = 0;
    private AdapterView.OnItemSelectedListener priceListener;

    private GetAmentiesAdapter getAmentiesAdapter;
    private TextInputLayout tdialogFlightSearchCategorySearchTil;
    private TextInputLayout dialogFlightSearchCategorySearchPriceTil;
    private Spinner dialogFlightSearchCategorySpPrice;
//    private GridLayoutManager gridLayoutManager;
    private RecyclerView dialogAmenityRvRecyclerView;
    private List<Integer> amentiesIds =new ArrayList<Integer>();
    private LinearLayoutManager linearLayoutHorizental;

    public HotelSearchDialog() {
//        this.getTopUmarAndTophajjPackage=getTopUmarAndTophajjPackage;
    }

    public HotelSearchDialog(SearchDialogCallback showSearchDialog, String flightOrHotel) {
        this.showSearchDialog=showSearchDialog;
        this.flightOrHotel=flightOrHotel;

    }

//    public static DialogFragment newInstance(View.OnClickListener listener) {
//        DialogFragment fragment = new DialogFragment();
//        return fragment;
//    }

//    String[] players={"Lionel Messi","Christiano Ronaldo","Neymar","Gareth Bale"};

    //    @SuppressLint("WrongViewCast")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // TODO Auto-generated method stub
        View rootView= getActivity().getLayoutInflater().inflate(R.layout.dialog_flight_search_category, null);

        //SET TITLE DIALOG TITLE
//        getDialog().setTitle("Best Players In The World");
//        this.getDialog().setTitle("أختيار القسم العام");
//        this.getDialog().setCancelable(true);

        cancelBtn=(ImageView) rootView.findViewById(R.id.dialog_flight_search_category_close_btn);
        filterBtn=(Button) rootView.findViewById(R.id.dialog_flight_search_category_filter_now_btn);
        tdialogFlightSearchCategorySearchTil=(TextInputLayout) rootView.findViewById(R.id.tdialog_flight_search_category_search_til);
        dialogFlightSearchCategorySearchPriceTil=(TextInputLayout) rootView.findViewById(R.id.dialog_flight_search_category_search_price_til);
        dialogFlightSearchCategorySpPrice=(Spinner) rootView.findViewById(R.id.dialog_flight_search_category_sp_price);
        showPaddingPart=(TextView) rootView.findViewById(R.id.dialog_flight_search_category_padding_txt);
        showRyclePart=(LinearLayout) rootView.findViewById(R.id.dialog_flight_search_category_Layout);
        dialogAmenityRvRecyclerView = (RecyclerView) rootView.findViewById(R.id.dialog_flight_search_category_recycler_view2);
            dialogAmenityRvRecyclerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });
//            setInitRecyclerViewAsGridLayoutManager(getActivity(),dialogAmenityRvRecyclerView , gridLayoutManager, 2);
        linearLayoutHorizental = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        dialogAmenityRvRecyclerView.setLayoutManager(linearLayoutHorizental);
        dialogAmenityRvRecyclerView.setHasFixedSize(true);
        getAmentiesAdapter = new GetAmentiesAdapter(getActivity(), getActivity(), amentiesList);
        dialogAmenityRvRecyclerView.setAdapter(getAmentiesAdapter);
        onCall();
        setData();


//        RecyclerView dialogCategoriesRvRecyclerView = (RecyclerView) rootView.findViewById(R.id.dialog_day_by_day_and_evisa_more_rv_recycler_view);
//
//        lLayout = new LinearLayoutManager(getActivity());
//
//        dialogCategoriesRvRecyclerView.setLayoutManager(lLayout);
//
//         rcAdapter = new ShowDayByDayVrRvAdapter(getContext(), getActivity(), getTopUmarAndTophajjPackage.getUmarhDays());
//        dialogCategoriesRvRecyclerView.setAdapter(rcAdapter);
//        rcAdapter.notifyDataSetChanged();

        //BUTTON
        cancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dismiss();

            }
        });

        filterBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String typeSearch;
                int priceFrom=0,priceTo=0;
                String searchKey=tdialogFlightSearchCategorySearchTil.getEditText().getText().toString();
                if(getAmentiesAdapter.ids.size()!=0){
                amentiesIds=getAmentiesAdapter.ids;}
                if(priceSelectedId1==1){
                    priceFrom=500;
                }
                if(priceSelectedId1==2){
                    priceFrom=400;  priceTo=500;
                }
                if(priceSelectedId1==3){
                    priceFrom=300;  priceTo=400;
                }
                if(priceSelectedId1==4){
                    priceFrom=200;  priceTo=300;
                }
                if(priceSelectedId1==5){
                    priceFrom=1;  priceTo=200;
                }
                if(validationLengthZero(tdialogFlightSearchCategorySearchTil, getString(R.string.invalid_search), 0)){ // return searchKey = null
                    if (!validationLength(tdialogFlightSearchCategorySearchTil, getString(R.string.invalid_search), 3)) {
                        onCreateErrorToast(getActivity(), getString(R.string.invalid_search));
                        return;
                    }else {
                        if (priceSelectedId1 == 0) {
                            if (amentiesIds.size() == 0) {
                                typeSearch = "nameOnly";
                            } else {
                                typeSearch = "nameAmenity";
                            }
                        } else {
                            if (amentiesIds.size() == 0) {
                                if (priceSelectedId1 == 1) {
                                    typeSearch = "namePriceAbove";
                                } else {
                                    typeSearch = "namePrice";

                                }
                            } else {
                                if (priceSelectedId1 == 1) {
                                    typeSearch = "namePriceAmentyAbove";
                                } else {
                                    typeSearch = "nameAmentyPrice";

                                }
                            }
                        }
                    } }else {
                    if(priceSelectedId1==0){
                        if(amentiesIds.size()==0) {
                            typeSearch = "noFilter";
                        }else {
                            typeSearch = "amentyOnly";

                        }
                    } else  {
                        if(amentiesIds.size()==0) {
                            if(priceSelectedId1==1){
                                typeSearch="priceAbove";
                            }
                            else   {
                                typeSearch="price";
                            }
                        }else {
                            if(priceSelectedId1==1){
                                typeSearch="priceAmentyAbove";
                            }
                            else   {
                                typeSearch="priceAmenty";
                            }
                        }
                    }
                }
                if(typeSearch!=null){
                showSearchDialog.filterOnMethodCallback(searchKey,priceFrom,priceTo,typeSearch, amentiesIds);
                }else {
                    showToast(getActivity(), typeSearch);

                }
                dismiss();

            }
        });
//        return rootView;
        return new AlertDialog.Builder(getActivity())
//                .setTitle("أختيار القسم العام")
                .setView(rootView)
//                .setPositiveButton(android.R.string.ok,
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                // do something
//                            }
//                        }
//                )
                .create();
    }

    private void hotelValidation() {

    }



    private void setData() {

            showRyclePart.setVisibility(View.VISIBLE);

        pricingList.add(new GeneralResposeData(0, " $500 - Above "));
        pricingList.add(new GeneralResposeData(1, " $400 - $500 "));
        pricingList.add(new GeneralResposeData(2, " $300 - $400 "));
        pricingList.add(new GeneralResposeData(3, " $200 - $300 "));
        pricingList.add(new GeneralResposeData(4, " $200 - Below "));
        priceSpinnerAdapter = new SpinnerAdapter(getActivity());
        priceSpinnerAdapter.setData(pricingList, "");
        dialogFlightSearchCategorySpPrice.setAdapter(priceSpinnerAdapter);
        dialogFlightSearchCategorySpPrice.setSelection(priceSelectedId1);
        priceListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                priceSelectedId1 = i;
                dialogFlightSearchCategorySearchPriceTil.getEditText().setText(String.valueOf(priceSpinnerAdapter.getItem(priceSelectedId1)));
//                showToast(getActivity(), String.valueOf(priceSelectedId1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
        dialogFlightSearchCategorySpPrice.setOnItemSelectedListener(priceListener);
    }

    private void onCall() {

        if (isConnected(getActivity())) {
            showProgressDialog(getActivity(), getString(R.string.wait));

            Call<GetAmenitiesResponce> call;
            call = getApiClient().getAmenitiesItemList();

            call.enqueue(new Callback<GetAmenitiesResponce>() {
                @Override
                public void onResponse(Call<GetAmenitiesResponce> call, Response<GetAmenitiesResponce> response) {
                    try {

                        dismissProgressDialog();
                        if (response!=null) {

                            if (response.body().getStatus().equals("success")) {
                                amentiesList.clear();
                                amentiesList = new ArrayList<>();
                                amentiesList.addAll(response.body().getGetAmenities());
                                getAmentiesAdapter = new GetAmentiesAdapter(getActivity(), getActivity(), amentiesList);
                                dialogAmenityRvRecyclerView.setAdapter(getAmentiesAdapter);
                                getAmentiesAdapter.notifyDataSetChanged();

//                                showToast(getActivity(), "yes");

                            } else {
//                                onCreateErrorToast(getActivity(), response.body().getMsg());
                            }

                        } else {
                            onCreateSuccessToast(getActivity(), response.body().getMessage());
                        }

                    } catch (Exception e) {

                    }

                }

                @Override
                public void onFailure(Call<GetAmenitiesResponce> call, Throwable t) {
                    dismissProgressDialog();
                    onCreateErrorToast(getActivity(), getString(R.string.error));
                }
            });

        } else {
            dismissProgressDialog();
            onCreateErrorToast(getActivity(), getResources().getString(R.string.error_inter_net));
        }

    }
//    @Override
//    public void onMethodCallback(String title) {
//        backBtn.setVisibility(View.VISIBLE);
//        titleName.setText(title);
//    }


}

