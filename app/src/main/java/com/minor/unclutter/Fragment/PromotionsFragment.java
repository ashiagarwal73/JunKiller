package com.minor.unclutter.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.minor.unclutter.Adapter.RecyclerViewAdapter;
import com.minor.unclutter.Callbacks.Callback;
import com.minor.unclutter.Model.MssageInfoModel;
import com.minor.unclutter.R;
import com.minor.unclutter.Utility.Types;

import java.util.List;

public class PromotionsFragment extends Fragment {
    private RecyclerView recyclerView;
    Callback callback;
    List<MssageInfoModel> sms;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback=(Callback) context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_promotions, container, false);
        recyclerView=view.findViewById(R.id.promotion_recycler_view);
        sms=callback.getMessages(Types.PromotionsFragment);
        RecyclerViewAdapter recyclerViewAdapter=new RecyclerViewAdapter(sms);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(recyclerViewAdapter);
        return view;
    }

}
