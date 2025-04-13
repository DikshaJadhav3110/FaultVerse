package com.mtnl.faultverse;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class SignupActivity extends AppCompatActivity {

    private ImageView cal;
    private int mDate, mMonth, mYear;
    private CheckBox land_chk,broad_chk,ftth_chk;
    private TextView editLand,editBroad,editFtth;
    private TextView editLoginFirstName;
    private TextView editLoginLastName;
    private TextView editDob;
    private TextView editGender;
    private TextView editAddr;
    private TextView editDistrict;
    private TextView editPincode;
    private TextView editLoginMail;
    private TextView editLoginPNumber;
    private Button btn_otp;
    static long signupPhNo;
    private Spinner spinner;
    public static final String EXTRA_KEY2="signupPhNo";

    static int VCount = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        land_chk = findViewById(R.id.landline_check);
        broad_chk = findViewById(R.id.broadband_check);
        ftth_chk = findViewById(R.id.ftth_check);
        editLand = findViewById(R.id.editLand);
        editBroad = findViewById(R.id.editBroad);
        editFtth = findViewById(R.id.editFtth);
        editLoginFirstName = findViewById(R.id.editLoginFirstName);
        editLoginLastName = findViewById(R.id.editLoginLastName);
        editDob = findViewById(R.id.editDob);
//        editGender = findViewById(R.id.editGender);
        editAddr = findViewById(R.id.editAddr);
        editDistrict = findViewById(R.id.editDistrict);
        editPincode = findViewById(R.id.editPincode);
        editLoginMail = findViewById(R.id.editLoginMail);
        editLoginPNumber = findViewById(R.id.editLoginPNumber);
        btn_otp = findViewById(R.id.phn_up);
        cal = findViewById(R.id.imgdate);
        spinner = findViewById(R.id.editGender);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                String item = adapterView.getItemAtPosition(position).toString();
//                Toast.makeText(SignupActivity.this, "Selected item:"+item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar Cal= Calendar.getInstance();
                mDate = Cal.get(Calendar.DATE);
                mMonth = Cal.get(Calendar.MONTH);
                mYear  = Cal.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(SignupActivity.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        int monthinc = month;
                        monthinc++;
                        editDob.setText(date+"-"+monthinc+"-"+year);
                    }
                }, mYear, mMonth, mDate);
                datePickerDialog.show();
            }
        });

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Male");
        arrayList.add("Female");
        arrayList.add("Other");
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,arrayList);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);

        land_chk.setChecked(true);
        land_chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(land_chk.isChecked()) {
                    if (!broad_chk.isChecked() && !ftth_chk.isChecked()) {
                        editLand.setVisibility(View.VISIBLE);
                        editBroad.setVisibility(View.GONE);
                        editFtth.setVisibility(View.GONE);
                    } else if (broad_chk.isChecked() && !ftth_chk.isChecked()) {
                        editLand.setVisibility(View.VISIBLE);
                        editBroad.setVisibility(View.VISIBLE);
                        editFtth.setVisibility(View.GONE);
                    } else if (!broad_chk.isChecked() && ftth_chk.isChecked()) {
                        editLand.setVisibility(View.VISIBLE);
                        editBroad.setVisibility(View.GONE);
                        editFtth.setVisibility(View.VISIBLE);
                    } else if (broad_chk.isChecked() && ftth_chk.isChecked()) {
                        editLand.setVisibility(View.VISIBLE);
                        editBroad.setVisibility(View.VISIBLE);
                        editFtth.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    if (!broad_chk.isChecked() && !ftth_chk.isChecked()) {
                        editLand.setVisibility(View.GONE);
                        editBroad.setVisibility(View.GONE);
                        editFtth.setVisibility(View.GONE);
                    } else if (broad_chk.isChecked() && !ftth_chk.isChecked()) {
                        editBroad.setVisibility(View.VISIBLE);
                        editLand.setVisibility(View.GONE);
                        editFtth.setVisibility(View.GONE);
                    } else if (!broad_chk.isChecked() && ftth_chk.isChecked()) {
                        editFtth.setVisibility(View.VISIBLE);
                        editLand.setVisibility(View.GONE);
                        editBroad.setVisibility(View.GONE);
                    }
                }
            }
        });
        broad_chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(broad_chk.isChecked()) {
                    if (!land_chk.isChecked() && !ftth_chk.isChecked()) {
                        editBroad.setVisibility(View.VISIBLE);
                        editLand.setVisibility(View.INVISIBLE);
                        editFtth.setVisibility(View.INVISIBLE);
                    } else if (land_chk.isChecked() && !ftth_chk.isChecked()) {
                        editBroad.setVisibility(View.VISIBLE);
                        editLand.setVisibility(View.VISIBLE);
                        editFtth.setVisibility(View.INVISIBLE);
                    } else if (!land_chk.isChecked() && ftth_chk.isChecked()) {
                        editBroad.setVisibility(View.VISIBLE);
                        editLand.setVisibility(View.INVISIBLE);
                        editFtth.setVisibility(View.VISIBLE);
                    } else if (land_chk.isChecked() && ftth_chk.isChecked()) {
                        editBroad.setVisibility(View.VISIBLE);
                        editLand.setVisibility(View.VISIBLE);
                        editFtth.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    if (!land_chk.isChecked() && !ftth_chk.isChecked()) {
                        editLand.setVisibility(View.INVISIBLE);
                        editBroad.setVisibility(View.INVISIBLE);
                        editFtth.setVisibility(View.INVISIBLE);
                    } else if (!land_chk.isChecked() && ftth_chk.isChecked()) {
                        editFtth.setVisibility(View.VISIBLE);
                        editLand.setVisibility(View.INVISIBLE);
                        editBroad.setVisibility(View.INVISIBLE);
                    } else if (land_chk.isChecked() && !ftth_chk.isChecked()) {
                        editLand.setVisibility(View.VISIBLE);
                        editBroad.setVisibility(View.INVISIBLE);
                        editFtth.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        ftth_chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ftth_chk.isChecked()) {
                    if (!land_chk.isChecked() && !broad_chk.isChecked() && ftth_chk.isChecked()) {
                        editFtth.setVisibility(View.VISIBLE);
                        editLand.setVisibility(View.INVISIBLE);
                        editBroad.setVisibility(View.INVISIBLE);
                    } else if (!land_chk.isChecked() && broad_chk.isChecked() && ftth_chk.isChecked()) {
                        editFtth.setVisibility(View.VISIBLE);
                        editLand.setVisibility(View.INVISIBLE);
                        editBroad.setVisibility(View.VISIBLE);
                    } else if (land_chk.isChecked() && !broad_chk.isChecked() && ftth_chk.isChecked()) {
                        editFtth.setVisibility(View.VISIBLE);
                        editLand.setVisibility(View.VISIBLE);
                        editBroad.setVisibility(View.INVISIBLE);
                    } else if (land_chk.isChecked() && broad_chk.isChecked() && ftth_chk.isChecked()) {
                        editFtth.setVisibility(View.VISIBLE);
                        editLand.setVisibility(View.VISIBLE);
                        editBroad.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    if (!land_chk.isChecked() && !broad_chk.isChecked() && !ftth_chk.isChecked()) {
                        editLand.setVisibility(View.INVISIBLE);
                        editBroad.setVisibility(View.INVISIBLE);
                        editFtth.setVisibility(View.INVISIBLE);
                    } else if (!land_chk.isChecked() && broad_chk.isChecked() && !ftth_chk.isChecked()) {
                        editBroad.setVisibility(View.VISIBLE);
                        editLand.setVisibility(View.INVISIBLE);
                        editFtth.setVisibility(View.INVISIBLE);
                    } else if (land_chk.isChecked() && !broad_chk.isChecked() && !ftth_chk.isChecked()) {
                        editLand.setVisibility(View.VISIBLE);
                        editBroad.setVisibility(View.INVISIBLE);
                        editFtth.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }
    public void sendOtpSignUp(View view) {
        signupPhNo=0;
        String fname = null;
        String lname = null;
        String dob = null;
        String gender = null;
        String district = null;
        String pincode = null;
        String addr = null;
        String mail = null;
        String phone_no = null;
        String ll_no = null;
        String bb_no = null;
        String ft_no = null;
        try {
            fname = editLoginFirstName.getText().toString();
            lname = editLoginLastName.getText().toString();
            dob = editDob.getText().toString();
            gender = spinner.getSelectedItem().toString();
            addr = editAddr.getText().toString();
            district = editDistrict.getText().toString();
            pincode = editPincode.getText().toString();
            mail = editLoginMail.getText().toString();
            phone_no = editLoginPNumber.getText().toString();
            ll_no = editLand.getText().toString();
            bb_no = editBroad.getText().toString();
            ft_no = editFtth.getText().toString();
            signupPhNo = Long.parseLong(phone_no);
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        VCount = 1;
        Intent intent = new Intent(this, LoginActivity.class);
//        intent.putExtra(EXTRA_KEY2, signupPhNo);
        Bundle signup = new Bundle();
        intent.putExtra("VerificationCount",VCount);
        signup.putString("fname", fname);
        signup.putString("lname", lname);
        signup.putString("dob", dob);
        signup.putString("gender", gender);
        signup.putString("addr", addr);
        signup.putString("district", district);
        signup.putString("pincode", pincode);
        signup.putString("mail", mail);
        signup.putString("ll_no", ll_no);
        signup.putString("bb_no", bb_no);
        signup.putString("ft_no", ft_no);
        signup.putLong(EXTRA_KEY2,signupPhNo);
//        intent.putExtras(signup);
        intent.putExtras(signup);
        startActivity(intent);
        finish();
//        OTPFragment otpFragment = new OTPFragment();
//        otpFragment.show(getSupportFragmentManager(),otpFragment.getTag());
//        Intent intent = new Intent(this,HomeActivity.class);
//        startActivity(intent);
//        finish();
    }
    public void sendLogin(View view){
        Intent intent = new Intent(this,LoginActivity.class);
        intent.putExtra("VerificationCount",VCount);
        startActivity(intent);
        finish();
    }
}