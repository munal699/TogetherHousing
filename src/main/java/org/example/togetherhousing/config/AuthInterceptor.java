package org.example.togetherhousing.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        String requestURI = request.getRequestURI();

        String email = (session != null) ? (String) session.getAttribute("email") : null;
        String role = (session != null) ? (String) session.getAttribute("role") : null;

        // If user is not logged in
        if (email == null || role == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=unauthorized");
            return false;
        }

        String normalizedRole = role.toUpperCase().trim();

        // Check path permissions based on role
        if (requestURI.startsWith("/buyer-dashboard") || requestURI.startsWith("/buyer/")) {
            if (!normalizedRole.equals("BUYER") && !normalizedRole.equals("ADMIN")) {
                response.sendRedirect(request.getContextPath() + "/login?error=unauthorized");
                return false;
            }
        } else if (requestURI.startsWith("/seller-dashboard") || requestURI.startsWith("/seller/")) {
            if (!normalizedRole.equals("SELLER") && !normalizedRole.equals("ADMIN")) {
                response.sendRedirect(request.getContextPath() + "/login?error=unauthorized");
                return false;
            }
        } else if (requestURI.startsWith("/admin-dashboard") || requestURI.startsWith("/admin/")) {
            if (!normalizedRole.equals("ADMIN")) {
                response.sendRedirect(request.getContextPath() + "/login?error=unauthorized");
                return false;
            }
        }

        return true;
    }
}
