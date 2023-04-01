package com.example.qr_go;

import com.example.qr_go.Actor.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Coupon {
    private ArrayList<String> dataList = new ArrayList<>();
    private final String gl = "T_T Good luck next time!!";

    public Coupon() {
        this.dataList = dataList;
        String[] code = {"CEK6GCN", "CEK6GCN", "N17ATBP", "29K6TBN", "QAXWGCU", "QQYJMGN"};
        dataList.addAll(Arrays.asList(code));
    }

    public String lottery(){
        Random randI = new Random();
        int i = randI.nextInt(1000);
        if (i < 5){
            return dataList.get(i);
        }else{
            return gl;
        }
    }



}

