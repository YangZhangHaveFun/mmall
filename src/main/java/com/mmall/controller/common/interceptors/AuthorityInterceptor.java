package com.mmall.controller.common.interceptors;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;

@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
      log.info("prehandled");
        HandlerMethod handlerMethod = (HandlerMethod) o;

        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBean().getClass().getSimpleName();

        StringBuffer requestParamBuffer = new StringBuffer();
        Map paramMap = httpServletRequest.getParameterMap();
        for (Object value : paramMap.entrySet()) {
            Map.Entry entry = (Map.Entry) value;
            String mapKey = (String) entry.getKey();
            String mapValue = StringUtils.EMPTY;

            Object obj = entry.getValue();
            if (obj instanceof String[]){
                String[] strs = (String[]) obj;
                mapValue = Arrays.toString(strs);
            }
            requestParamBuffer.append(mapKey).append("=").append(mapValue);
        }
        if(StringUtils.equals(className, "UserManageController") && StringUtils.equals(methodName, "login")) {
            log.info("Authorization interceptor catches request, className: {}, methodName: {}, params: {}", className, methodName, requestParamBuffer.toString());
            return true;
        }
        User user = null;

        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isNotEmpty(loginToken)){
            String userJsonStr = RedisShardedPoolUtil.get(loginToken);
            user = JsonUtil.string2Obj(userJsonStr, User.class);
        }

        if(user == null || (user.getRole().intValue() != Const.Role.ROLE_ADMIN)){
            httpServletResponse.reset(); // If not reset, there will be an exception "getWriter() has been called for this response"
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json;charset=UTF-8");

            PrintWriter out = httpServletResponse.getWriter();


            if (user == null){
                if(StringUtils.equals(className, "ProductManagerController") && StringUtils.equals(methodName, "richtextImgUpload")){
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success", false);
                    resultMap.put("msg", "Please login as administrator");
                    out.print(JsonUtil.object2String(resultMap));
                }else{
                    out.print(JsonUtil.object2String(ServerResponse.createByErrorMessage("Interceptor catches the request. User didn't login")));
                }
            } else {
                if(StringUtils.equals(className, "ProductManagerController") && StringUtils.equals(methodName, "richtextImgUpload")){
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success", false);
                    resultMap.put("msg", "No privilege to operate");
                    out.print(JsonUtil.object2String(resultMap));
                } else {
                    out.print(JsonUtil.object2String(ServerResponse.createByErrorMessage("Interceptor catches request, user can't operate due to lack of privilege.")));
                }
            }
            out.flush();
            out.close();

            return false;
        }
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
