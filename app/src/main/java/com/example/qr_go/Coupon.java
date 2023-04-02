package com.example.qr_go;

import com.example.qr_go.Actor.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Coupon {
    private ArrayList<String> dataList = new ArrayList<>();
    private final String gl = "Good luck next time!!";
    private final String cg = "Congratulation!! You get a free coffee!!! Contact the team with following coupon: ";

    public Coupon() {
        this.dataList = dataList;
        String[] code = {"CEK6GCN", "CEK6GCN", "N17ATBP", "29K6TBN", "QAXWGCU", "QQYJMGN"};
        dataList.addAll(Arrays.asList(code));
    }

    public String lottery(){
        Random randI = new Random();
        int i = randI.nextInt(10);
        if (i < 5){
            return cg + dataList.get(i);
        }else{
            return gl;
        }
    }



}

