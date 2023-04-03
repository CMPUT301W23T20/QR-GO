package com.example.qr_go;

import com.example.qr_go.Actor.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * This class generate coupon
 */
public class Coupon {
    private ArrayList<String> dataList = new ArrayList<>();

    private final String gl = "Keep scanning for a discount code!!!";
    private final String cg = "Congratulations, you got a free coffee! Contact the team with the following coupon code: \n";


    /**
     * Coupon constructor
     */
    public Coupon() {
        this.dataList = dataList;
        String[] code = {"CEK6GCN", "CEK6GCN", "N17ATBP", "29K6TBN", "QAXWGCU", "QQYJMGN"};
        dataList.addAll(Arrays.asList(code));
    }

    /**
     * this return a string with a coupon
     * @return
     * String
     */
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

