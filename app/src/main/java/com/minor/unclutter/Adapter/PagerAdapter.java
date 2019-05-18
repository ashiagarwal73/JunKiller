package com.minor.unclutter.Adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.minor.unclutter.Fragment.BankUpdateFragment;
import com.minor.unclutter.Fragment.NetworkProviderFragment;
import com.minor.unclutter.Fragment.OTPFragment;
import com.minor.unclutter.Fragment.PersonalMessageFragment;
import com.minor.unclutter.Fragment.PromotionsFragment;
import com.minor.unclutter.Fragment.PurchaseUpdateFragment;
import com.minor.unclutter.Fragment.SpamMessageFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    Context context;
    private final int PAGE_COUNT=7;
    public PagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context=context;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i)
        {
            case 0:return new PersonalMessageFragment();

            case 1:return new BankUpdateFragment();

            case 2:return new OTPFragment();

            case 3: return new PurchaseUpdateFragment();

            case 4:return new NetworkProviderFragment();

            case 5:return new PromotionsFragment();

            case 6:return new SpamMessageFragment();
        }
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:return  "Personal Message";

            case 1:return "Bank Update";

            case 2:return "OTP";

            case 3:return "Purchase Update";

            case 4:return "Network Provider";

            case 5:return "Promotions";

            case 6:return "Spam";
        }
        return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
