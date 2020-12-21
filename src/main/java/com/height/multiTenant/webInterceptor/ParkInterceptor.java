package com.height.multiTenant.webInterceptor;

import com.alibaba.fastjson.JSONObject;
import com.height.multiTenant.utils.CookieUtils;
import com.height.multiTenant.utils.ParkContext;
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
	private static final String PARK_NO = "PARK_NO";
	protected final Logger logger = LoggerFactory.getLogger(ParkInterceptor.class);


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Integer userId = (Integer) request.getAttribute(USER_ID);
		return handleParkPermission(request, userId);
	}

	private boolean handleParkPermission(HttpServletRequest request, Integer userId) {
		// 1.获取cookies中的 parkNo
		// 2.根据parkNo获取parkId
		// 3.check user和park的关系
		// 4.向context中添加parkId
		Cookie cookie = CookieUtils.getCookie(request, PARK_NO);
		if (cookie == null) {
			logger.info("park_no cookie is null");
			return false;
		}
		String parkNo = cookie.getValue();
		if (StringUtils.isEmpty(parkNo)) {
			logger.info("parkNo is empty");
			return false;
		}
		//TODO parkNo -> parkId
		ThreadLocalUtils.setContextStr(ParkContext.getInstance(getParkId(parkNo)).toString());
				RpcContext.getContext().getAttachment(ParkContext.PARK_CONTENT_KEY);
		return true;

	}

	private Integer getParkId(String parkNo) {
		// FIXME 这个要通过no换id。
		// 如果直接把id放入cookie，则可能会有安全性问题
		return 1;
	}

	private void buildInvalidResponse(HttpServletResponse response, String responseMessage) throws IOException {
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(JSONObject.toJSON(responseMessage).toString());
	}


}
