package com.example.barakatravelapp.view.fragment.HomeCycle2.hotels;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.barakatravelapp.R;
import com.example.barakatravelapp.adapter.GetHotelsItemsAdapter;
import com.example.barakatravelapp.data.model.getHotelsResponce.GetHotelsResponce;
import com.example.barakatravelapp.data.model.getHotelsResponce.HotelData;
import com.example.barakatravelapp.data.model.userLoginResponce.UserData;
import com.example.barakatravelapp.utils.FlightSearchDialog;
import com.example.barakatravelapp.utils.HotelSearchDialog;
import com.example.barakatravelapp.utils.OnEndLess;
import com.example.barakatravelapp.utils.SearchDialogCallback;
import com.example.barakatravelapp.view.fragment.BaSeFragment;
import com.example.barakatravelapp.view.viewModel.ViewModelGetLists;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

import static com.example.barakatravelapp.data.api.ApiClient.getApiClient;
import static com.example.barakatravelapp.utils.HelperMethod.showToast;
import static com.example.barakatravelapp.utils.ToastCreator.onCreateErrorToast;
import static com.example.barakatravelapp.utils.validation.Validation.validationLength;


public class HottelsFragment extends BaSeFragment implements SearchDialogCallback {
    @BindView(R.id.top_part_in_nav_genral_part_search_til)
    TextInputLayout topPartInNavGenralPartSearchTil;
    @BindView(R.id.fragment_home_hottels_recycler_view)
    RecyclerView fragmentHomeHottelsRecyclerView;
    @BindView(R.id.load_more)
    RelativeLayout loadMore;
    @BindView(R.id.error_title)
    TextView errorTitle;
    @BindView(R.id.error_sub_view)
    LinearLayout errorSubView;
    @BindView(R.id.no_result_error_title)
    TextView noResultErrorTitle;
    @BindView(R.id.fragment_home_hottels_sr_refresh)
    SwipeRefreshLayout fragmentHomeHottelsSrRefresh;
    private NavController navController;
    private LinearLayoutManager linearLayout;
    public List<HotelData> getHotelsItemsListData = new ArrayList<HotelData>();
    public GetHotelsItemsAdapter getHotelsItemsAdapter;
    private ViewModelGetLists viewModel;
    public Integer maxPage = 0;
    private OnEndLess onEndLess;
    private boolean Filter = false;
    private String searchKey,typeSearch;
    private int priceFrom,priceTo;
    private List<Integer> amentiesIds =new ArrayList<Integer>();
    private UserData clientData;
    //    private int citiesSelectedId = 0;
    private String keyword;
    private int priceToAbove= 900000000;

    //    private AdapterView.OnItemSelectedListener listener;
    public HottelsFragment() {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home_hottels, container, false);

        ButterKnife.bind(this, root);
        topPartInNavGenralPartSearchTil.setVisibility(View.VISIBLE);
        navController = Navigation.findNavController(getActivity(), R.id.home_activity_fragment);
        setUpActivity();
        initListener();
//        clientData = LoadUserData(getActivity());
        init();
        return root;
    }

    private void initListener() {

        viewModel = ViewModelProviders.of(this).get(ViewModelGetLists.class);

        viewModel.makeGetHotelsDataList().observe(getViewLifecycleOwner(), new Observer<GetHotelsResponce>() {
            @Override
            public void onChanged(@Nullable GetHotelsResponce response) {
                try {
                    if (response != null) {
                        if (response.getStatus().equals("success")) {

//                                showToast(getActivity(), "max="+maxPage);

                            if (response.getHotels() != null ) {
                                getHotelsItemsListData.clear();
                                getHotelsItemsListData.addAll(response.getHotels());
//                                showToast(getActivity(), "list="+clientrestaurantsListData.get(1));

                                getHotelsItemsAdapter.notifyDataSetChanged();
//                                if(getHotelsItemsListData.size()){
                                    maxPage++;
//                                }
                                noResultErrorTitle.setVisibility(View.GONE);
//
                            } else {
                                noResultErrorTitle.setVisibility(View.VISIBLE);
                            }
//                                showToast(getActivity(), "success1");

                        }
                    } else {

                    }

                } catch (Exception e) {
                }
            }
        });

    }

    private void init() {
        linearLayout = new LinearLayoutManager(getActivity());
        fragmentHomeHottelsRecyclerView.setLayoutManager(linearLayout);

        onEndLess = new OnEndLess(linearLayout, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= maxPage) {
                    if (maxPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        loadMore.setVisibility(View.VISIBLE);
                        if (Filter) {
                            getHotelsListByFilter(current_page, searchKey, priceFrom, priceTo, amentiesIds, typeSearch);
                        } else {
                            getHotelsHomeList(current_page);
                        }

                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };
        fragmentHomeHottelsRecyclerView.addOnScrollListener(onEndLess);

        getHotelsItemsAdapter = new GetHotelsItemsAdapter(getActivity(), getContext(), navController,getHotelsItemsListData);
        fragmentHomeHottelsRecyclerView.setAdapter(getHotelsItemsAdapter);
//            showToast(getActivity(), "success adapter");




        if (getHotelsItemsListData.size() == 0) {
            getHotelsHomeList(0);
        }
//        else {
//            clientAndRestaurantHomeFragmentSFlShimmer.stopShimmer();
//            clientAndRestaurantHomeFragmentSFlShimmer.setVisibility(View.GONE);
//        }

        fragmentHomeHottelsSrRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                maxPage=0;
                if (Filter) {
                    getHotelsListByFilter(0, searchKey, priceFrom, priceTo, amentiesIds, typeSearch);
                } else {
                    getHotelsHomeList(0);
                }

            }
        });
    }

    private void getHotelsListByFilter(int page, String searchKey, int priceFrom, int priceTo, List<Integer> amentiesIds, String typeSearch) {

        Filter = true;
        if(page == 0){ maxPage=0;}
//        keyword = topPartInNavGenralPartSearchTil.getEditText().getText().toString().trim();
//        keyword="jfk";
        Call<GetHotelsResponce> getHotelsResponceCall=null;
        if(typeSearch.equalsIgnoreCase("nameOnly")) {
//            showToast(getActivity(), "nameOnly");
            getHotelsResponceCall = getApiClient().getHotelsItemListByNormalFilter(page, searchKey);
        }
        if(typeSearch.equalsIgnoreCase("nameAmenity")) {
//            showToast(getActivity(), "nameAmenity");
            getHotelsResponceCall = getApiClient().getHotelsItemListByNameAmenityFilter(page, searchKey,amentiesIds);
        }
        if(typeSearch.equalsIgnoreCase("namePriceAbove")) {
//            showToast(getActivity(), "namePriceAbove"+priceFrom);
            getHotelsResponceCall = getApiClient().getHotelsItemListByNamePriceFilter(page, searchKey,priceFrom,priceToAbove);
        }
        if(typeSearch.equalsIgnoreCase("namePrice")) {
//            showToast(getActivity(), "namePrice");
            getHotelsResponceCall = getApiClient().getHotelsItemListByNamePriceFilter(page, searchKey,priceFrom,priceTo);
        }
        if(typeSearch.equalsIgnoreCase("namePriceAmentyAbove")) {
//            showToast(getActivity(), "namePriceAmentyAbove");
            getHotelsResponceCall = getApiClient().getHotelsItemListByAllFilter(page, searchKey,amentiesIds,priceFrom,priceToAbove);
        }
        if(typeSearch.equalsIgnoreCase("nameAmentyPrice")) {
//            showToast(getActivity(), "nameAmentyPrice");
            getHotelsResponceCall = getApiClient().getHotelsItemListByAllFilter(page, searchKey,amentiesIds,priceFrom,priceTo);
        }
        if(typeSearch.equalsIgnoreCase("amentyOnly")) {
//            showToast(getActivity(), "amentyOnly");
            getHotelsResponceCall = getApiClient().getHotelsItemListByAmintyFilter(page, amentiesIds);
        }
        if(typeSearch.equalsIgnoreCase("priceAbove")) {
//            showToast(getActivity(), "priceAbove"+" priceFrom= "+priceFrom);
            getHotelsResponceCall = getApiClient().getHotelsItemListByPriceFilter(page, priceFrom, priceToAbove);
        }
        if(typeSearch.equalsIgnoreCase("price")) {
//            showToast(getActivity(), "priceNotAbove"+" priceFrom= "+priceFrom+" priceTo= "+priceTo);
            getHotelsResponceCall = getApiClient().getHotelsItemListByPriceFilter(page, priceFrom,priceTo);
        }
        if(typeSearch.equalsIgnoreCase("priceAmentyAbove")) {
//            showToast(getActivity(), "priceAmentyAbove");
            getHotelsResponceCall = getApiClient().getHotelsItemListByPriceAmenityFilter(page, amentiesIds,priceFrom,priceToAbove);
        }
        if(typeSearch.equalsIgnoreCase("priceAmenty")) {
//            showToast(getActivity(), "priceAmenty");
            getHotelsResponceCall = getApiClient().getHotelsItemListByPriceAmenityFilter(page, amentiesIds,priceFrom,priceTo);
        }
        if(typeSearch.equalsIgnoreCase("noFilter")) {
//            showToast(getActivity(), "noFilter");
            getHotelsHomeList(0);
            return;
        }
//        startShimmer(page);
        if(getHotelsResponceCall!=null)
        viewModel.getHotelsDataList(getActivity(), errorSubView, getHotelsResponceCall,fragmentHomeHottelsSrRefresh, loadMore);
    }

    private void getHotelsHomeList(int page) {
        Filter = false;
        if(page == 0){
        maxPage=0;}
        Call<GetHotelsResponce> getHotelsResponceCall;

//        startShimmer(page);

        reInit();
        getHotelsResponceCall = getApiClient().getHotelsItemList(page);

//            clientRestaurantsCall = getApiClient().getRestaurantsWithoutFiltter(page);
        viewModel.getHotelsDataList(getActivity(), errorSubView, getHotelsResponceCall,fragmentHomeHottelsSrRefresh, loadMore);
//            showToast(getActivity(), "success without fillter");



    }




    private void reInit() {
        onEndLess.previousTotal = 0;
        onEndLess.current_page = 1;
        onEndLess.previous_page = 1;
        getHotelsItemsListData = new ArrayList<>();
        getHotelsItemsAdapter = new GetHotelsItemsAdapter(getActivity(), getContext(), navController, getHotelsItemsListData);
        fragmentHomeHottelsRecyclerView.setAdapter(getHotelsItemsAdapter);

    }


    public void setError(String errorTitleTxt) {
        View.OnClickListener action = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Filter) {
                    getHotelsListByFilter(0, searchKey, priceFrom, priceTo, amentiesIds, typeSearch);
                } else {
                    getHotelsHomeList(0);
                }

            }
        };
        errorSubView.setVisibility(View.VISIBLE);
        errorTitle.setText(errorTitleTxt);

    }

    @Override
    public void onBack() {
//        replaceFragment(getActivity().getSupportFragmentManager(), R.id.home_activity_fragment, new DiscoverFragment());
        navController.navigate(R.id.action_navigation_hotels_to_navigation_discover);
//        homeCycleActivity.setNavigation("v");
//        homeCycleActivity.bottomNavView.getMenu().getItem(0).setChecked(true);
    }

    @OnClick(R.id.top_part_in_nav_genral_part_search_etxt)
    public void onViewClicked() {
        final HotelSearchDialog dialog = new HotelSearchDialog(this::filterOnMethodCallback,"hotel");
        dialog.show(getActivity().getSupportFragmentManager(), "example");
//        getHotelsItemsListData.clear();
//        topPartInNavGenralPartSearchTil.setErrorEnabled(false);
//        if (!validationLengthZero(topPartInNavGenralPartSearchTil, getString(R.string.invalid_search), 0)) {
////                    onCreateErrorToast(getActivity(), getString(R.string.invalid_search));
//            getHotelsHomeList(0);
//            return;
//        }
//            if (!validationLength(topPartInNavGenralPartSearchTil, getString(R.string.invalid_search), 3)) {
//                onCreateErrorToast(getActivity(), getString(R.string.invalid_search));
//
//                return;
//            } else {
//
//                getHotelsListByFilter(0);
//
//            }
    }

    @Override
    public void filterOnMethodCallback(String searchKey, int priceFrom, int priceTo, String typeSearch, List<Integer> amentiesIds) {
        getHotelsItemsListData.clear();
        this.searchKey=searchKey;
        this.priceFrom=priceFrom;
        this.priceTo=priceTo;
        this.typeSearch=typeSearch;
        this.amentiesIds=amentiesIds;
//        showToast(getActivity(), String.valueOf(amentiesIds.size()));
        getHotelsListByFilter(0,searchKey,priceFrom,priceTo,amentiesIds,typeSearch);
    }
}