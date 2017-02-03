package database.servlets.user;

import database.DBException;
import database.DBService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/doDeleteUser")
public class DoDeleteController extends HttpServlet {

    private static final String FORWARD_PAGE = "/userList";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        req.setCharacterEncoding("UTF-8");

        try {
            long id = Long.parseLong(req.getParameter("id"));
            DBService.getInstance().deleteUser(id);
            resp.sendRedirect(FORWARD_PAGE);
        } catch (NumberFormatException | DBException e) {
            e.printStackTrace();
        }
    }
}
