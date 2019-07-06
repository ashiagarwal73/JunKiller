package com.minor.unclutter.Activity;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.minor.unclutter.Adapter.PagerAdapter;
import com.minor.unclutter.Callbacks.Callback;
import com.minor.unclutter.Fragment.PersonalMessageFragment;
import com.minor.unclutter.Model.MssageInfoModel;
import com.minor.unclutter.R;
import com.minor.unclutter.Utility.Classifier;
import com.minor.unclutter.Utility.Types;

import org.jetbrains.annotations.Nullable;
import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements Callback {
    List<MssageInfoModel> sms;
    String[] days = {"Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat"};
    TabLayout tabLayout;
    List<MssageInfoModel> personal=new ArrayList<>();
    List<MssageInfoModel> bank=new ArrayList<>();
    List<MssageInfoModel> otpmsgs=new ArrayList<>();
    List<MssageInfoModel> promotional=new ArrayList<>();
    List<MssageInfoModel> spam=new ArrayList<>();
    List<MssageInfoModel> network=new ArrayList<>();
    List<MssageInfoModel> purchase=new ArrayList<>();

    ViewPager viewPager;
    private int[] imageResId = {
            R.drawable.otp_icon,
            R.drawable.bank_icon,
            R.drawable.purchase_icon,
            R.drawable.network_icon,
            R.drawable.ic_promotions_black_24dp,
            R.drawable.personal_icon,
            R.drawable.ic_spam_black_24dp
    };
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog=new ProgressDialog(this);

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
            progressDialog.setTitle("Loading...");
            progressDialog.show();
            setUpUI();
        }

    }

    void executeModel()
    {
        progressDialog.setTitle("Loading Model...");
        final Classifier classifier=new Classifier(this,"word_dict.json");
        classifier.setMaxLength(183);
        classifier.setCallback(new Classifier.DataCallback() {
            @Override
            public void onDataProcessed(@Nullable HashMap<String, Integer> result) {
                progressDialog.setTitle("Classifying...");
                for(MssageInfoModel msgs:sms) {
                    String message = msgs.getMessage();
                    message = message.toLowerCase().trim();
                    boolean isPreProcess =preprocess(message, msgs);
                    if (!isPreProcess){
                        if (!TextUtils.isEmpty(message)) {
                            classifier.setVocab(result);
                            int[] token = classifier.tokenize(message);
                            int[] seq = classifier.padSequence(token);
                            try {
                                Interpreter tflite = new Interpreter(loadModelFile(MainActivity.this));
                                float input[] = new float[seq.length];
                                for (int i = 0; i < seq.length; i++) {
                                    input[i] = (float) seq[i];
                                }
                                float output[][] = {{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f}};
                                if (seq.length == 183) {
                                    tflite.run(input, output);
                                    int maxindex = maxArray(output);
                                    if (maxindex == 0)
                                        personal.add(msgs);
                                    else if (maxindex == 1)
                                        purchase.add(msgs);
                                    else if (maxindex == 2)
                                        bank.add(msgs);
                                    else if (maxindex == 3)
                                        promotional.add(msgs);
                                    else if (maxindex == 4)
                                        network.add(msgs);
                                    else if (maxindex == 5)
                                       otpmsgs.add(msgs);
                                    else
                                        spam.add(msgs);
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                }
                progressDialog.dismiss();
                viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(),
                        MainActivity.this));
                tabLayout.setupWithViewPager(viewPager);
                for (int i = 0; i < imageResId.length; i++) {
                    Objects.requireNonNull(tabLayout.getTabAt(i)).setIcon(imageResId[i]);
                }
            }
        });
        classifier.loadData();
    }



    @Override
    public List<MssageInfoModel> getMessages(int type) {
        if (type == Types.PersonalMessageFragment) {
            return personal;
        } else if (type == Types.BankUpdateFragment) {
            return bank;
        } else if (type == Types.OTPFragment) {
            return otpmsgs;
        } else if (type == Types.NetworkProviderFragment) {
            return network;
        } else if (type == Types.PurchaseUpdateFragment) {
            return purchase;
        } else if (type == Types.PromotionsFragment) {
            return promotional;
        } else if (type == Types.SpamMessageFragment) {
            return spam;
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

                progressDialog.setTitle("Fetching SMS...");
                sms = new ArrayList<>();
                Uri uriSms = Uri.parse("content://sms/inbox");
                Cursor cursor = getContentResolver().query(uriSms, new String[]{"_id", "address", "date", "body"}, null, null, null);
                if(cursor!=null) {
                    cursor.moveToFirst();
                    while (cursor.moveToNext()) {
                        MssageInfoModel tempMessage = new MssageInfoModel();
                        tempMessage.setSender(cursor.getString(1));
                        Date date = new Date(cursor.getLong(2));
                        tempMessage.setDay(days[date.getDay()]);
                        tempMessage.setMessage(cursor.getString(3));
                        sms.add(tempMessage);
                    }
                }
                executeModel();

    }
    private MappedByteBuffer loadModelFile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd("model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
    int maxArray(float[][] arr){
        float max=arr[0][0];
        int index=0;
        for(int i=0;i<arr[0].length;i++)
        {
            if(max<arr[0][i])
            {
                max=arr[0][i];
                index=i;
            }
        }
        return index;
    }

    private boolean preprocess(String message, MssageInfoModel msgs) {
        boolean ispreProcess=true;
        if(message.contains("otp")||message.contains("verification code")){
            otpmsgs.add(msgs);
        }
        else if(message.contains("inr")||message.contains("acct")||message.contains("debit")||message.contains("credit")||message.contains("txn")||message.contains("card")){
            bank.add(msgs);
        }
        else if(message.contains("airtel")||message.contains("jio")||message.contains("vodafone")||message.contains("8957212220")||message.contains("399")||message.contains("recharge")||message.contains("voice")||message.contains("data")||message.contains("internet")||message.contains("missed")||message.contains("call")||message.contains("balance")||message.contains("plan")){
            network.add(msgs);
        }
        else if(message.contains("%off")||message.contains("hurry")||message.contains("cashback")||message.contains("flat")||message.contains("sale")||message.contains("deal")||message.contains("save")||message.contains("% off")||message.contains("din")||message.contains("ke")||message.contains("roaming")||message.contains("unlimited")||message.contains("local")){
            promotional.add(msgs);
        }
        else if(message.contains("swiggy")||message.contains("zomato")||message.contains("customer")||message.contains("booking")||message.contains("order")||message.contains("pnr")||message.contains("ticket")||message.contains("http")||message.contains("delivered")||message.contains("dear")){
            purchase.add(msgs);
        }
        else if(message.contains("sonal")||message.contains("whatsapp")||message.contains("ashi")||message.contains("indigo")||message.contains("avik")||message.contains("ashish")||message.contains("student")||message.contains("okay")||message.contains("bc")||message.contains("bike")||message.contains("please")||message.contains("test")||message.contains("upes")||message.contains("all")){
            personal.add(msgs);
        }
        else if(message.contains("free")||message.contains("now")||message.contains("txt")||message.contains("mobile")||message.contains("claim")||message.contains("stop")||message.contains("ur")||message.contains("reply")){
            personal.add(msgs);
        }
        else {
            ispreProcess=false;
        }
        return ispreProcess;
    }
}