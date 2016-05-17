package controller;

import com.mongodb.MongoClient;
import dao.MongoUserDAO;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by deoxys on 17.05.16.
 */
@WebServlet("/deleteUser")
public class DeleteUserServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");
        if (id == null || id.equals("")) {
            throw new ServletException("Невірний id користувача");
        }
        MongoClient mongo = (MongoClient) request.getServletContext()
                .getAttribute("MONGO_CLIENT");
        MongoUserDAO userDAO = new MongoUserDAO(mongo);
        User user = new User();
        user.setId(id);
        System.out.println("Видалення користувача з id=" + id);
        request.setAttribute("success", "Користувача " +
                ((userDAO.deleteUser(user) > 0) ? "" : "не ") + "видалено");

        List<User> users = userDAO.findAllUsers();
        request.setAttribute("users", users);
        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }
}