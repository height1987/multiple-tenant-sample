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

    private Integer tenantId;

    public static TenantContext getInstance(int tenantId){
        TenantContext tenantContext = new TenantContext();
        tenantContext.setTenantId(tenantId);
        return tenantContext;
    }

    @Override
    public String toString(){
        if(tenantId == null || tenantId == 0){
            return "";
        }
        return tenantId.toString();
    }
    public static TenantContext parse(String str){
        if(StringUtils.isBlank(str)){
            return EMPTY_CONTEXT;
        }
        try {
            Integer tenantId = Integer.parseInt(str);
            TenantContext context = new TenantContext();
            context.setTenantId(tenantId);
            return context;
        }catch (Exception e){
            logger.error("parse tenantIdd error",e);
            return EMPTY_CONTEXT;
        }

    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

}
