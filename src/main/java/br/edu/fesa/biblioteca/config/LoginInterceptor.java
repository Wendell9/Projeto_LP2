/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.fesa.biblioteca.config;

import br.edu.fesa.biblioteca.service.CookieService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 *
 * @author guind
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws UnsupportedEncodingException, IOException {
        System.out.println(CookieService.getCookie(request, "usuarioId"));
        System.out.println(CookieService.getCookie(request, "ADMIN"));
        if (CookieService.getCookie(request, "usuarioId") != null) {
            if ("true".equals(CookieService.getCookie(request, "ADMIN"))) {
                return true;
            } else if ("false".equals(CookieService.getCookie(request, "ADMIN"))) {
                String uri = request.getRequestURI();
                if (uri.contains("Usuario/lista")) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN); // Define status 403 sem redirecionamento
                    return false;
                }
                else
                {
                    return true;
                }
            }
        }     

        response.sendRedirect("biblioteca-fesa");
        return false;
    }
}
