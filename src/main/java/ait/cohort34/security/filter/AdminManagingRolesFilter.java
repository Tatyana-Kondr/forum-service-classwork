package ait.cohort34.security.filter;

import ait.cohort34.accounting.model.Role;
import ait.cohort34.security.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.io.IOException;


@Component
@Order(20)
public class AdminManagingRolesFilter implements Filter {


    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        if (checkEndpoint(request.getMethod(), request.getServletPath()) ) {
            User principal = (User) request.getUserPrincipal();
            if(!principal.getRoles().contains(Role.ADMIN.name())){
                response.sendError(403, "You do not have permission to access this resource");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean checkEndpoint(String method, String path) {
//        String[] parts = path.split("/");
//       return parts.length == 6 && "role".equalsIgnoreCase(parts[4]);
        return  path.matches("/account/user/\\w+/role/\\w+");
    }
}
