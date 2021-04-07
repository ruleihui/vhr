package com.example.entity.help;

import java.util.List;

public class RespPageBean {
    private int total;
    private List<?> data;

    public RespPageBean(int total, List<?> data) {
        this.total = total;
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
