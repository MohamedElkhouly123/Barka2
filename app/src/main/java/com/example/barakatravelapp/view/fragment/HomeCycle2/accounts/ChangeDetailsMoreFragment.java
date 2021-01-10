package com.example.barakatravelapp.view.fragment.HomeCycle2.accounts;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.barakatravelapp.R;
import com.example.barakatravelapp.data.model.appSettingResponce.AppSetting;
import com.example.barakatravelapp.data.model.appSettingResponce.AppSettingResponce;
import com.example.barakatravelapp.utils.ToastCreator;
import com.example.barakatravelapp.view.activity.SplashCycleActivity;
import com.example.barakatravelapp.view.fragment.BaSeFragment;
import com.example.barakatravelapp.view.viewModel.ViewModelUser;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

import static com.example.barakatravelapp.data.api.ApiClient.getApiClient;
import static com.example.barakatravelapp.data.local.SharedPreferencesManger.clean;
import static com.example.barakatravelapp.utils.network.InternetState.isConnected;


public class ChangeDetailsMoreFragment extends BaSeFragment {
    private NavController navController;
    private ViewModelUser viewModelUser;
    private AppSetting appSetting;
    private Bundle bundle;
    boolean checkResponceReturn=false;
    public ChangeDetailsMoreFragment() {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_change_more_details, container, false);

        ButterKnife.bind(this, root);
        navController = Navigation.findNavController(getActivity(), R.id.home_activity_fragment);
        initListener();
        getAppSettings();
        return root;
    }

    private void getAppSettings() {
        Call<AppSettingResponce> clientCall;
        clientCall = getApiClient().appSettings();
        viewModelUser.getAppSettings(getActivity(), clientCall,true);
        bundle= new Bundle();
    }

    @SuppressLint("FragmentLiveDataObserve")
    private void initListener() {
        viewModelUser = ViewModelProviders.of(this).get(ViewModelUser.class);
        viewModelUser.makeGetAppSettings().observe(this, new Observer<AppSettingResponce>() {
            @Override
            public void onChanged(@Nullable AppSettingResponce response) {
                if (response != null) {
                    if (response.getStatus().equals("success")) {
//                        showToast(getActivity(), "success");
                          checkResponceReturn=true;
                          appSetting=response.getAppSetting();
                    }

                }
            }
        });
    }

    @Override
    public void onBack() {
//        replaceFragment(getActivity().getSupportFragmentManager(), R.id.home_activity_fragment, new ChangeDetailsFragment());
        navController.navigate(R.id.action_changeDetailsMoreFragment_to_navigation_account);
        homeCycleActivity.setNavigation("v");

    }

    @OnClick({R.id.fragment_change_more_details_about_barka_faq_btn,R.id.fragment_change_more_details_about_barka_btn, R.id.fragment_change_more_details_rate_app_btn, R.id.fragment_change_more_terms_condation_btn, R.id.fragment_change_more_details_contact_us_btn, R.id.fragment_change_more_details_log_out_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_change_more_details_about_barka_faq_btn:
                navController.navigate(R.id.action_changeDetailsMoreFragment_to_FAQFragment);
                break;
            case R.id.fragment_change_more_details_about_barka_btn:
                if (checkResponceReturn) {
                    bundle.putSerializable("ObjectAppSettings", appSetting);
                    navController.navigate(R.id.action_changeDetailsMoreFragment_to_aboutUsFragment, bundle);
                }else {
                    ToastCreator.onCreateErrorToast(getActivity(), getString(R.string.check_your_internet_and_try));
                    onBack();
                }
                break;
            case R.id.fragment_change_more_details_rate_app_btn:
                String url ="https://play.google.com/store/apps/details?id=com.barkatravel.usbarakatravelapp";
                try {
                    Intent i = new Intent("android.intent.action.MAIN");
                    i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
                    i.addCategory("android.intent.category.LAUNCHER");
                    i.setData(Uri.parse(url));
                    getActivity().startActivity(i);
                }
                catch(ActivityNotFoundException e) {
                    // Chrome is not installed
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    getActivity().startActivity(i);
                }
                break;
            case R.id.fragment_change_more_terms_condation_btn:
                if (checkResponceReturn){
                bundle.putSerializable("ObjectAppSettings",  appSetting);
                navController.navigate(R.id.action_changeDetailsMoreFragment_to_policyFragment,bundle);
                }else {
//                    if(!isConnected(getActivity())){
                    ToastCreator.onCreateErrorToast(getActivity(), getString(R.string.check_your_internet_and_try));
//                    }else {
//                        initListener();
//                        getAppSettings();
//                        Bundle bundle2= new Bundle();
//                        bundle2.putSerializable("ObjectAppSettings",  appSetting);
//                        navController.navigate(R.id.action_changeDetailsMoreFragment_to_policyFragment,bundle2);
//                    }
                    onBack();
                }
                break;
            case R.id.fragment_change_more_details_contact_us_btn:
                navController.navigate(R.id.action_changeDetailsMoreFragment_to_contactUsFragment);
                break;
            case R.id.fragment_change_more_details_log_out_next:
                showLogOutDialog();
                break;
        }
    }

    private void showLogOutDialog(){
        try {
//                final View view = activity.getLayoutInflater().inflate(R.layout.dialog_restaurant_add_category, null);
//            alertDialog = new AlertDialog.Builder(HomeFragment.this).create();
            AlertDialog alertDialog;
            alertDialog = new AlertDialog.Builder(getActivity()).create();
//            alertDialog.setTitle("Delete");
            alertDialog.setMessage("Are you sure you want log out ?");
            alertDialog.setCancelable(true);

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Log Out", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    clean(getActivity());
                    Intent i = new Intent(getActivity(), SplashCycleActivity.class);

                    getActivity().startActivity(i);
                    // close this activity
                    getActivity().finish();

                }
            });


            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss() ;
                }
            });

//                alertDialog.setView(view);
//            alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
//                @SuppressLint("ResourceAsColor")
//                @Override
//                public void onShow(DialogInterface arg0) {
//                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.pink);
//                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(R.color.pink);
//
//                }
//            });

            alertDialog.show();

        } catch (Exception e) {

        }
    }
}