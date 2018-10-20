package com.trungnguyen.android.houston123.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by trungnd4 on 07/10/2018.
 */

public class ListBaseResponse<T extends BaseUserResponse> {
    @SerializedName("current_page")
    private int page;
    @SerializedName("data")
    private List<T> dataList;

    public int getPage() {
        return this.page;
    }

    public List<T> getDataList() {
        return this.dataList;
    }
}