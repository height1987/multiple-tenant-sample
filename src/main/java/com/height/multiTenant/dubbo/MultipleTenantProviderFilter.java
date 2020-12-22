package com.height.multiTenant.dubbo;

import com.height.multiTenant.utils.TenantContext;
import com.height.multiTenant.utils.TenantThreadLocalUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MultipleTenantProviderFilter implements Filter {

    protected  static final Logger logger = LoggerFactory.getLogger(MultipleTenantProviderFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        //服务端的filter，在执行业务代码之前先执行本filter
        //从RpcContext中获取context
        String tenantId = RpcContext.getContext().getAttachment(TenantContext.TENANT_CONTENT_KEY);

        if(!StringUtils.isEmpty(tenantId)){
            //把context放入本地ThreadLocal
            //方便在本服务中调用后续服务的context传递
            TenantThreadLocalUtils.setContextStr(tenantId);
        }else{
            logger.error("msg",new RuntimeException("PROVIDER INVALID PARK_ID !!"));
        }
        try {
            return invoker.invoke(invocation);
        }finally {
            //本次请求在服务端执行结束后，释放当前线程的context
            TenantThreadLocalUtils.clearContext();
        }
    }
}
