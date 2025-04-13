package com.mtnl.faultverse;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mtnl.faultverse.databinding.FragmentBroadbandBinding;
import com.mtnl.faultverse.databinding.FragmentLandlineBinding;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class BroadbandFragment extends Fragment {
    FragmentBroadbandBinding binding;
    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private  static final String URL = "jdbc:oracle:thin:@0.tcp.in.ngrok.io:10257:XE";
    private static final String USERNAME = "system";
    private static final String PASSWORD = "sys123";
    String ServiceType = "Broadband";
    String cus_PAcStatus = "Pending";
    String cus_IPAcStatus = "InProg";
    String cus_CAcStatus = "Completed";
    private Connection connection;

    public BroadbandFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);
        ExpandableAdminClass [] expandableAdminClass = new ExpandableAdminClass[10];

        try {
            binding = FragmentBroadbandBinding.inflate(inflater,container,false);
            binding.pendButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark_green)));
            binding.compliButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bg_color)));
            binding.inProgBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bg_color)));
            Class.forName(DRIVER);

            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            Statement statement = connection.createStatement();
            ResultSet resultSet1 = statement.executeQuery("select count(*) from BroadComplaintsPendingView");
            int count = 0;
            if (resultSet1.next()) {
                count += resultSet1.getInt(1);
            }
            Toast.makeText(getActivity(), String.valueOf(count), Toast.LENGTH_LONG).show();
            resultSet1.close();

            String [] cus_PIds = new String[count];
            String []cus_PServs = new String[count];
            String [] cus_PDates = new String[count];
            String [] cus_PNames  = new String[count];
            String [] cus_PSerIds = new String[count];
            String [] cus_PCIds = new String[count];
            String [] cus_PAddr = new String[count];
            String [] cus_PIssue = new String[count];
            String [] cus_PSService = new String[count];
            String [] cus_PDP= new String[count];
            String [] cus_PRE= new String[count];
            String [] cus_PAdsl_IN= new String[count];
            String [] cus_PAdsl_OUT= new String[count];

//            ResultSet resultSet2 = statement.executeQuery("select COMPLAINT_NO,BOOKING_DATETIME,SERVICE_NUMBER,ISSUE,ISSUE_DESCRIPTION,PHONE_NO,DP,RE,ADSL_IN,ADSL_OUT,STATUS,FNAME,LNAME,STREETADDRESS,DISTRICT,PINCODE from LandlineComplaintsPendingView");
            ResultSet resultSet2 = statement.executeQuery("select * from BroadComplaintsPendingView");
            int a=0;
            String compId, bookdt, serv_no, comp_Issue, comp_Issue_Des, phone, dp, re, adslin, adslout, status, sa, dis, pincode, fname, lname, name, address;
            while (resultSet2.next()){
                compId = resultSet2.getString("COMPLAINT_NO");
                bookdt = resultSet2.getString("BOOKING_DATETIME");
                serv_no = resultSet2.getString("SERVICE_NUMBER");
                comp_Issue = resultSet2.getString("ISSUE");
                comp_Issue_Des = resultSet2.getString("ISSUE_DESCRIPTION");
                phone = resultSet2.getString("PHONE_NO");
                dp = resultSet2.getString("DP");
                re = resultSet2.getString("RE");
                adslin = resultSet2.getString("ADSL_IN");
                adslout = resultSet2.getString("ADSL_OUT");
                status = resultSet2.getString("STATUS");
                sa = resultSet2.getString("STREETADDRESS");
                dis = resultSet2.getString("DISTRICT");
                pincode = resultSet2.getString("PINCODE");
                fname = resultSet2.getString("FNAME");
                lname = resultSet2.getString("LNAME");
                name = fname + " " + lname;
                address = sa + ',' + pincode + ',' + dis;
                cus_PIds[a]= compId;
                cus_PDates[a] = bookdt.substring(0,10);
                cus_PSerIds[a] = serv_no;
                cus_PServs[a] = ServiceType;
                cus_PCIds[a] = phone;
                cus_PNames[a] = name;
                cus_PAddr[a] = address;
                cus_PDP[a] = dp;
                cus_PRE[a] =re;
                cus_PAdsl_IN[a] = adslin;
                cus_PAdsl_OUT[a] = adslout;
                cus_PSService[a] = status;
                if (comp_Issue.equalsIgnoreCase("Other")){
                    cus_PIssue[a]= comp_Issue_Des;
                }
                else {
                    cus_PIssue[a]= comp_Issue;
                }
                a++;
            }

            for(int i = 0; i <count;i++){
                expandableAdminClass[i] = new ExpandableAdminClass(cus_PIds[i],cus_PDates[i],cus_PServs[i],cus_PNames[i],cus_PSerIds[i],cus_PCIds[i],cus_PAddr[i],cus_PIssue[i],cus_PSService[i],cus_PDP[i],cus_PRE[i],cus_PAdsl_IN[i],cus_PAdsl_OUT[i],cus_PAcStatus,ServiceType);
            }

            ArrayList<ExpandableAdminClass> complaints = new ArrayList<>();
            for(int i = 0;i<count;i++){
                complaints.add(expandableAdminClass[i]);
            }
            ExpandableAdminAdapter adapter = new ExpandableAdminAdapter(requireActivity(),R.layout.admin_comp_list,complaints);
            binding.broadComplaint.setAdapter(adapter);
            resultSet2.close();
            connection.close();
            statement.close();
        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(), "Error: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        binding.pendButton.setOnClickListener(new View.OnClickListener() {
            private Connection con;
            @Override
            public void onClick(View v) {
                try {
                    binding.complaintView.setText("PENDING");
                    binding.pendButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark_green)));
                    binding.compliButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bg_color)));
                    binding.inProgBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bg_color)));
                    Class.forName(DRIVER);
                    this.con = DriverManager.getConnection(URL, USERNAME, PASSWORD);

                    Statement statement = con.createStatement();
                    ResultSet resultSet1 = statement.executeQuery("select count(*) from BroadComplaintsPendingView");
                    int count = 0;
                    if (resultSet1.next()) {
                        count += resultSet1.getInt(1);
                    }
                    Toast.makeText(getActivity(), String.valueOf(count), Toast.LENGTH_LONG).show();
                    resultSet1.close();

                    String [] cus_PIds = new String[count];
                    String []cus_PServs = new String[count];
                    String [] cus_PDates = new String[count];
                    String [] cus_PNames  = new String[count];
                    String [] cus_PSerIds = new String[count];
                    String [] cus_PCIds = new String[count];
                    String [] cus_PAddr = new String[count];
                    String [] cus_PIssue = new String[count];
                    String [] cus_PSService = new String[count];
                    String [] cus_PDP= new String[count];
                    String [] cus_PRE= new String[count];
                    String [] cus_PAdsl_IN= new String[count];
                    String [] cus_PAdsl_OUT= new String[count];

//            ResultSet resultSet2 = statement.executeQuery("select COMPLAINT_NO,BOOKING_DATETIME,SERVICE_NUMBER,ISSUE,ISSUE_DESCRIPTION,PHONE_NO,DP,RE,ADSL_IN,ADSL_OUT,STATUS,FNAME,LNAME,STREETADDRESS,DISTRICT,PINCODE from LandlineComplaintsPendingView");
                    ResultSet resultSet2 = statement.executeQuery("select * from BroadComplaintsPendingView");
                    int a=0;
                    String compId, bookdt, serv_no, comp_Issue, comp_Issue_Des, phone, dp, re, adslin, adslout, status, sa, dis, pincode, fname, lname, name, address;
                    while (resultSet2.next()){
                        compId = resultSet2.getString("COMPLAINT_NO");
                        bookdt = resultSet2.getString("BOOKING_DATETIME");
                        serv_no = resultSet2.getString("SERVICE_NUMBER");
                        comp_Issue = resultSet2.getString("ISSUE");
                        comp_Issue_Des = resultSet2.getString("ISSUE_DESCRIPTION");
                        phone = resultSet2.getString("PHONE_NO");
                        dp = resultSet2.getString("DP");
                        re = resultSet2.getString("RE");
                        adslin = resultSet2.getString("ADSL_IN");
                        adslout = resultSet2.getString("ADSL_OUT");
                        status = resultSet2.getString("STATUS");
                        sa = resultSet2.getString("STREETADDRESS");
                        dis = resultSet2.getString("DISTRICT");
                        pincode = resultSet2.getString("PINCODE");
                        fname = resultSet2.getString("FNAME");
                        lname = resultSet2.getString("LNAME");
                        name = fname + " " + lname;
                        address = sa + ',' + pincode + ',' + dis;
                        cus_PIds[a]= compId;
                        cus_PDates[a] = bookdt.substring(0,10);
                        cus_PSerIds[a] = serv_no;
                        cus_PServs[a] = ServiceType;
                        cus_PCIds[a] = phone;
                        cus_PNames[a] = name;
                        cus_PAddr[a] = address;
                        cus_PDP[a] = dp;
                        cus_PRE[a] =re;
                        cus_PAdsl_IN[a] = adslin;
                        cus_PAdsl_OUT[a] = adslout;
                        cus_PSService[a] = status;
                        if (comp_Issue.equalsIgnoreCase("Other")){
                            cus_PIssue[a]= comp_Issue_Des;
                        }
                        else {
                            cus_PIssue[a]= comp_Issue;
                        }
                        a++;
                    }
                    for(int i = 0; i <count;i++){
                        expandableAdminClass[i] = new ExpandableAdminClass(cus_PIds[i],cus_PDates[i],cus_PServs[i],cus_PNames[i],cus_PSerIds[i],cus_PCIds[i],cus_PAddr[i],cus_PIssue[i],cus_PSService[i],cus_PDP[i],cus_PRE[i],cus_PAdsl_IN[i],cus_PAdsl_OUT[i],cus_PAcStatus,ServiceType);
                    }

                    ArrayList<ExpandableAdminClass> complaints = new ArrayList<>();
                    for(int i = 0;i<count;i++){
                        complaints.add(expandableAdminClass[i]);
                    }
                    ExpandableAdminAdapter adapter = new ExpandableAdminAdapter(requireActivity(),R.layout.admin_comp_list,complaints);
                    binding.broadComplaint.setAdapter(adapter);
                    con.close();
                    resultSet2.close();
                    statement.close();
                }
                catch (Exception e)
                {
                    Toast.makeText(getActivity(), "Error: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.compliButton.setOnClickListener(new View.OnClickListener() {
            private Connection conn;
            @Override
            public void onClick(View v) {
                try{
                    binding.complaintView.setText("COMPLETED");
                    binding.compliButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark_green)));
                    binding.pendButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bg_color)));
                    binding.inProgBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bg_color)));
                    Class.forName(DRIVER);
                    this.conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

                    Statement statement = conn.createStatement();
                    ResultSet resultSet1 = statement.executeQuery("select count(*) from BroadComplaintsClearedView ");
                    int count = 0;
                    if (resultSet1.next()) {
                        count += resultSet1.getInt(1);
                    }
                    Toast.makeText(getActivity(), String.valueOf(count), Toast.LENGTH_LONG).show();
                    resultSet1.close();

                    String [] cus_CIds = new String[count];
                    String []cus_CServs = new String[count];
                    String [] cus_CDates = new String[count];
                    String [] cus_CNames  = new String[count];
                    String [] cus_CSerIds = new String[count];
                    String [] cus_CCIds = new String[count];
                    String [] cus_CAddr = new String[count];
                    String [] cus_CIssue = new String[count];
                    String [] cus_CSService = new String[count];
                    String [] cus_CDP= new String[count];
                    String [] cus_CRE= new String[count];
                    String [] cus_CAdsl_IN= new String[count];
                    String [] cus_CAdsl_OUT= new String[count];

//            ResultSet resultSet2 = statement.executeQuery("select COMPLAINT_NO,BOOKING_DATETIME,SERVICE_NUMBER,ISSUE,ISSUE_DESCRIPTION,PHONE_NO,DP,RE,ADSL_IN,ADSL_OUT,STATUS,FNAME,LNAME,STREETADDRESS,DISTRICT,PINCODE from LandlineComplaintsClearedView ");
                    ResultSet resultSet2 = statement.executeQuery("select * from BroadComplaintsClearedView ");
                    int a=0;
                    String compId, bookdt, serv_no, comp_Issue, comp_Issue_Des, phone, dp, re, adslin, adslout, status, sa, dis, pincode, fname, lname, name, address;
                    while (resultSet2.next()){
                        compId = resultSet2.getString("COMPLAINT_NO");
                        bookdt = resultSet2.getString("BOOKING_DATETIME");
                        serv_no = resultSet2.getString("SERVICE_NUMBER");
                        comp_Issue = resultSet2.getString("ISSUE");
                        comp_Issue_Des = resultSet2.getString("ISSUE_DESCRIPTION");
                        phone = resultSet2.getString("PHONE_NO");
                        dp = resultSet2.getString("DP");
                        re = resultSet2.getString("RE");
                        adslin = resultSet2.getString("ADSL_IN");
                        adslout = resultSet2.getString("ADSL_OUT");
                        status = resultSet2.getString("STATUS");
                        sa = resultSet2.getString("STREETADDRESS");
                        dis = resultSet2.getString("DISTRICT");
                        pincode = resultSet2.getString("PINCODE");
                        fname = resultSet2.getString("FNAME");
                        lname = resultSet2.getString("LNAME");
                        name = fname + " " + lname;
                        address = sa + ',' + pincode + ',' + dis;
                        cus_CIds[a]= compId;
                        cus_CDates[a] = bookdt.substring(0,10);
                        cus_CSerIds[a] = serv_no;
                        cus_CServs[a] = ServiceType;
                        cus_CCIds[a] = phone;
                        cus_CNames[a] = name;
                        cus_CAddr[a] = address;
                        cus_CDP[a] = dp;
                        cus_CRE[a] =re;
                        cus_CAdsl_IN[a] = adslin;
                        cus_CAdsl_OUT[a] = adslout;
                        cus_CSService[a] = status;
                        if (comp_Issue.equalsIgnoreCase("Other")){
                            cus_CIssue[a]= comp_Issue_Des;
                        }
                        else {
                            cus_CIssue[a]= comp_Issue;
                        }
                        a++;
                    }
                    for(int i = 0; i <count;i++){
                        expandableAdminClass[i] = new ExpandableAdminClass(cus_CIds[i],cus_CDates[i],cus_CServs[i],cus_CNames[i],cus_CSerIds[i],cus_CCIds[i],cus_CAddr[i],cus_CIssue[i],cus_CSService[i],cus_CDP[i],cus_CRE[i],cus_CAdsl_IN[i],cus_CAdsl_OUT[i],cus_CAcStatus,ServiceType);
                    }

                    ArrayList<ExpandableAdminClass> complaints = new ArrayList<>();
                    for(int i = 0;i<count;i++){
                        complaints.add(expandableAdminClass[i]);
                    }
                    ExpandableAdminAdapter adapter = new ExpandableAdminAdapter(requireActivity(),R.layout.admin_comp_list,complaints);
                    binding.broadComplaint.setAdapter(adapter);
                    resultSet2.close();
                    conn.close();
                    statement.close();
                }
                catch (Exception e)
                {
                    Toast.makeText(getActivity(), "Error: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.inProgBtn.setOnClickListener(new View.OnClickListener() {
            private Connection con1;
            @Override
            public void onClick(View v) {
                try{
                    binding.complaintView.setText("IN PROGRESS");
                    binding.inProgBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark_green)));
                    binding.pendButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bg_color)));
                    binding.compliButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bg_color)));
                    Class.forName(DRIVER);
                    this.con1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);

                    Statement statement = con1.createStatement();
                    ResultSet resultSet1 = statement.executeQuery("select count(*) from BroadComplaintInprogress ");
                    int count = 0;
                    if (resultSet1.next()) {
                        count += resultSet1.getInt(1);
                    }
                    Toast.makeText(getActivity(), String.valueOf(count), Toast.LENGTH_LONG).show();
                    resultSet1.close();

                    String [] cus_IPIds = new String[count];
                    String []cus_IPServs = new String[count];
                    String [] cus_IPDates = new String[count];
                    String [] cus_IPNames  = new String[count];
                    String [] cus_IPSerIds = new String[count];
                    String [] cus_IPCIds = new String[count];
                    String [] cus_IPAddr = new String[count];
                    String [] cus_IPIssue = new String[count];
                    String [] cus_IPSService = new String[count];
                    String [] cus_IPDP= new String[count];
                    String [] cus_IPRE= new String[count];
                    String [] cus_IPAdsl_IN= new String[count];
                    String [] cus_IPAdsl_OUT= new String[count];

//            ResultSet resultSet2 = statement.executeQuery("select COMPLAINT_NO,BOOKING_DATETIME,SERVICE_NUMBER,ISSUE,ISSUE_DESCRIPTION,PHONE_NO,DP,RE,ADSL_IN,ADSL_OUT,STATUS,FNAME,LNAME,STREETADDRESS,DISTRICT,PINCODE from LandlineComplaintsPendingView");
                    ResultSet resultSet2 = statement.executeQuery("select * from BroadComplaintInprogress ");
                    int a=0;
                    String compId, bookdt, serv_no, comp_Issue, comp_Issue_Des, phone, dp, re, adslin, adslout, status, sa, dis, pincode, fname, lname, name, address;
                    while (resultSet2.next()){
                        compId = resultSet2.getString("COMPLAINT_NO");
                        bookdt = resultSet2.getString("BOOKING_DATETIME");
                        serv_no = resultSet2.getString("SERVICE_NUMBER");
                        comp_Issue = resultSet2.getString("ISSUE");
                        comp_Issue_Des = resultSet2.getString("ISSUE_DESCRIPTION");
                        phone = resultSet2.getString("PHONE_NO");
                        dp = resultSet2.getString("DP");
                        re = resultSet2.getString("RE");
                        adslin = resultSet2.getString("ADSL_IN");
                        adslout = resultSet2.getString("ADSL_OUT");
                        status = resultSet2.getString("STATUS");
                        sa = resultSet2.getString("STREETADDRESS");
                        dis = resultSet2.getString("DISTRICT");
                        pincode = resultSet2.getString("PINCODE");
                        fname = resultSet2.getString("FNAME");
                        lname = resultSet2.getString("LNAME");
                        name = fname + " " + lname;
                        address = sa + ',' + pincode + ',' + dis;
                        cus_IPIds[a]= compId;
                        cus_IPDates[a] = bookdt.substring(0,10);
                        cus_IPSerIds[a] = serv_no;
                        cus_IPServs[a] = ServiceType;
                        cus_IPCIds[a] = phone;
                        cus_IPNames[a] = name;
                        cus_IPAddr[a] = address;
                        cus_IPDP[a] = dp;
                        cus_IPRE[a] =re;
                        cus_IPAdsl_IN[a] = adslin;
                        cus_IPAdsl_OUT[a] = adslout;
                        cus_IPSService[a] = status;
                        if (comp_Issue.equalsIgnoreCase("Other")){
                            cus_IPIssue[a]= comp_Issue_Des;
                        }
                        else {
                            cus_IPIssue[a]= comp_Issue;
                        }
                        a++;
                    }
                    for(int i = 0; i <count;i++){
                        expandableAdminClass[i] = new ExpandableAdminClass(cus_IPIds[i],cus_IPDates[i],cus_IPServs[i],cus_IPNames[i],cus_IPSerIds[i],cus_IPCIds[i],cus_IPAddr[i],cus_IPIssue[i],cus_IPSService[i],cus_IPDP[i],cus_IPRE[i],cus_IPAdsl_IN[i],cus_IPAdsl_OUT[i],cus_IPAcStatus,ServiceType);
                    }

                    ArrayList<ExpandableAdminClass> complaints = new ArrayList<>();
                    for(int i = 0;i<count;i++){
                        complaints.add(expandableAdminClass[i]);
                    }
                    ExpandableAdminAdapter adapter = new ExpandableAdminAdapter(requireActivity(),R.layout.admin_comp_list,complaints);
                    binding.broadComplaint.setAdapter(adapter);
                    resultSet2.close();
                    con1.close();
                    statement.close();
                }
                catch (Exception e)
                {
                    Toast.makeText(getActivity(), "Error: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return binding.getRoot();
    }
}