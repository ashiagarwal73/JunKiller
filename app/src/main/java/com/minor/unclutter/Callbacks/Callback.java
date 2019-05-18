package com.minor.unclutter.Callbacks;

import com.minor.unclutter.Model.MssageInfoModel;

import java.util.List;

public interface Callback {
        List<MssageInfoModel> getMessages(int type);
}
