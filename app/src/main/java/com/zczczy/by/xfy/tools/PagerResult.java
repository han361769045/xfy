package com.zczczy.by.xfy.tools;

import java.util.List;

/**
 * Created by Leo on 2015/4/29.
 */
public class PagerResult<T> {

    private int PageIndex;

    private int PageSize;

    private int RowCount;

    private int PageCount;

    private List<T> ListData;


    public List<T> getListData() {
        return ListData;
    }

    public void setListData(List<T> listData) {
        ListData = listData;
    }

    public int getPageCount() {
        return PageCount;
    }

    public void setPageCount(int pageCount) {
        PageCount = pageCount;
    }

    public int getPageIndex() {
        return PageIndex;
    }

    public void setPageIndex(int pageIndex) {
        PageIndex = pageIndex;
    }

    public int getPageSize() {
        return PageSize;
    }

    public void setPageSize(int pageSize) {
        PageSize = pageSize;
    }

    public int getRowCount() {
        return RowCount;
    }

    public void setRowCount(int rowCount) {
        RowCount = rowCount;
    }
}
