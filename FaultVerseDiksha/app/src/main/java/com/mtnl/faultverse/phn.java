package com.mtnl.faultverse;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class phn extends BottomSheetDialogFragment {
    private long loginPhNo=0;
    private TextView textView36,editTextNumber2;
    private Button phn_up;
    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@0.tcp.in.ngrok.io:10257:XE";
    private static final String USERNAME = "system";
    private static final String PASSWORD = "sys123";
    private Connection connection;
    public phn() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        textView36 = view.findViewById(R.id.textView36);
        phn_up = view.findViewById(R.id.phn_up);
        editTextNumber2 = view.findViewById(R.id.editTextNumber2);

        View bottomSheet = (View) view.getParent();
        bottomSheet.setBackgroundTintMode(PorterDuff.Mode.CLEAR);
        bottomSheet.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
        bottomSheet.setBackgroundColor(Color.TRANSPARENT);
        Bundle args = getArguments();
        if (args != null) {
            loginPhNo = args.getLong("loginPhNo");
            // Use the loginPhNo value as needed
        }
        Toast.makeText(getActivity(), ""+loginPhNo, Toast.LENGTH_SHORT).show();
        String number = String.valueOf(loginPhNo);
        textView36.setText(number);
        phn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                action(view);
            }
        });
    }

    public void action(View view) {
        String new_phn = editTextNumber2.getText().toString();
        long new_ph = Long.parseLong(new_phn);
        String number;
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            PreparedStatement statement = connection.prepareStatement("UPDATE tbl_customers SET phone_no = ? WHERE phone_no = ?");
            statement.setLong(1, new_ph);
            statement.setLong(2, loginPhNo);
            int count = statement.executeUpdate();
            if (count > 0) {
                Toast.makeText(getActivity(), "Record Updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Connection Failed", Toast.LENGTH_SHORT).show();
            }
            if(count>0){
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
            connection.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_phn, container, false);
    }
}