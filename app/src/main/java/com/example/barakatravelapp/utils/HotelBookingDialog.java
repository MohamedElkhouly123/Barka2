package com.example.barakatravelapp.utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.barakatravelapp.R;
import com.example.barakatravelapp.data.model.DateTxt;
import com.example.barakatravelapp.data.model.getDiscoverHomeResponce.GetDiscoverHomeResponce;
import com.example.barakatravelapp.data.model.getHotelsResponce.GetRoom;
import com.example.barakatravelapp.data.model.getHotelsResponce.HotelData;
import com.example.barakatravelapp.data.model.userLoginResponce.UserData;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;

import static com.example.barakatravelapp.data.api.ApiClient.getApiClient;
import static com.example.barakatravelapp.data.local.SharedPreferencesManger.LoadUserData;
import static com.example.barakatravelapp.utils.GeneralRequest.sentUserRateAndBookHotelCallBack;
import static com.example.barakatravelapp.utils.HelperMethod.showCalender;
import static com.example.barakatravelapp.utils.HelperMethod.showToast;
import static com.example.barakatravelapp.utils.validation.Validation.validationLength;
import static com.example.barakatravelapp.utils.validation.Validation.validationPhone;


public class HotelBookingDialog {
    private static int adultNum;
    private DialogAdapterCallback dialogAdapterCallback;
    private TextView checkOutTv,checkInTv,fromDateTv,toDateTv,hotelNameTv,typeTv;
    List<EditText> editTexts = new ArrayList<>();
    private UserData userData;
    private int idPackage,idHotel;
    private DateTxt checkinDate, checkoutDate;
    private Calendar maxDate,minDate;
    public void showDialog(final Activity activity, GetRoom getRoom, HotelData hotelData) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_hotel_booking);
        dialog.setCanceledOnTouchOutside(true);
        userData= LoadUserData(activity);
         hotelNameTv = (TextView) dialog.findViewById(R.id.dialog_hotel_booking_hotel_name_tv);
         typeTv = (TextView) dialog.findViewById(R.id.dialog_hotel_booking_type_name_tv);
         fromDateTv = (TextView) dialog.findViewById(R.id.dialog_hotel_booking_from_date_tv);
        toDateTv = (TextView) dialog.findViewById(R.id.dialog_hotel_booking_to_date_tv);
        checkInTv = (TextView) dialog.findViewById(R.id.dialog_hotel_booking_check_in_btn);
        checkOutTv = (TextView) dialog.findViewById(R.id.dialog_hotel_booking_check_out_btn);
        ImageView hotelBookingCloseBtn = (ImageView) dialog.findViewById(R.id.dialog_hotel_booking_close_btn);
        Button bookNowBtn = (Button) dialog.findViewById(R.id.dialog_hotel_booking_book_now_btn);
        if(getRoom.getIsAvaliable().equalsIgnoreCase("false")) {
            bookNowBtn.setText("No Rooms Left");
//            bookNowBtn.setVisibility(View.GONE);
        }
//        showToast(activity,getRoom.getIsAvaliable() );
            setData(getRoom,hotelData);
        setDates(getRoom,activity);

        hotelBookingCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialogAdapterCallback.onMethodCallback(getHomeDisscoverGetItemsListData.get(position));
                dialog.cancel();
            }
        });

        bookNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!getRoom.getIsAvaliable().equalsIgnoreCase("false")){
                if(checkinDate.getDate_txt()!=null&&checkoutDate.getDate_txt()!=null&&checkoutDate.getDate_txt()!=checkinDate.getDate_txt()){
                onCall(activity,dialog,hotelData,getRoom);
                }else {
                    ToastCreator.onCreateErrorToast(activity, activity.getString(R.string.select_date));
                    return;
                }

            }
            }
        });

        checkInTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!getRoom.getIsAvaliable().equalsIgnoreCase("false")) {

                    showCalender(activity, activity.getString(R.string.select_date), checkInTv, checkinDate, maxDate, minDate);
                }

            }
        });

        checkOutTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!getRoom.getIsAvaliable().equalsIgnoreCase("false")) {

                    showCalender(activity, activity.getString(R.string.select_date), checkOutTv, checkoutDate, maxDate, minDate);

                }
            }
        });

                dialog.show();

            }

    private void setDates(GetRoom getRoom, Activity activity) {
        DecimalFormat mFormat = new DecimalFormat("00");
        Calendar calander = Calendar.getInstance();
        String cDay = mFormat.format(Double.valueOf(String.valueOf(calander.get(Calendar.DAY_OF_MONTH))));
        String cMonth = mFormat.format(Double.valueOf(String.valueOf(calander.get(Calendar.MONTH + 1))));
        String cYear = String.valueOf(calander.get(Calendar.YEAR));

        String [] maxDateParts = getRoom.getToDate().split("-");
        String maxDay = maxDateParts[2];
        String maxMonth = maxDateParts[1];
        String maxYear = maxDateParts[0];

        String [] minDateParts = getRoom.getFromDate().split("-");
        String minDay = minDateParts[2];
        String minMonth = minDateParts[1];
        String minYear = minDateParts[0];
//                showToast(activity, String.valueOf(day));

        maxDate = Calendar.getInstance();
        maxDate.set(Integer.parseInt(maxYear), Integer.parseInt(maxMonth)-1, Integer.parseInt(maxDay));//Year,Mounth -1,Day
        minDate = Calendar.getInstance();
        minDate.set(Integer.parseInt(minYear), Integer.parseInt(minMonth)-1, Integer.parseInt(minDay));//Year,Mounth -1,Day
//        your_date_picker.setMaxDate(c.getTimeInMillis());
//        checkinDate = new DateTxt(cDay, cMonth, cYear, cDay + "-" + cMonth + "-" + cYear);
//        checkinDate = new DateTxt("01", "01", "2020", "01-01-2020");
        int maxDay2 = Integer.parseInt(maxDay)-2;
        String stMaxDate = String.valueOf(maxDay2);
        checkoutDate = new DateTxt(stMaxDate, maxMonth, maxYear, stMaxDate + "-" + maxMonth + "-" + maxYear);
        checkinDate = new DateTxt(stMaxDate, maxMonth, maxYear, stMaxDate + "-" + maxMonth + "-" + maxYear);

    }
    private void setData(GetRoom getRoom, HotelData hotelData) {
        typeTv.setText(getRoom.getName());
        hotelNameTv.setText(hotelData.getName());
        fromDateTv.setText(getRoom.getFromDate());
        toDateTv.setText(getRoom.getToDate());

    }




    private void onCall(Activity activity, Dialog dialog, HotelData hotelData, GetRoom getRoom) {
        String reserfedFrom = checkinDate.getDate_txt();
        String reserfedTo = checkoutDate.getDate_txt();
//        showToas(activity, String.valueOf(checkinDate.getDate_txt()));
        int userId = userData.getId();
        int hotelId = hotelData.getId();
        int roomId = getRoom.getId();
        Call<GetDiscoverHomeResponce> updateItemCal= null;
            updateItemCal = getApiClient().bookHotel(reserfedFrom, reserfedTo, userId, hotelId, roomId);


        sentUserRateAndBookHotelCallBack(activity,updateItemCal,"Success Hotel Booking");
        dialog.cancel();

    }



}
