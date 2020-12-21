package com.height.multiTenant.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class TenantContext implements Serializable {
    protected  static final Logger logger = LoggerFactory.getLogger(TenantContext.class);
    public final static TenantContext EMPTY_CONTEXT = new TenantContext();
    public final static String TENANT_CONTENT_KEY = "PARK_CONTENT_KEY";
    public final static String SPLITTER = "|";

    private Integer parkId;

    public static TenantContext getInstance(int parkId){
        TenantContext tenantContext = new TenantContext();
        tenantContext.setParkId(parkId);
        return tenantContext;
    }

    @Override
    public String toString(){
        if(parkId == null || parkId == 0){
            return "";
        }
        return parkId.toString();
    }
    public static TenantContext parse(String str){
        if(StringUtils.isBlank(str)){
            return EMPTY_CONTEXT;
        }
        try {
            Integer parkId = Integer.parseInt(str);
            TenantContext context = new TenantContext();
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
