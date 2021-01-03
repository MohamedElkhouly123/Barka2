package com.example.barakatravelapp.adapter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barakatravelapp.R;
import com.example.barakatravelapp.data.model.getBookingPackageResponce.BookingPackage;
import com.example.barakatravelapp.data.model.getBookingPackageResponce.PackagePerson;
import com.example.barakatravelapp.data.model.getUmrahAndHujjResponce.GetTopUmarAndTophajjPackage;
import com.example.barakatravelapp.view.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GetBookingsHajjAndUmrahItemsAdapter extends RecyclerView.Adapter<GetBookingsHajjAndUmrahItemsAdapter.ViewHolder> {

//    private final DialogAdapterCallback dialogAdapterCallback;

    private final String hajjOrUmrah;
    private final String bookingType;
    private BaseActivity activity;
    private Context context;
    private List<BookingPackage> getHajjAndUmraItemsListData = new ArrayList<>();
    private List<String> hotelImages = new ArrayList<>();
    private NavController navController;
    private String visaOrCash;
    public GetBookingsHajjAndUmrahItemsAdapter(Activity activity, Context context, NavController navController, String bookingType, String hajjOrUmrah, List<BookingPackage> getHajjAndUmraItemsListData) {
        getHajjAndUmraItemsListData.clear();
        this.activity = (BaseActivity) activity;
        this.context = context;
        this.hajjOrUmrah = hajjOrUmrah;
        this.navController = navController;
        this.bookingType = bookingType;
        this.getHajjAndUmraItemsListData = getHajjAndUmraItemsListData;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.cardview_general_vert_booking_hajj_and_hotels_item,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {

//        final int itemType = getItemViewType(position);


        GetTopUmarAndTophajjPackage getTopUmarAndTophajjPackageData = getHajjAndUmraItemsListData.get(position).getPackageDetail();
        PackagePerson packagePerson = getHajjAndUmraItemsListData.get(position).getPackagePerson();

        holder.cardviewGeneralVertBookingHajjAndHotelsItemUserNameTv.setText("Name : " + packagePerson.getFirstName() + " " + packagePerson.getLastName());
        holder.cardviewGeneralVertBookingHajjAndHotelsItemPackageNameTv.setText("Package Name : " + getTopUmarAndTophajjPackageData.getUmar().getName());
        holder.cardviewGeneralVertBookingHajjAndHotelsItemDepartureDateTv.setText("Departure Date : " + getHajjAndUmraItemsListData.get(position).getDepartureDate());
        holder.cardviewGeneralVertBookingHajjAndHotelsItemReturnDateTv.setText("Return Date : " + getHajjAndUmraItemsListData.get(position).getReturnDate());
        holder.cardviewGeneralVertBookingHajjAndHotelsItemTotalPriceTv.setText("Total : $" + getHajjAndUmraItemsListData.get(position).getTotalPrice());
        holder.cardviewGeneralVertBookingHajjAndHotelsItemCommentTv.setText("Comment : " + getHajjAndUmraItemsListData.get(position).getPriefTravel());
        String status = getHajjAndUmraItemsListData.get(position).getStatus();
         visaOrCash = getHajjAndUmraItemsListData.get(position).getPaymentType();


        if (status.equalsIgnoreCase("pending")){
//            holder.cardviewBookEVisaOffersItemStatusLy.setBackgroundResource(R.drawable.circle_btn_yello_shape);
            holder.cardviewBookEVisaOffersItemCompletePaymentBtn.setVisibility(View.VISIBLE);
            if (!visaOrCash.equalsIgnoreCase("Visa")) {
                holder.cardviewBookEVisaOffersItemCompletePaymentBtn.setText("Contact with Management");
            }
            holder.cardviewGeneralVertBookingHajjAndHotelsItemStatusTv.setText("Pending");

        }
        if (status.equalsIgnoreCase("complete")) {
            holder.cardviewGeneralVertBookingHajjAndHotelsItemStatusTv.setText("Complete");
        }
        if (status.equalsIgnoreCase("cancel")) {
            holder.cardviewGeneralVertBookingHajjAndHotelsItemStatusTv.setText("Cancel");
        }
        if (status.equalsIgnoreCase("in_process")) {
            holder.cardviewGeneralVertBookingHajjAndHotelsItemStatusTv.setText("In Process");
        }
//        holder.cardviewGeneralVertBookingHajjAndHotelsItemStatusTv.setText(getHajjAndUmraItemsListData.get(position).getStatus());

    }

    private void setAction(ViewHolder holder, int position) {

        holder.cardviewBookEVisaOffersItemCompletePaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visaOrCash = getHajjAndUmraItemsListData.get(position).getPaymentType();
                if (visaOrCash.equalsIgnoreCase("Visa")) {

                String url = getHajjAndUmraItemsListData.get(position).getCompletePayment();
                try {
                    Intent i = new Intent("android.intent.action.MAIN");
                    i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
                    i.addCategory("android.intent.category.LAUNCHER");
                    i.setData(Uri.parse(url));
                    activity.startActivity(i);
                }
                catch(ActivityNotFoundException e) {
                    // Chrome is not installed
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    activity.startActivity(i);
                }
            }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

    @Override
    public int getItemCount() {
        return getHajjAndUmraItemsListData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cardview_general_vert_booking_hajj_and_hotels_item_status_tv)
        TextView cardviewGeneralVertBookingHajjAndHotelsItemStatusTv;
        @BindView(R.id.cardview_general_vert_booking_hajj_and_hotels_item_user_name_tv)
        TextView cardviewGeneralVertBookingHajjAndHotelsItemUserNameTv;
        @BindView(R.id.cardview_general_vert_booking_hajj_and_hotels_item_package_name_tv)
        TextView cardviewGeneralVertBookingHajjAndHotelsItemPackageNameTv;
        @BindView(R.id.cardview_general_vert_booking_hajj_and_hotels_item_departure_date_tv)
        TextView cardviewGeneralVertBookingHajjAndHotelsItemDepartureDateTv;
        @BindView(R.id.cardview_general_vert_booking_hajj_and_hotels_item_return_date_tv)
        TextView cardviewGeneralVertBookingHajjAndHotelsItemReturnDateTv;
        @BindView(R.id.cardview_general_vert_booking_hajj_and_hotels_item_total_price_tv)
        TextView cardviewGeneralVertBookingHajjAndHotelsItemTotalPriceTv;
        @BindView(R.id.cardview_general_vert_booking_hajj_and_hotels_item_comment_tv)
        TextView cardviewGeneralVertBookingHajjAndHotelsItemCommentTv;
        @BindView(R.id.cardview_general_vert_booking_hajj_and_hotels_item_status_ly)
        LinearLayout cardviewGeneralVertBookingHajjAndHotelsItemStatusLy;
        @BindView(R.id.cardview_book_e_visa_offers_item_complete_payment_btn)
        TextView cardviewBookEVisaOffersItemCompletePaymentBtn;
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
//            navController = Navigation.findNavController(activity, R.id.home_activity_fragment);

        }
    }
}
