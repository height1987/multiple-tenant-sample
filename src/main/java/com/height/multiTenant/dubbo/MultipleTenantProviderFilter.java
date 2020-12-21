package com.height.multiTenant.dubbo;

import com.height.multiTenant.utils.TenantContext;
import com.height.multiTenant.utils.ThreadLocalUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MultipleTenantProviderFilter implements Filter {

    protected  static final Logger logger = LoggerFactory.getLogger(MultipleTenantProviderFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String tenantId = RpcContext.getContext().getAttachment(TenantContext.TENANT_CONTENT_KEY);

        if(!StringUtils.isEmpty(tenantId)){
            ThreadLocalUtils.setContextStr(tenantId);
        }else{
            logger.error("msg",new RuntimeException("PROVIDER INVALID PARK_ID !!"));
        }
        return invoker.invoke(invocation);
    }
}
