package com.height.multiTenant.mybatis;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.height.multiTenant.utils.ParkContext;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.apache.dubbo.rpc.RpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MultipleTenantInterceptor {

    protected  static final Logger logger = LoggerFactory.getLogger(MultipleTenantInterceptor.class);

    private final static LongValue DEFAULT_PARK_ID = new LongValue(-1);

    /**
     * 多租户表前缀  我们定了一个规则，有些表是区分租户的，则在表名前方加入了MT_作为区分
     */
    private final static String MT_PREFIX = "MT_";

    /**
     * 新多租户插件配置,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存万一出现问题
     */
    public static MybatisPlusInterceptor getInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                String parkObject = RpcContext.getContext().getAttachment(ParkContext.PARK_CONTENT_KEY);
                if(parkObject == null){
                    logger.warn("PARK_CONTEXT IS INVALID : NULL !");
                    return DEFAULT_PARK_ID;
                }
                ParkContext parkContext = ParkContext.parse(parkObject);
                if(parkContext == null || parkContext.getParkId() == null || parkContext.getParkId() == 0){
                    logger.warn("PARK_ID IS INVALID : NULL OR 0 !");
                    return DEFAULT_PARK_ID;
                }
                logger.info("valid PARK_ID = "+ parkContext.getParkId());
                return new LongValue(parkContext.getParkId());
            }

            @Override
            public String getTenantIdColumn() {
                return "MT_PARK_ID";
            }
            // 这是 default 方法,默认返回 false 表示所有表都需要拼多租户条件
            @Override
            public boolean ignoreTable(String tableName) {
                return !tableName.startsWith(MT_PREFIX);
            }
        }));
        // 如果用了分页插件注意先 add TenantLineInnerInterceptor 再 add PaginationInnerInterceptor
        // 用了分页插件必须设置 MybatisConfiguration#useDeprecatedExecutor = false
//        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }

}
