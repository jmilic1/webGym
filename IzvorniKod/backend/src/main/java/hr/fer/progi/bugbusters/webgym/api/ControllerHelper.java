package hr.fer.progi.bugbusters.webgym.api;

import hr.fer.progi.bugbusters.webgym.model.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControllerHelper {
    static void addUserCookies(HttpServletResponse response, User myUser) {
        Cookie cookie = new Cookie("username", myUser.getUsername());
        Cookie roleCookie = new Cookie("role", myUser.getRole().toString());
        response.addCookie(cookie);
        response.addCookie(roleCookie);

        response.addCookie(new Cookie("SameSite", "None"));
        response.addCookie(new Cookie("Secure", null));
    }

    static void logOutUser(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("username") || cookie.getName().equals("role")) {
                deleteCookie(response, cookie);
            }
        }
    }

    static void deleteCookie(HttpServletResponse response, Cookie cookie) {
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    static void deleteUser(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("username") || cookie.getName().equals("role")) {
                deleteCookie(response, cookie);
            }
        }
    }

    static String extractRoleFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("role")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    static String extractUsernameFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("username")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
