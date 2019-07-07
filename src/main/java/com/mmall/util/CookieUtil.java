package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CookieUtil {
    private final static String COOKIE_DOMAIN = ".yzzhan.info";
    private final static String COOKIE_NAME = "mmall_login_token";


    public static String readLoginToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for(Cookie cookie: cookies){
               log.info("Read cookie name: {}, cookie value: {}", cookie.getName(), cookie.getValue());
                if(StringUtils.equals(cookie.getName(), COOKIE_NAME)){
                    log.info("Return cookieName: {}, cookieValue: {}", cookie.getName(), cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static void writeLoginToken(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setDomain(COOKIE_DOMAIN);
        cookie.setPath("/");
        cookie.setMaxAge(60* 60 * 24 * 365);
        cookie.setHttpOnly(true);

        log.info("write cookieNmae: {}, cookieValue: {}", cookie.getName(), cookie.getValue());
        response.addCookie(cookie);
    }

    public static void delLoginToken(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for(Cookie cookie: cookies){
                log.info("Read cookie name: {}, cookie value: {}", cookie.getName(), cookie.getValue());
                if(StringUtils.equals(cookie.getName(), COOKIE_NAME)){
                    cookie.setDomain(COOKIE_DOMAIN);
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    log.info("delete cookieNmae: {}, cookieValue: {}", cookie.getName(), cookie.getValue());
                    response.addCookie(cookie);
                    return;
                }
            }
        }

    }
}