package com.mtnl.faultverse;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.mtnl.faultverse.databinding.FragmentHomeBinding;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import io.github.muddz.styleabletoast.StyleableToast;

public class HomeFragment extends Fragment {
    boolean pendl,inprogl,completel,land,broad,ftth,pendb,inprogb,completeb,pendf,inprogf,completef;
    private FragmentHomeBinding binding;
    private int count=1;
    public static long loginPhNo;
    public static final String EXTRA_KEY = "MTNLdb";
    public static String serviceNo=null;
    public static final String EXTRA_KEY5 = "serviceNo";
    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@0.tcp.in.ngrok.io:10257:XE";
    private static final String USERNAME = "system";
    private static final String PASSWORD = "sys123";
    private Connection connection;

    public HomeFragment() {
        // Required empty public constructor
    }
    public void onCreate(Bundle savedInstanceState){
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

        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        try {

        binding = FragmentHomeBinding.inflate(inflater, container, false);

            binding.landBtn.setImageResource(R.drawable.landline_white);
            binding.landBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark_green)));
            binding.broadBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bg_color)));
            binding.ftBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bg_color)));

            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            Statement statement = connection.createStatement();

            //for track status
            land = true;
            broad = true;
            ftth = true;


            pendl = true;
            inprogl = true;
            completel = false;


            pendb = true;
            inprogb = true;
            completeb = false;


            pendf = true;
            inprogf = true;
            completef = false;


        //for the customer services
//        int land= 1;
//        int broad= 1;
//        int ftth = 1;


            ResultSet resultSet1 = statement.executeQuery("select fname,lname from tbl_customers where phone_no = " + loginPhNo + "");
            if (resultSet1.next()) {
                String fname = resultSet1.getString("fname");
                String lname = resultSet1.getString("lname");
                binding.cusName.setText(fname + " " + lname);
            }

            ResultSet resultSet2 = statement.executeQuery("select status from tbl_service where phone_no = " + loginPhNo + "");
            if (resultSet2.next()) {
                String status = resultSet2.getString("status");
                binding.status.setText(status);
            }

            ResultSet resultSet3 = statement.executeQuery("select ll_no,bb_no,ft_no from tbl_customers where phone_no = " + loginPhNo + "");
            if (resultSet3.next()) {
                long land = resultSet3.getLong("ll_no");
                long broad = resultSet3.getLong("bb_no");
                long ftth = resultSet3.getLong("ft_no");
                String ll_no = String.valueOf(land);
                String bb_no = String.valueOf(broad);
                String ft_no = String.valueOf(ftth);

                // conditions for the services
                if (land != 0 && broad != 0 && ftth != 0) {
                    binding.landBtn.setVisibility(View.VISIBLE);
                    binding.broadBtn.setVisibility(View.VISIBLE);
                    binding.ftBtn.setVisibility(View.VISIBLE);
                    binding.serviceName.setText("Landline");
                    binding.serNum.setText(ll_no);
                    count = 1;
                } else if (land != 0 && broad == 0 && ftth == 0) {
                    binding.landBtn.setVisibility(View.VISIBLE);
                    binding.broadBtn.setVisibility(View.GONE);
                    binding.ftBtn.setVisibility(View.GONE);
                    binding.line1.setVisibility(View.GONE);
                    binding.line2.setVisibility(View.GONE);
                    binding.serviceName.setText("Landline");
                    binding.serNum.setText(ll_no);
                    count = 1;
                } else if (land != 0 && broad != 0 && ftth == 0) {
                    binding.landBtn.setVisibility(View.VISIBLE);
                    binding.broadBtn.setVisibility(View.VISIBLE);
                    binding.ftBtn.setVisibility(View.GONE);
                    binding.line2.setVisibility(View.GONE);
                    binding.serviceName.setText("Landline");
                    binding.serNum.setText(ll_no);
                    count = 1;
                } else if (land != 0 && broad == 0 && ftth != 0) {
                    binding.landBtn.setVisibility(View.VISIBLE);
                    binding.broadBtn.setVisibility(View.GONE);
                    binding.ftBtn.setVisibility(View.VISIBLE);
                    binding.line1.setVisibility(View.GONE);
                    binding.serviceName.setText("Landline");
                    binding.serNum.setText(ll_no);
                    count = 1;
                } else if (land == 0 && broad == 0 && ftth != 0) {
                    binding.landBtn.setVisibility(View.GONE);
                    binding.broadBtn.setVisibility(View.GONE);
                    binding.ftBtn.setVisibility(View.VISIBLE);
                    binding.line1.setVisibility(View.GONE);
                    binding.line2.setVisibility(View.GONE);
                    binding.serviceName.setText("Fibre-to-the-home");
                    binding.serNum.setText(ft_no);
                    count = 3;
                } else if (land == 0 && broad != 0 && ftth != 0) {
                    binding.landBtn.setVisibility(View.GONE);
                    binding.broadBtn.setVisibility(View.VISIBLE);
                    binding.ftBtn.setVisibility(View.VISIBLE);
                    binding.line1.setVisibility(View.GONE);
                    binding.serviceName.setText("Broadband");
                    binding.serNum.setText(bb_no);
                    count = 2;
                } else if (land == 0 && broad != 0 && ftth == 0) {
                    binding.landBtn.setVisibility(View.GONE);
                    binding.broadBtn.setVisibility(View.VISIBLE);
                    binding.ftBtn.setVisibility(View.GONE);
                    binding.line1.setVisibility(View.GONE);
                    binding.line2.setVisibility(View.GONE);
                    binding.serviceName.setText("Broadband");
                    binding.serNum.setText(bb_no);
                    count = 2;
                }

                binding.landBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        count = 1;
                        binding.serviceName.setText("Landline");
                        binding.serNum.setText(ll_no);


                        binding.landBtn.setImageResource(R.drawable.landline_white);
                        binding.broadBtn.setImageResource(R.drawable.broadband);
                        binding.ftBtn.setImageResource(R.drawable.ftth);

                        binding.landBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark_green)));
                        binding.broadBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bg_color)));
                        binding.ftBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bg_color)));
                    }
                });

                binding.broadBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        count = 2;
                        binding.serviceName.setText("Broadband");
                        binding.serNum.setText(bb_no);


                        binding.landBtn.setImageResource(R.drawable.landline);
                        binding.broadBtn.setImageResource(R.drawable.broadband_white);
                        binding.ftBtn.setImageResource(R.drawable.ftth);

                        binding.broadBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark_green)));
                        binding.landBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bg_color)));
                        binding.ftBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bg_color)));
                    }
                });

                binding.ftBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        count = 3;
                        binding.serviceName.setText("Fibre-to-the-home");
                        binding.serNum.setText(ft_no);


                        binding.landBtn.setImageResource(R.drawable.landline);
                        binding.broadBtn.setImageResource(R.drawable.broadband);
                        binding.ftBtn.setImageResource(R.drawable.ftth_white);

                        binding.ftBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark_green)));
                        binding.broadBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bg_color)));
                        binding.landBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bg_color)));
                    }
                });

                binding.compBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent;
                        if (count == 1) {
                            serviceNo = ll_no;
                            intent = new Intent(getActivity(), TComplaint.class);
                            intent.putExtra(EXTRA_KEY, loginPhNo);
                            intent.putExtra(EXTRA_KEY5, serviceNo);
                            startActivity(intent);
                        } else if (count == 2) {
                            serviceNo = bb_no;
                            intent = new Intent(getActivity(), BComplaint.class);
                            intent.putExtra(EXTRA_KEY, loginPhNo);
                            intent.putExtra(EXTRA_KEY5, serviceNo);
                            startActivity(intent);
                        } else if (count == 3) {
                            serviceNo = ft_no;
                            intent = new Intent(getActivity(), FComplaint.class);
                            intent.putExtra(EXTRA_KEY, loginPhNo);
                            intent.putExtra(EXTRA_KEY5, serviceNo);
                            startActivity(intent);
                        }
                    }
                });
            }
            connection.close();
        }catch (Exception e){
            Toast.makeText(getActivity(), ""+e.toString(), Toast.LENGTH_SHORT).show();
        }
        return binding.getRoot();
    }
    public void ForLandLine(){
        if (pendl) {
            if (inprogl && completel) {
                binding.compPending.setAlpha(1);
                binding.progbar1.setAlpha(1);
                binding.inprog.setAlpha(1);
                binding.progbar2.setAlpha(1);
                binding.viewComplete.setAlpha(1);
                StyleableToast.makeText(getActivity(), "Your Complaint is Successfully Resolved", R.style.errorStyleToast).show();
                RateUsDialog rateUsDialog = new RateUsDialog(getActivity());
                rateUsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                rateUsDialog.setCancelable(false);
                rateUsDialog.show();
            } else if (inprogl && !completel) {
                binding.compPending.setAlpha(1);
                binding.progbar1.setAlpha(1);
                binding.inprog.setAlpha(1);
                binding.progbar2.setAlpha(0.5F);
                binding.viewComplete.setAlpha(0.5F);
            }
        }
        else if (!pendl) {
            binding.statusTrackingCardLandline.setVisibility(View.GONE);
            binding.statusCheckViewLand.setVisibility(View.VISIBLE);
            binding.listViewStatusLand.setVisibility(View.GONE);
        }
    }
    public void ForBroadband(){
        if (pendb) {
            if (inprogb && completeb) {
                binding.compPending.setAlpha(1);
                binding.progbar1.setAlpha(1);
                binding.inprog.setAlpha(1);
                binding.progbar2.setAlpha(1);
                binding.viewComplete.setAlpha(1);
                StyleableToast.makeText(getActivity(), "Your Complaint is Successfully Resolved", R.style.errorStyleToast).show();
                RateUsDialog rateUsDialog = new RateUsDialog(getActivity());
                rateUsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                rateUsDialog.setCancelable(false);
                rateUsDialog.show();
            } else if (inprogb && !completeb) {
                binding.compPending.setAlpha(1);
                binding.progbar1.setAlpha(1);
                binding.inprog.setAlpha(1);
                binding.progbar2.setAlpha(0.5F);
                binding.viewComplete.setAlpha(0.5F);
            }
        }
        else if (!pendb) {
            binding.statusTrackingCardBroadband.setVisibility(View.GONE);
            binding.statusCheckViewBroad.setVisibility(View.VISIBLE);
            binding.listViewStatus.setVisibility(View.GONE);
        }
    }

    public void ForFTTH(){
        if (pendf) {
            if (inprogf && completef) {
                binding.compPending.setAlpha(1);
                binding.progbar1.setAlpha(1);
                binding.inprog.setAlpha(1);
                binding.progbar2.setAlpha(1);
                binding.viewComplete.setAlpha(1);
                StyleableToast.makeText(getActivity(), "Your Complaint is Successfully Resolved", R.style.errorStyleToast).show();
                RateUsDialog rateUsDialog = new RateUsDialog(getActivity());
                rateUsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                rateUsDialog.setCancelable(false);
                rateUsDialog.show();
            } else if (inprogf && !completef) {
                binding.compPending.setAlpha(1);
                binding.progbar1.setAlpha(1);
                binding.inprog.setAlpha(1);
                binding.progbar2.setAlpha(0.5F);
                binding.viewComplete.setAlpha(0.5F);
            }
        }
        else if (!pendf) {
            binding.statusTrackingCardFTTH.setVisibility(View.GONE);
            binding.statusCheckViewFTTH.setVisibility(View.VISIBLE);
            binding.listViewStatusFTTH.setVisibility(View.GONE);
        }
    }
}
