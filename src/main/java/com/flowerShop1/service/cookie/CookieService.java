package com.flowerShop1.service.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieService {
    public static void setCookie(HttpServletResponse response, String name, String value, int maxAge){
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);


    }
    //create function getCookie value by name
    public static String getCookieValue(HttpServletRequest request, String name){
      Cookie[] cookies = request.getCookies();
      if (cookies != null) {
          for (Cookie cookie : cookies) {
              if (cookie.getName().equals(name)) {
                  return cookie.getValue();
              }
          }
      }
           return null;
    }


}
