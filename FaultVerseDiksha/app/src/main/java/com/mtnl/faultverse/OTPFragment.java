package com.mtnl.faultverse;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mtnl.faultverse.databinding.FragmentOTPBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.github.muddz.styleabletoast.StyleableToast;

public class OTPFragment extends BottomSheetDialogFragment {
    FragmentOTPBinding binding;
    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds = 120000;
    private boolean timerRunning;
    static String loginPhnNo;
    static long loginPhNo;
    static long signupNo;
    public static final String EXTRA_KEY = "MTNLdb";
    String getOtp;
    static int VCount;


    //database
    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private  static final String URL = "jdbc:oracle:thin:@0.tcp.in.ngrok.io:10257:XE";
    private static final String USERNAME = "system";
    private static final String PASSWORD = "sys123";
    static String role = null ;
    public static final String EXTRA_KEY3 = "role";

    private Bundle bundle;
    public static final String EXTRA_KEY4 = "extrabundle";
    private Connection connection;

    public OTPFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        loginPhNo =9820309291l;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        View bottomSheet = (View) view.getParent();
        bottomSheet.setBackgroundTintMode(PorterDuff.Mode.CLEAR);
        bottomSheet.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
        bottomSheet.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOTPBinding.inflate(inflater, container, false);
        Bundle bundle1 = this.getArguments();
        assert bundle1 != null;
        loginPhnNo = bundle1.getString("MtnlLoginNumber");
        getOtp = bundle1.getString("MtnlOTPNumber");
        VCount = bundle1.getInt("VerificationCount",LoginActivity.VCount);
        if(VCount==1) {
            bundle = bundle1.getBundle(LoginActivity.EXTRA_BUNDLE);
            signupNo = bundle.getLong(LoginActivity.EXTRA_KEY2);
            loginPhNo = bundle.getLong(LoginActivity.EXTRA_KEY2);
        }
        else if(VCount==0){
            signupNo = 0;
            loginPhNo = bundle1.getLong("DataLogin",LoginActivity.loginPhNo);
        }



        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        binding.phoneNumberView.setText(loginPhnNo);

        startStop();

        binding.verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onVerify();
            }
        });

        binding.resendOtpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        loginPhnNo,
                        120,
                        TimeUnit.SECONDS,
                        getActivity(),
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                StyleableToast.makeText(getActivity(), e.getMessage(), R.style.errorStyleToast).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String backEndOtp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                StyleableToast.makeText(getActivity(), "Code Resend", R.style.errorStyleToast).show();
                            }
                        }
                );
            }
        });
        return binding.getRoot();
    }

    public void startStop() {
        if (timerRunning) {
            stopTimer();
        } else {
            startTimer();
        }
    }

    public void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMilliseconds = l;
                updateTimer();
            }

            @Override
            public void onFinish() {

            }
        }.start();
        timerRunning = true;
    }

    public void stopTimer() {
        countDownTimer.cancel();
        timerRunning = false;
    }

    public void updateTimer() {
        int minutes = (int) timeLeftInMilliseconds / 60000;
        int seconds = (int) timeLeftInMilliseconds % 60000 / 1000;

        String timeLeftText = "" + minutes;
        timeLeftText += ":";
        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;

        binding.timerView.setText(timeLeftText);
        if (seconds == 45) {
            binding.resendOtpText.setVisibility(View.VISIBLE);
        }
        numberotpmove();
    }

    public void onVerify() {
        if (!binding.input1.getText().toString().trim().isEmpty() && !binding.input2.getText().toString().trim().isEmpty() && !binding.input3.getText().toString().trim().isEmpty() && !binding.input4.getText().toString().trim().isEmpty() && !binding.input5.getText().toString().trim().isEmpty() && !binding.input6.getText().toString().trim().isEmpty()) {
            String enterCodeOtp = binding.input1.getText().toString() +
                    binding.input2.getText().toString() +
                    binding.input3.getText().toString() +
                    binding.input4.getText().toString() +
                    binding.input5.getText().toString() +
                    binding.input6.getText().toString();

            if (getOtp != null) {

                PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                        getOtp, enterCodeOtp
                );

                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    StyleableToast.makeText(getActivity(), "You Are Verified", R.style.errorStyleToast).show();

                                        if(signupNo==0){
                                            try {
                                                Class.forName(DRIVER);
                                                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

//                        PreparedStatement statement = connection.prepareStatement("SELECT role FROM tbl_customers WHERE phone_no = ?");
//                        statement.setLong(1, loginPhNo);
//                        ResultSet resultSet = statement.executeQuery();

//                        if (resultSet.next()) {
//                            role = resultSet.getString("role");
//                            Toast.makeText(this, "" + role, Toast.LENGTH_SHORT).show();
//                        }

                                                CallableStatement statement = connection.prepareCall("{call LOGIN_PROCEDURE(?, ?, ?)}");
                                                statement.setLong(1, loginPhNo);
                                                statement.setLong(2, signupNo);
                                                statement.registerOutParameter(3, Types.VARCHAR);
                                                statement.execute();

                                                // Retrieve the output parameter value
                                                role = statement.getString(3);
                                                statement.close();

                                                if (role != null) {
                                                    if (role.equals("customer")) {
                                                        Toast.makeText(getActivity(), "You are a customer", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                                                        startActivity(intent);
                                                        intent.putExtra(EXTRA_KEY, loginPhNo);
                                                        getActivity().finish();
                                                    } else if (role.equals("admin")) {
                                                        Toast.makeText(getActivity(), "You are an admin", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(getActivity(), AdminHomeActivity.class);
                                                        startActivity(intent);
                                                        intent.putExtra(EXTRA_KEY, loginPhNo);
                                                        getActivity().finish();
                                                    } else {
                                                        Toast.makeText(getActivity(), "Not Verified", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(getActivity(), "Invalid role", Toast.LENGTH_SHORT).show();
                                                }
                                                connection.close();
                                            } catch (Exception e) {
                                                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
//                                            String fname = null;
//                                            String pincode = null;
//                                            String ll_no = null;
//                                            String bb_no = null;
//                                            String ft_no = null;
//                                            String dob = null;
//                                            String lname = null;
//                                            String gender = null;
//                                            String addr = null;
//                                            String district = null;
//                                            String mail = null;
//                                            if (bundle != null) {
//                                                fname = bundle.getString("fname");
//                                                lname = bundle.getString("lname");
//                                                dob = bundle.getString("dob");
//                                                gender = bundle.getString("gender");
//                                                district = bundle.getString("district");
//                                                addr = bundle.getString("addr");
//                                                pincode = bundle.getString("pincode");
//                                                mail = bundle.getString("mail");
//                                                ll_no = bundle.getString("ll_no");
//                                                bb_no = bundle.getString("bb_no");
//                                                ft_no = bundle.getString("ft_no");
//                                            }
//
//                                            Toast.makeText(getActivity(), ""+fname, Toast.LENGTH_SHORT).show();
//                                            Toast.makeText(getActivity(), ""+dob, Toast.LENGTH_SHORT).show();
//                                            Toast.makeText(getActivity(), ""+mail, Toast.LENGTH_SHORT).show();
//
//                                            Toast.makeText(getActivity(), "Verified", Toast.LENGTH_SHORT).show();
//                                            Intent intent = new Intent(getActivity(), HomeActivity.class);
//                                            startActivity(intent);
//                                            intent.putExtra(EXTRA_KEY, loginPhNo);
//                                            getActivity().finish();
                                        }
//
//                                        Intent intent = new Intent(getActivity(), HomeActivity.class);
//                                        startActivity(intent);
//                                        intent.putExtra(EXTRA_KEY, loginPhNo);
//                                        getActivity().finish();
                                        try{
                                            Class.forName(DRIVER);
                                            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                                            String fname = null;
                                            String pincode = null;
                                            String ll_no = null;
                                            String bb_no = null;
                                            String ft_no = null;
                                            String dob = null;
                                            String lname = null;
                                            String gender = null;
                                            String addr = null;
                                            String district = null;
                                            String mail = null;
                                            if (bundle != null) {
                                                fname = bundle.getString("fname");
                                                lname = bundle.getString("lname");
                                                dob = bundle.getString("dob");
                                                gender = bundle.getString("gender");
                                                district = bundle.getString("district");
                                                addr = bundle.getString("addr");
                                                pincode = bundle.getString("pincode");
                                                mail = bundle.getString("mail");
                                                ll_no = bundle.getString("ll_no");
                                                bb_no = bundle.getString("bb_no");
                                                ft_no = bundle.getString("ft_no");
                                            }

                                            Toast.makeText(getActivity(), "" + fname, Toast.LENGTH_SHORT).show();
                                            Toast.makeText(getActivity(), "" + dob, Toast.LENGTH_SHORT).show();
                                            Toast.makeText(getActivity(), "" + mail, Toast.LENGTH_SHORT).show();


                                            long pincode_num = 0;
                                            if (pincode != null) {
                                                pincode_num = Long.parseLong(pincode);
                                            }

                                            long ll_num = 0;
                                            if (ll_no != null && !ll_no.isEmpty()) {
                                                ll_num = Long.parseLong(ll_no);
                                            }

                                            long bb_num = 0;
                                            if (bb_no != null && !bb_no.isEmpty()) {
                                                bb_num = Long.parseLong(bb_no);
                                            }

                                            long ft_num = 0;
                                            if (ft_no != null && !ft_no.isEmpty()) {
                                                ft_num = Long.parseLong(ft_no);
                                            }

                                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                            Date birthdate = dateFormat.parse(dob);
                                            java.sql.Date sqlBirthdate = new java.sql.Date(birthdate.getTime());


//                String insertQuery = "INSERT INTO TBL_CUSTOMERS (phone_no,Fname,Lname,gender,DOB,streetaddress,district,pincode,email,ll_no,bb_no,ft_no) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
//                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
//                preparedStatement.setLong(1, loginPhNo);
//                preparedStatement.setString(2, fname);
//                preparedStatement.setString(3, lname);
//                preparedStatement.setString(4, gender);
//                preparedStatement.setDate(5, sqlBirthdate);
//                preparedStatement.setString(6, addr);
//                preparedStatement.setString(7, district);
//                preparedStatement.setLong(8, pincode_num);
//                preparedStatement.setString(9, mail);
//                preparedStatement.setLong(10, ll_num);
//                preparedStatement.setLong(11, bb_num);
//                preparedStatement.setLong(12, ft_num);

                                            String insertProcedure = "{CALL insert_customer(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
                                            CallableStatement statement2 = conn.prepareCall(insertProcedure);
                                            statement2.setLong(1, loginPhNo);
                                            statement2.setString(2, fname);
                                            statement2.setString(3, lname);
                                            statement2.setString(4, gender);
                                            statement2.setDate(5, sqlBirthdate);
                                            statement2.setString(6, addr);
                                            statement2.setString(7, district);
                                            statement2.setLong(8, pincode_num);
                                            statement2.setString(9, mail);
                                            statement2.setLong(10, ll_num);
                                            statement2.setLong(11, bb_num);
                                            statement2.setLong(12, ft_num);

                                            int count = statement2.executeUpdate();

                                            if (count > 0) {
                                                Toast.makeText(getActivity(), "Record Inserted", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getActivity(), "Connection Failed", Toast.LENGTH_SHORT).show();
                                            }
                                            conn.close();

                                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                                            startActivity(intent);
                                            intent.putExtra(EXTRA_KEY, loginPhNo);
                                            getActivity().finish();
                                        }
                                     catch (Exception ex) {
                                        StyleableToast.makeText(getActivity(), "" + ex.toString(),R.style.errorStyleToast).show();
                                    }
                                } else {
                                    StyleableToast.makeText(getActivity(), "Incorrect", R.style.errorStyleToast).show();
                                }
                            }
                        });
            } else {
                StyleableToast.makeText(getActivity(), "Please Check Internet Connection", R.style.errorStyleToast).show();
            }
//                    Toast.makeText(activity_verify.this, "OTP Verify", Toast.LENGTH_SHORT).show();
        } else {
            StyleableToast.makeText(getActivity(), "Please Enter all number", R.style.errorStyleToast).show();
        }


    numberotpmove();

}

    private void numberotpmove() {

        binding.input1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    binding.input2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.input2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    binding.input3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.input3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    binding.input4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.input4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    binding.input5.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.input5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    binding.input6.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }
}