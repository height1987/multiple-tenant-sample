package com.height.multiTenant.dubbo;

import com.height.multiTenant.utils.ParkContext;
import com.height.multiTenant.utils.ThreadLocalUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MultipleTenantProviderFilter implements Filter {

    private String PROVIDER_MT_PROVIDER_PARK = "PROVIDER_MT_PARK";
    protected  static final Logger logger = LoggerFactory.getLogger(MultipleTenantProviderFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String parkId = RpcContext.getContext().getAttachment(ParkContext.PARK_CONTENT_KEY);

        if(!StringUtils.isEmpty(parkId)){
            ThreadLocalUtils.setContextStr(parkId);
        }else{
            logger.error("msg",new RuntimeException("PROVIDER INVALID PARK_ID !!"));
        }
        return invoker.invoke(invocation);
    }
}
