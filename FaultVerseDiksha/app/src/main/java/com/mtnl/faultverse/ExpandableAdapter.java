package com.mtnl.faultverse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;

public class ExpandableAdapter extends ArrayAdapter<ExpandableClass>{
    private static final String TAG= "ExpandableAdapter";

    private Context mContext;
    int mResource;

    public ExpandableAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ExpandableClass> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String id = getItem(position).getID();
        String ser = getItem(position).getService();
        String date = getItem(position).getDate();
        String remark = getItem(position).getRemark();
        String cdate = getItem(position).getCDate();
        String issue = getItem(position).getIssue();

        ExpandableClass expandableClass = new ExpandableClass(id,ser,date,remark,cdate,issue);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);

        TextView Id = convertView.findViewById(R.id.cusIdView);
        TextView Service = convertView.findViewById(R.id.cus_SerView);
        TextView Date = convertView.findViewById(R.id.cus_DateView);
        TextView Remark = convertView.findViewById(R.id.cus_RemarkView);
        TextView CDate = convertView.findViewById(R.id.cus_ClearedDateView);
        TextView Issue = convertView.findViewById(R.id.cus_IssueDescView);
        ImageView ServiceImage = convertView.findViewById(R.id.adminServiceVIew);
        CardView cardView = convertView.findViewById(R.id.adminCompCardView);
        LinearLayout hiddenLayout = convertView.findViewById(R.id.hiddenLayout);
        LinearLayout hiddenLayout1 = convertView.findViewById(R.id.hiddenLayout2);
        LinearLayout hiddenLayout2 = convertView.findViewById(R.id.hiddenLayout3);
        TextView Viewmoreless = convertView.findViewById(R.id.ViewMORELESS11);


        Id.setText(id);
        Service.setText(ser);
        Date.setText(date);
        Remark.setText(remark);
        CDate.setText(cdate);
        Issue.setText(issue);


        if(Service.getText().toString().equals("Broadband")){
            ServiceImage.setImageResource(R.drawable.broadband_white);
        }
        else if(Service.getText().toString().equals("FTTH")){
            ServiceImage.setImageResource(R.drawable.ftth_white);
        }

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hiddenLayout1.getVisibility()== View.GONE && hiddenLayout.getVisibility()== View.GONE&& hiddenLayout2.getVisibility()== View.GONE){
                    hiddenLayout1.setVisibility(View.VISIBLE);
                    hiddenLayout.setVisibility(View.VISIBLE);
                    hiddenLayout2.setVisibility(View.VISIBLE);
                    Viewmoreless.setText("View Less");
                }
                else{
                    hiddenLayout1.setVisibility(View.GONE);
                    hiddenLayout.setVisibility(View.GONE);
                    hiddenLayout2.setVisibility(View.GONE);
                    Viewmoreless.setText("View More");
                }
            }
        });



        return convertView;
    }
}
