package com.minor.unclutter.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class PersonalMessageFragment extends Fragment {
    RecyclerView recyclerView;
    List<MssageInfoModel> sms;
    String[] days={"Sun","Mon","Tue","Wed","Thur","Fri","Sat"};
    Callback callback;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback=(Callback) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_personal_message, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sms=callback.getMessages(Types.PersonalMessageFragment);
        recyclerView=view.findViewById(R.id.personal_recycler_view);
        RecyclerViewAdapter recyclerViewAdapter=new RecyclerViewAdapter(sms);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(recyclerViewAdapter);
    }

}































//        Workbook wb = new HSSFWorkbook();
//        Sheet sheet=wb.createSheet("Hello");
//        if(sms==null) {
//            sms = new ArrayList();
//            Uri uriSms = Uri.parse("content://sms/inbox");
//            Cursor cursor = getActivity().getContentResolver().query(uriSms, new String[]{"_id", "address", "date", "body"},null,null,null);
//            int i=0;
//            cursor.moveToFirst();
//            while  (cursor.moveToNext())
//            {
//                MssageInfoModel tempMessage=new MssageInfoModel();
//                tempMessage.setSender(cursor.getString(1));
//                Date date = new Date(cursor.getLong(2));
//                tempMessage.setDay(days[date.getDay()]);
//                tempMessage.setMessage(cursor.getString(3));
//                sms.add(tempMessage);
//                Row row=sheet.createRow(i++);
//                Cell cell = row.createCell(0);
//                cell.setCellValue(tempMessage.getMessage());
//            }
//
//            File root = new File(Environment.getExternalStorageDirectory(), "ashi");
//            if (!root.exists()) {
//                root.mkdirs();
//            }
//            File yourFile = new File(root, "myfile.xls");
//            FileOutputStream fileOut=null;
//            try {
//                yourFile.createNewFile(); // if file already exists will do nothing
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                fileOut = new FileOutputStream(yourFile, false);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            try {
//                wb.write(fileOut);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                fileOut.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            // Closing the workbook
//            try {
//                wb.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//  }