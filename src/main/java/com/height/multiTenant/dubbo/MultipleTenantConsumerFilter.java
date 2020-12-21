package com.height.multiTenant.dubbo;

import com.height.multiTenant.utils.TenantContext;
import com.height.multiTenant.utils.ThreadLocalUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultipleTenantConsumerFilter implements Filter{

    protected  static final Logger logger = LoggerFactory.getLogger(MultipleTenantConsumerFilter.class);
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String tenantId = ThreadLocalUtils.getContextStr();
        logger.info("MT_ConsumerFilter ： {}",tenantId);

        if(StringUtils.isEmpty(tenantId)){
            String msg = "CONSUMER INVALID PARK_ID !!";
            logger.error(msg,new RuntimeException(msg));
        }else{
            RpcContext.getContext().setAttachment(TenantContext.TENANT_CONTENT_KEY,tenantId);
        }
        return invoker.invoke(invocation);
    }
}
