package servlets.filters;

import database.DBException;
import database.DBService;
import database.pojo.User;
import servlets.LoginController;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebFilter("/*")
public class CookieFilter implements Filter {
    private static final String COOKIE_CHECKED = "COOKIE_CHECKED";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession();

        User userFromSession = LoginController.getLoggedUser(session);
        // if logged in -> check cookie
        if (userFromSession != null) {
            session.setAttribute(COOKIE_CHECKED, "CHECKED");
            chain.doFilter(request, response);
            return;
        }

        // if not logged but cookie checked
        if (session.getAttribute(COOKIE_CHECKED) != null) {
            String login = LoginController.getLoginFromCookie(req);
            try {
                long id = DBService.getInstance().getIdByLogin(login);
                User user = DBService.getInstance().getUser(id);
                LoginController.storeUserCookie(resp, user);
            } catch (DBException e) {
                e.printStackTrace();
            }

            session.setAttribute(COOKIE_CHECKED, "CHECKED");
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
