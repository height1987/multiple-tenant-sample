package com.height.multiTenant.webInterceptor;

import com.alibaba.fastjson.JSONObject;
import com.height.multiTenant.utils.CookieUtils;
import com.height.multiTenant.utils.TenantContext;
import com.height.multiTenant.utils.ThreadLocalUtils;
import org.apache.dubbo.rpc.RpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author hariyoo
 * @Date 2020/12/9 13:56
 */
public class ParkInterceptor implements HandlerInterceptor {

	private static final String USER_ID = "USER_TOKEN";
	private static final String TENANT_NO = "TENANT_NO";
	protected final Logger logger = LoggerFactory.getLogger(ParkInterceptor.class);


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Integer userId = (Integer) request.getAttribute(USER_ID);
		return handleParkPermission(request, userId);
	}

	private boolean handleParkPermission(HttpServletRequest request, Integer userId) {
		// 1.获取cookies中的 tenantNo
		// 2.根据tenantNo获取tenantId
		// 3.check user和tenant的关系
		// 4.向context中添加tenantId
		Cookie cookie = CookieUtils.getCookie(request, TENANT_NO);
		if (cookie == null) {
			logger.info("tenantNo cookie is null");
			return false;
		}
		String tenantNo = cookie.getValue();
		if (StringUtils.isEmpty(tenantNo)) {
			logger.info("tenantNo is empty");
			return false;
		}
		ThreadLocalUtils.setContextStr(TenantContext.getInstance(getParkId(tenantNo)).toString());
				RpcContext.getContext().getAttachment(TenantContext.TENANT_CONTENT_KEY);
		return true;

	}

	private Integer getParkId(String tenantNo) {
		// FIXME 这个要通过no换id。
		// 如果直接把id放入cookie，则可能会有安全性问题
		// parkNo可以跟进业务的情况生产，可以用长度为6位的随机字符串
		return 1;
	}

	private void buildInvalidResponse(HttpServletResponse response, String responseMessage) throws IOException {
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(JSONObject.toJSON(responseMessage).toString());
	}


}
