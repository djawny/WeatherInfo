package com.example.daniel.weatherinfo.model;

import java.util.List;

public class ResponseByIds {

    private int cnt;
    private List<ResponseByCity> list;

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public List<ResponseByCity> getList() {
        return list;
    }

    public void setList(List<ResponseByCity> list) {
        this.list = list;
    }
}
