package com.height.multiTenant.dubbo;

import com.height.multiTenant.utils.ParkContext;
import com.height.multiTenant.utils.ThreadLocalUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultipleTenantConsumerFilter implements Filter{

    protected  static final Logger logger = LoggerFactory.getLogger(MultipleTenantConsumerFilter.class);
    public static final String CONSUMER_MT_PROVIDER_PARK = "CONSUMER_MT_PARK";
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String parkId = ThreadLocalUtils.getContextStr();
        logger.info("MT_ConsumerFilter ： {}",parkId);

        if(StringUtils.isEmpty(parkId)){
            String msg = "CONSUMER INVALID PARK_ID !!";
            logger.error(msg,new RuntimeException(msg));
        }else{
            RpcContext.getContext().setAttachment(ParkContext.PARK_CONTENT_KEY,parkId);
        }
        return invoker.invoke(invocation);
    }
}
