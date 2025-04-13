package com.mtnl.faultverse;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.mtnl.faultverse.databinding.FragmentProfileBinding;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    public static long loginPhNo;
    public static final String EXTRA_KEY = "MTNLdb";
    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@0.tcp.in.ngrok.io:10257:XE";
    private static final String USERNAME = "system";
    private static final String PASSWORD = "sys123";
    private Connection connection;
    public  ProfileFragment () {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        Bundle data = getArguments();//from homeactivity.java
        if (data != null) {
            loginPhNo = data.getLong(HomeActivity.EXTRA_KEY);
        }

        String Phn = String.valueOf(loginPhNo);
        Toast.makeText(getActivity(), ""+Phn, Toast.LENGTH_SHORT).show();
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        try {

            binding = FragmentProfileBinding.inflate(inflater, container, false);
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet1 = statement.executeQuery("select fname,lname,email,dob,streetaddress from tbl_customers where phone_no = " + loginPhNo + "");
            if (resultSet1.next()) {
                String fname = resultSet1.getString("fname");
                String lname = resultSet1.getString("lname");
                String email = resultSet1.getString("email");
                String add = resultSet1.getString("streetaddress");
                String dob = resultSet1.getString("dob");
                binding.textView27.setText(fname + " " + lname);
                binding.textView29.setText(email);
                binding.phnNo1.setText(String.valueOf(loginPhNo));
                binding.emailAddr.setText(email);
                binding.FirstName.setText(fname);
                binding.LastName.setText(lname);
                //binding.DOB.setText(dob.substring(0,11));
                binding.Address.setText(add);
            }
            connection.close();
            resultSet1.close();
            statement.close();
        }catch (Exception e){
            Toast.makeText(getActivity(), "Error"+e.toString(), Toast.LENGTH_SHORT).show();
        }

        binding.chngPhn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phn phnFragment = new phn();
                Bundle args = new Bundle();
                args.putLong("loginPhNo", loginPhNo);
                phnFragment.setArguments(args);
                phnFragment.show(getChildFragmentManager(),phnFragment.getTag());

            }
        });
        binding.chngEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email emailFragment = new email();
                Bundle args = new Bundle();
                args.putLong("loginPhNo", loginPhNo);
                emailFragment.setArguments(args);
                emailFragment.show(getChildFragmentManager(),emailFragment.getTag());
            }
        });
        binding.chngFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                first firstFragment = new first();
                Bundle args = new Bundle();
                args.putLong("loginPhNo", loginPhNo);
                firstFragment.setArguments(args);
                firstFragment.show(getChildFragmentManager(),firstFragment.getTag());

            }
        });
        binding.chngLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                last lastFragment = new last();
                Bundle args = new Bundle();
                args.putLong("loginPhNo", loginPhNo);
                lastFragment.setArguments(args);
                lastFragment.show(getChildFragmentManager(),lastFragment.getTag());

            }
        });
//        binding.chngDob.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dob dobFragment = new dob();
//                Bundle args = new Bundle();
//                args.putLong("loginPhNo", loginPhNo);
//                dobFragment.setArguments(args);
//                dobFragment.show(getChildFragmentManager(),dobFragment.getTag());
//            }
//        });
        binding.chngAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                add addFragment = new add();
                Bundle args = new Bundle();
                args.putLong("loginPhNo", loginPhNo);
                addFragment.setArguments(args);
                addFragment.show(getChildFragmentManager(),addFragment.getTag());
            }
        });
        return binding.getRoot();


    }
}