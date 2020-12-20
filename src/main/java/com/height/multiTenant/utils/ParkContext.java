package com.height.multiTenant.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class ParkContext implements Serializable {
    protected  static final Logger logger = LoggerFactory.getLogger(ParkContext.class);
    public final static ParkContext EMPTY_CONTEXT = new ParkContext();
    public final static String PARK_CONTENT_KEY = "PARK_CONTENT_KEY";
    public final static String SPLITTER = "|";

    private Integer parkId;

    public static ParkContext getInstance(int parkId){
        ParkContext parkContext = new ParkContext();
        parkContext.setParkId(parkId);
        return parkContext;
    }

    @Override
    public String toString(){
        if(parkId == null || parkId == 0){
            return "";
        }
        return parkId.toString();
    }
    public static ParkContext parse(String str){
        if(StringUtils.isBlank(str)){
            return EMPTY_CONTEXT;
        }
        try {
            Integer parkId = Integer.parseInt(str);
            ParkContext context = new ParkContext();
            context.setParkId(parkId);
            return context;
        }catch (Exception e){
            logger.error("parse parkid error",e);
            return EMPTY_CONTEXT;
        }

    }

    public Integer getParkId() {
        return parkId;
    }

    public void setParkId(Integer parkId) {
        this.parkId = parkId;
    }

}
