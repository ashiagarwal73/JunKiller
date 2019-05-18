package com.minor.unclutter.Activity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.minor.unclutter.Adapter.PagerAdapter;
import com.minor.unclutter.Callbacks.Callback;
import com.minor.unclutter.Model.MssageInfoModel;
import com.minor.unclutter.R;
import com.minor.unclutter.Utility.Types;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements Callback {
    List<MssageInfoModel> sms;
    String[] days = {"Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat"};
    TabLayout tabLayout;
    ViewPager viewPager;
    private int[] imageResId = {
            R.drawable.personal_icon,
            R.drawable.bank_icon,
            R.drawable.otp_icon,
            R.drawable.purchase_icon,
            R.drawable.network_icon,
            R.drawable.ic_promotions_black_24dp,
            R.drawable.ic_spam_black_24dp
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.sliding_tabs);
        viewPager = findViewById(R.id.viewpager);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_SMS},
                    0);
        }
        else {
            setUpUI();
        }

    }

    @Override
    public List<MssageInfoModel> getMessages(int type) {
        if (type == Types.PersonalMessageFragment) {
            return sms.subList(0, 10);
        } else if (type == Types.BankUpdateFragment) {
            return sms.subList(10, 20);
        } else if (type == Types.OTPFragment) {
            return sms.subList(20, 30);
        } else if (type == Types.PromotionsFragment) {
            return sms.subList(40, 50);
        } else if (type == Types.SpamMessageFragment) {
            return sms.subList(50, 60);
        } else if (type == Types.NetworkProviderFragment) {
            return sms.subList(60, 70);
        } else if (type == Types.PurchaseUpdateFragment) {
            return sms.subList(70, 80);
        }
        return sms;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setUpUI();
                }
        }
    }
    void setUpUI () {
            if (sms == null) {
                sms = new ArrayList<>();
                Uri uriSms = Uri.parse("content://sms/inbox");
                Cursor cursor = getContentResolver().query(uriSms, new String[]{"_id", "address", "date", "body"}, null, null, null);
                Objects.requireNonNull(cursor).moveToFirst();
                while (cursor.moveToNext()) {
                    MssageInfoModel tempMessage = new MssageInfoModel();
                    tempMessage.setSender(cursor.getString(1));
                    Date date = new Date(cursor.getLong(2));
                    tempMessage.setDay(days[date.getDay()]);
                    tempMessage.setMessage(cursor.getString(3));
                    sms.add(tempMessage);
                }
                viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(),
                        MainActivity.this));
                tabLayout.setupWithViewPager(viewPager);
                for (int i = 0; i < imageResId.length; i++) {
                    Objects.requireNonNull(tabLayout.getTabAt(i)).setIcon(imageResId[i]);
                }
            }
        }
    }
