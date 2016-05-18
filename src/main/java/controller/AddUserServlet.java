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
@WebServlet("/addUser")
public class AddUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String psswd = request.getParameter("password");
        String errtype = null;
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(psswd);
        if (name == null || name.equals(""))
            errtype = "name";
        else if (email == null || !email.matches("\\S+@\\w+\\.\\w+"))
            errtype = "email";
        else if (psswd == null || psswd.length() < 8)
            errtype = "psswd";
        if (errtype != null) {
            request.setAttribute("errtype", errtype);
            request.setAttribute("addusr", user);
        }
        else {
            MongoClient mongo = (MongoClient) request.getServletContext()
                    .getAttribute("MONGO_CLIENT");
            MongoUserDAO userDAO = new MongoUserDAO(mongo);
            userDAO.createUser(user);
            request.setAttribute("success", "Нового користувача додано");
            List<User> users = userDAO.findAllUsers();
            request.getSession().setAttribute("users", users);
        }
        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        MongoClient mongo = (MongoClient) request.getServletContext()
                .getAttribute("MONGO_CLIENT");
        MongoUserDAO userDAO = new MongoUserDAO(mongo);
        List<User> users = userDAO.findAllUsers();
        request.getSession().setAttribute("users", users);
        request.setAttribute("user", null);
        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }
}