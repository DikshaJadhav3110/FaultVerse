package com.mtnl.faultverse;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.mtnl.faultverse.databinding.FragmentHistoryBinding;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    FragmentHistoryBinding binding;
    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private  static final String URL = "jdbc:oracle:thin:@0.tcp.in.ngrok.io:10257:XE";
    private static final String USERNAME = "system";
    private static final String PASSWORD = "sys123";
    private static long loginPhNo ;
    public static final String EXTRA_KEY = "MTNLdb";

    private Connection connection;
    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        Bundle data = getArguments();
        if (data != null) {
            loginPhNo = data.getLong(HomeActivity.EXTRA_KEY);
        }
        String Phn = String.valueOf(loginPhNo);

        binding = FragmentHistoryBinding.inflate(inflater, container, false);

        ExpandableClass [] expandableClass = new ExpandableClass[10];
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        try {
            Class.forName(DRIVER);

            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            Statement statement = connection.createStatement();
            ResultSet resultSet1 = statement.executeQuery("select count(*) from tbl_complainthistory where phone_no = "+loginPhNo);
            int count = 0;
            if (resultSet1.next()) {
                count += resultSet1.getInt(1);
            }
            Toast.makeText(getActivity(), String.valueOf(count), Toast.LENGTH_LONG).show();
            resultSet1.close();

            String [] cus_Issue = new String[count];
            String []cus_ClearedDates = new String[count];
            String [] cus_CRemark = new String[count];
            String [] cus_CDates  = new String[count];
            String [] cus_CServs = new String[count];
            String [] cus_CIds = new String[count];

            ResultSet resultSet2 = statement.executeQuery("SELECT complaint_no, booking_datetime, type_of_service, issue, ISSUE_DESCRIPTION, REMARK, CLEARED1_DATETIME FROM tbl_complainthistory WHERE phone_no = " + loginPhNo + " ORDER BY booking_datetime DESC");
            int a=0;
            String issue,cleardate,remark,bookdate,service,com_no,issue_des;
            while (resultSet2.next()){
                issue = resultSet2.getString("ISSUE");
                bookdate = resultSet2.getString("BOOKING_DATETIME");
                cleardate = resultSet2.getString("CLEARED1_DATETIME");
                issue_des = resultSet2.getString("ISSUE_DESCRIPTION");
                remark = resultSet2.getString("REMARK");
                service = resultSet2.getString("TYPE_OF_SERVICE");
                com_no = resultSet2.getString("COMPLAINT_NO");
                cus_ClearedDates[a] = cleardate.substring(0,10);
                cus_CRemark[a] = remark;
                cus_CDates[a] = bookdate.substring(0,10);
                cus_CServs[a] = service;
                cus_CIds[a] = com_no;
                if (issue.equalsIgnoreCase("Other")){
                    cus_Issue[a]= issue_des;
                }
                else {
                    cus_Issue[a]= issue;
                }
                a++;
            }

            for(int i = 0; i < count ;i++){
                expandableClass[i] = new ExpandableClass(cus_CIds[i],cus_CServs[i],cus_CDates[i],cus_CRemark[i],cus_ClearedDates[i],cus_Issue[i]);
            }

            ArrayList<ExpandableClass> complaints = new ArrayList<>();
            for(int i = 0;i<count ;i++){
                complaints.add(expandableClass[i]);
            }
            ExpandableAdapter adapter = new ExpandableAdapter(requireActivity(),R.layout.comp_list,complaints);
            binding.compListView.setAdapter(adapter);

            binding.compListView.setDivider(null);
            resultSet1.close();
            connection.close();
            statement.close();
        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(), "Error: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return binding.getRoot();
    }
}