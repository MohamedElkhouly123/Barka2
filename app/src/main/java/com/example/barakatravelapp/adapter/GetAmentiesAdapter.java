package com.example.barakatravelapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barakatravelapp.R;
import com.example.barakatravelapp.data.model.getAmenitiesResponce.GetAmenity2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.barakatravelapp.utils.HelperMethod.showToast;

public class GetAmentiesAdapter extends RecyclerView.Adapter<GetAmentiesAdapter.ViewHolder> {



    private Context context;
    private Activity activity;
    //    private List<String> oldBloodTypes = new ArrayList<>();
    private List<GetAmenity2> getAmenity2s = new ArrayList<>();
    public List<Integer> ids = new ArrayList<>();

    public GetAmentiesAdapter(Context context, Activity activity, List<GetAmenity2> getAmenity2s) {
        ids.clear();
        this.context = context;
        this.activity = activity;
        this.getAmenity2s = getAmenity2s;
//        this.oldBloodTypes = oldBloodTypes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.check_box_amenty_item,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.setIsRecyclable(false);
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {

        try {

//            holder.checkboxItemChbx.setChecked(false);

            holder.checkBoxAmentyItemCbRemember.setChecked(false);
//            ids.add(getAmenity2s.get(position).getId());
            holder.checkBoxAmentyItemNameTv.setText(getAmenity2s.get(position).getName());

        } catch (Exception e) {

        }
    }

    private void setAction(ViewHolder holder, final int position) {

        try {

            holder.checkBoxAmentyItemCbRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        ids.add(getAmenity2s.get(position).getId());
                    } else {
                        for (int i = 0; i < ids.size(); i++) {
                            if (ids.get(i).equals(getAmenity2s.get(position).getId())) {
                                ids.remove(i);
                            }
                        }
                    }
                }
            });

        } catch (Exception e) {

        }

    }

    @Override
    public int getItemCount() {
        return getAmenity2s.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.check_box_amenty_item_cb_remember)
        CheckBox checkBoxAmentyItemCbRemember;
        @BindView(R.id.check_box_amenty_item_name_tv)
        TextView checkBoxAmentyItemNameTv;
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}
