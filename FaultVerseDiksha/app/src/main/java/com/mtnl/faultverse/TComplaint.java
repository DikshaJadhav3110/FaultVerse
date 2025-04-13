package com.mtnl.faultverse;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
public class TComplaint extends AppCompatActivity {
    Spinner s;
    TextView e, editTextNumber;
    Button button;
    Dialog myDialog;
    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@0.tcp.in.ngrok.io:10257:XE";
    private static final String USERNAME = "system";
    private static final String PASSWORD = "sys123";
    static long loginPhNo;
    public static final String EXTRA_KEY = "MTNLdb";
    public static String serviceNo = null;
    public static final String EXTRA_KEY5 = "serviceNo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcomplaint);
        myDialog = new Dialog(this);
        editTextNumber = findViewById(R.id.editTextNumber);
        Intent intent = getIntent();
        loginPhNo = intent.getLongExtra(HomeFragment.EXTRA_KEY, HomeFragment.loginPhNo);
        serviceNo = intent.getStringExtra(HomeFragment.EXTRA_KEY5);

        Toast.makeText(this, "" + loginPhNo, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "" + serviceNo, Toast.LENGTH_SHORT).show();

        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        s = findViewById(R.id.spinner);
        e = findViewById(R.id.editOther);
        final List<String> tele_options = Arrays.asList("Dead", "Miscellaneous", "Instrument Default", "Other");
        editTextNumber.setText(serviceNo);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.selected_item, tele_options);
        adapter.setDropDownViewResource(R.layout.dropdown);
        s.setAdapter(adapter);
        button = findViewById(R.id.fcomp_btn);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notification", "notification", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }


        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("Other")) {
                    e.setVisibility(View.VISIBLE);
                }
                if (selectedItem.equals("Dead") || selectedItem.equals("Miscellaneous") || selectedItem.equals("Instrument Default")) {
                    e.setVisibility(View.GONE);
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                action();
                ShowPopup();
                Bitmap largeicon = BitmapFactory.decodeResource(getResources(), R.drawable.mainicon);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(TComplaint.this, "notification");
                builder.setSmallIcon(R.drawable.mainicon);
                builder.setContentTitle("FaultVerse");
                builder.setContentText("Your Complaint has been registered");
                builder.setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle("FaultVerse")
                        .bigText("Dear customer, We acknowledge the successful registration of your complaint, and we assure you that FaultVerse is committed to resolving it promptly with utmost professionalism."));
                builder.setLargeIcon(largeicon);
                builder.setAutoCancel(true);
                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(TComplaint.this);
                if (ActivityCompat.checkSelfPermission(TComplaint.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                managerCompat.notify(1, builder.build());
            }
        });
    }

    public void ShowPopup() {
        TextView txtclose, complaintId, landNo;
        myDialog.setContentView(R.layout.tpopup);
        txtclose = myDialog.findViewById(R.id.txtclose);
        complaintId = myDialog.findViewById(R.id.complaintIdt);
        landNo = myDialog.findViewById(R.id.serviceNot);
        txtclose.setText("X");
        landNo.setText(serviceNo);
        String number = null;
        try {
            Class.forName(DRIVER);
            Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement statement = con.prepareStatement("SELECT complaint_no FROM tbl_complaint WHERE phone_no = ? AND service_number = ? AND status = 'pending' ORDER BY complaint_no DESC ");
            statement.setLong(1, loginPhNo);
            statement.setString(2, serviceNo);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                number = resultSet.getString("complaint_no");
                //Toast.makeText(this, ""+number, Toast.LENGTH_SHORT).show();
            }
            complaintId.setText(number);
            con.close();
            resultSet.close();
            statement.close();
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                noti();
            }
        });
        myDialog.show();
    }


    public void noti() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void action() {
        try {
            long service_num = Long.parseLong(serviceNo);
            String description;
            String selectedItem2 = s.getSelectedItem().toString();
            String service = "Landline";
            description = e.getText().toString();

            Class.forName(DRIVER);
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            String insertQuery;
            if (description.isEmpty()) {
                insertQuery = "INSERT INTO tbl_complaint (complaint_no,type_of_service, service_number, issue, issue_description,phone_no) VALUES (seq_ComplaintId.NEXTVAL,?,?,?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setString(1, service);
                preparedStatement.setLong(2, service_num);
                preparedStatement.setString(3, selectedItem2);
                preparedStatement.setString(4, null);
                preparedStatement.setLong(5, loginPhNo);
                int count = preparedStatement.executeUpdate();

                if (count > 0) {
                    Toast.makeText(TComplaint.this, "Record Inserted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TComplaint.this, "Connection Failed", Toast.LENGTH_SHORT).show();
                }
            } else {
                insertQuery = "INSERT INTO tbl_complaint(complaint_no,type_of_service, service_number, issue, issue_description,phone_no) VALUES (seq_ComplaintId.NEXTVAL,?,?,?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setString(1, service);
                preparedStatement.setLong(2, service_num);
                preparedStatement.setString(3, selectedItem2);
                preparedStatement.setString(4, description);
                preparedStatement.setLong(5, loginPhNo);

                int count = preparedStatement.executeUpdate();

                if (count > 0) {
                    Toast.makeText(TComplaint.this, "Record Inserted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TComplaint.this, "Connection Failed", Toast.LENGTH_SHORT).show();
                }
            }

            connection.close();
        } catch (Exception e) {
            Toast.makeText(TComplaint.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
