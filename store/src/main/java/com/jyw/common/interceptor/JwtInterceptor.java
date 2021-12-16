package com.jyw.common.interceptor;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.jyw.common.annotation.Token;
import com.jyw.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;


public class JwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //获取请求头中的token
        String token = request.getHeader("token");
        // 如果不是映射到方法直接通过
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod)handler;
        Method method=handlerMethod.getMethod();

        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(Token.class)) {
            Token userLoginToken = method.getAnnotation(Token.class);
            if (userLoginToken.required()) {
                // 执行认证
                if (token == null) {
                    returnJson(response,"{\"code\":\"401\",\"msg\":\"没有登录，请登录！\"}");
                }
                try{
                    TokenUtil.verify(token);
                }catch(Exception e){
                    returnJson(response,"{\"code\":\"401\",\"msg\":\"登录已过期，请重新登陆！\"}");
                }
            }
        }
        return true;
    }

    /*返回客户端数据*/
    private void returnJson(HttpServletResponse response, String json){
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(json);

        } catch (IOException e) {
        } finally {
            if (writer != null)
                writer.close();
        }
    }
}
