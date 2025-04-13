package com.mtnl.faultverse;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ExpandableAdminAdapter extends ArrayAdapter<ExpandableAdminClass> {
    private static final String TAG= "ExpandableAdminAdapter";

    private Context mContext;
    int mResource;

    public ExpandableAdminAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ExpandableAdminClass> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        String id = getItem(position).getComplaintID();
        String ser = getItem(position).getService();
        String date = getItem(position).getDate();
        String name = getItem(position).getCustomerName();
        String serID = getItem(position).getServiceID();
        String cusID = getItem(position).getCustomerID();
        String cusAddr = getItem(position).getCustomerAddr();
        String issue = getItem(position).getIssue();
        String statusService = getItem(position).getStatusService();
        String dp = getItem(position).getDP();
        String re = getItem(position).getRE();
        String adsl_in = getItem(position).getADSL_IN();
        String adsl_out = getItem(position).getADSL_OUT();
        String acStatus = getItem(position).getACStatus();
        String servicetype = getItem(position).getServiceType();


        ExpandableAdminClass expandableClass = new ExpandableAdminClass(id,ser,date,name,serID,cusID,cusAddr,issue,statusService,dp,re,adsl_in,adsl_out,acStatus,servicetype);


        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);


        TextView Id = convertView.findViewById(R.id.admin_cus_CompId);
        TextView Service = convertView.findViewById(R.id.cus_SerView);
        TextView Date = convertView.findViewById(R.id.cus_DateView);
        TextView CusName = convertView.findViewById(R.id.admin_cus_Name);
        TextView SerID = convertView.findViewById(R.id.admin_cus_ser_ID);
        TextView CusID = convertView.findViewById(R.id.cusIdView);
        TextView CusAddr = convertView.findViewById(R.id.admin_cus_Addr);
        TextView CusIssue = convertView.findViewById(R.id.admin_Cus_Issue);
        TextView CusServiceStatus = convertView.findViewById(R.id.admin_cus_Ser);
        TextView CusDP = convertView.findViewById(R.id.admin_cus_DP);
        TextView CusRE = convertView.findViewById(R.id.admin_cus_RE);
        TextView CusAdsl_IN = convertView.findViewById(R.id.admin_Cus_ADIN);
        TextView CusAdsl_out = convertView.findViewById(R.id.admin_cus_ADOUT);
        TextView CusAcStatus= convertView.findViewById(R.id.admin_Cus_ACStatus);


        TextView ViewMore = convertView.findViewById(R.id.ViewMORELESS11);
        LinearLayout mulgaLayout = convertView.findViewById(R.id.adminHiddenLayout);
        LinearLayout aaiLayout = convertView.findViewById(R.id.adminCompLinearL);
        Button takeBtn = convertView.findViewById(R.id.takeBtn);
        Button clrBtn = convertView.findViewById(R.id.clrBtn);
        EditText editRemark = convertView.findViewById(R.id.editRemarkText);
        ImageView adminServiceView = convertView.findViewById(R.id.adminServiceVIew);


        Id.setText(id);
        Service.setText(ser);
        Date.setText(date);
        CusName.setText(name);
        SerID.setText(serID);
        CusID.setText(cusID);
        CusAddr.setText(cusAddr);
        CusIssue.setText(issue);
        CusServiceStatus.setText(statusService);
        CusDP.setText(dp);
        CusRE.setText(re);
        CusAdsl_IN.setText(adsl_in);
        CusAdsl_out.setText(adsl_out);
        CusAcStatus.setText(acStatus);


        if(servicetype.equals("Broadband")){
            adminServiceView.setImageResource(R.drawable.broadband_white);
        }
        else if(servicetype.equals("FTTH")){
            adminServiceView.setImageResource(R.drawable.ftth_white);
        }

        if(CusAcStatus.getText().toString().equals("Pending")){
            takeBtn.setVisibility(View.VISIBLE);
        }
        else if(CusAcStatus.getText().toString().equals("InProg")){
            clrBtn.setVisibility(View.VISIBLE);
            editRemark.setVisibility(View.VISIBLE);
        }

        aaiLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mulgaLayout.getVisibility()== View.GONE){
                    TransitionManager.beginDelayedTransition(aaiLayout,new AutoTransition());
                    mulgaLayout.setVisibility(View.VISIBLE);
                    ViewMore.setText("View Less");
                }
                else{
                    TransitionManager.beginDelayedTransition(aaiLayout,new AutoTransition());
                    mulgaLayout.setVisibility(View.GONE);
                    ViewMore.setText("View More");
                }
            }
        });



        takeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Toast for Take", Toast.LENGTH_SHORT).show();
            }
        });

        clrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Toast for Clear", Toast.LENGTH_SHORT).show();
            }
        });


        return convertView;
    }
}
