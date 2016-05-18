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
@WebServlet("/editUser")
public class EditUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");
        String error = null;
        if (id == null || id.equals("")) {
            throw new ServletException("Невірний id користувача");
        }
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String psswd = request.getParameter("password");
        if (name == null || name.equals(""))
            error = "Ім'я введено невірно";
        else if (email == null || !email.matches("\\w+@\\w+\\.\\w+"))
            error = "Електронну пошту введено невірно";
        else if (psswd == null || psswd.length() < 8)
            error = "Занадто короткий пароль";

        MongoClient mongo = (MongoClient) request.getServletContext()
                .getAttribute("MONGO_CLIENT");
        MongoUserDAO userDAO = new MongoUserDAO(mongo);
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(psswd);
        if (error != null) {
            request.setAttribute("user", user);
            request.setAttribute("error", error);
        } else {
            System.out.println("Редагування користувача з id=" + user.getId());
            request.setAttribute("success", "Користувача " +
                    ((userDAO.updateUser(user) > 0) ? "" : "не ") + "редаговано");
        }
        List<User> users = userDAO.findAllUsers();
        request.getSession().setAttribute("users", users);
        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");
        if (id == null || id.equals("")) {
            throw new ServletException("Невірний id користувача");
        }
        System.out.println("Редагування даних користувача з id=" + id);
        MongoClient mongo = (MongoClient) request.getServletContext()
                .getAttribute("MONGO_CLIENT");
        MongoUserDAO userDAO = new MongoUserDAO(mongo);
        User user = new User();
        user.setId(id);
        user = userDAO.findUser(user);
        request.setAttribute("user", user);
        List<User> users = userDAO.findAllUsers();
        request.getSession().setAttribute("users", users);
        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }
}