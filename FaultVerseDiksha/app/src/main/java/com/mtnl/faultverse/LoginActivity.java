package com.mtnl.faultverse;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.github.muddz.styleabletoast.StyleableToast;

public class LoginActivity extends AppCompatActivity {
    CheckBox captchaBox;
    ImageView im;
    Captcha c;
    TextView text91;
    EditText editCaptchaNumber;
    EditText editLoginNumber;
    String loginPhnNo;
    String t91;
    private int count = 0;

    static long loginPhNo;
    static int VCount;

    public static final String EXTRA_KEY2 = "signupPhNo";
    private Bundle bundle;
    public static final String EXTRA_BUNDLE = "extrabundle";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        text91 = findViewById(R.id.text91);
        captchaBox = findViewById(R.id.captchaCheckBox);
        im = findViewById(R.id.imageView7);
        editCaptchaNumber = findViewById(R.id.editCaptchaNum);
        editLoginNumber = findViewById(R.id.editLoginNumber);

        //database code
        Intent intent = getIntent();
        VCount = intent.getIntExtra("VerificationCount",SignupActivity.VCount);
        if(VCount==1) {
            bundle = intent.getExtras();
            long signup = bundle.getLong(SignupActivity.EXTRA_KEY2, SignupActivity.signupPhNo);
            String sign = Long.toString(signup);
            editLoginNumber.setText(sign);
        }
        else{
            Toast.makeText(this, "Shit", Toast.LENGTH_SHORT).show();
        }
//        signupPhNo = intent.getLongExtra(SignupActivity.EXTRA_KEY2, SignupActivity.signupPhNo);
//        Toast.makeText(this, "Your Signup Number: " + signupPhNo, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "Your Login Number: " + loginPhNo, Toast.LENGTH_SHORT).show();


        captchaBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (captchaBox.isChecked()) {
                    im.setVisibility(View.VISIBLE);
                    editCaptchaNumber.setVisibility(View.VISIBLE);
                    c = new MathCaptcha(300, 100, MathCaptcha.MathOptions.PLUS_MINUS_MULTIPLY);
                    im.setImageBitmap(c.image);
                    im.setLayoutParams(new LinearLayout.LayoutParams(c.getWidth() * 2, c.getHeight() * 2));
                }
                else
                {
                    im.setVisibility(View.GONE);
                    editCaptchaNumber.setVisibility(View.GONE);
                }
            }
        });
    }

    public void sendOtpLogin(View view) {
        if (editLoginNumber.getText().toString().equals("") && editLoginNumber.getText().toString().length()!=10) {
            StyleableToast.makeText(this, "Don't Enter Null Value",R.style.errorStyleToast).show();
        }
        else {
            if (captchaBox.isChecked()) {
                if (editCaptchaNumber.getText().toString().equals(c.answer)) {
                    count = 1;
                }
                if (count == 1) {
                    t91 = text91.getText().toString();
                    String phnNo = editLoginNumber.getText().toString();
                    loginPhNo = Long.parseLong(phnNo);
                    loginPhnNo = t91+phnNo;
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            loginPhnNo,
                            120,
                            TimeUnit.SECONDS,
                            LoginActivity.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    StyleableToast.makeText(LoginActivity.this, e.getMessage(), R.style.errorStyleToast).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String backEndOtp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                    Bundle bundle1 = new Bundle();
                                    bundle1.putString("MtnlLoginNumber",loginPhnNo);
                                    bundle1.putString("MtnlOTPNumber",backEndOtp);
                                    bundle1.putInt("VerificationCount",VCount);
                                    bundle1.putLong("DataLogin",loginPhNo);
                                    if(VCount==1) {
                                        bundle1.putBundle(EXTRA_BUNDLE, bundle);
                                    }
                                    OTPFragment otpFragment = new OTPFragment();
                                    otpFragment.setArguments(bundle1);
                                    otpFragment.show(getSupportFragmentManager(), otpFragment.getTag());
                                }
                            }
                    );
//                Bundle bundle = new Bundle();
//                bundle.putString(MTNL_KEY,loginPhnNo);
//                OTPFragment otpFragment = new OTPFragment();
//                otpFragment.setArguments(bundle);
//                otpFragment.show(getSupportFragmentManager(), otpFragment.getTag());
                }
                else {
                    StyleableToast.makeText(this, "Do the Captcha",R.style.errorStyleToast).show();
                }
            }
            else{
                StyleableToast.makeText(this, "Do the Captcha",R.style.errorStyleToast).show();
            }
        }
    }

    public void sendSignUp(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
        finish();
    }
}