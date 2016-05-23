package controller;

import com.hazelcast.core.Hazelcast;
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
        String errtype = null;
        if (id == null || id.equals("")) {
            throw new ServletException("Невірний id користувача");
        }
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String psswd = request.getParameter("password");
        if (name == null || name.equals(""))
            errtype = "name";
        else if (email == null || !email.matches("\\w+@\\w+\\.\\w+"))
            errtype = "email";
        else if (psswd == null || psswd.length() < 8)
            errtype = "psswd";

        MongoClient mongo = (MongoClient) request.getServletContext()
                .getAttribute("MONGO_CLIENT");
        MongoUserDAO userDAO = new MongoUserDAO(mongo);
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(psswd);
        if (errtype != null) {
            request.setAttribute("user", user);
            request.setAttribute("errtype", errtype);
        } else {
            request.setAttribute("success", "Користувача " +
                    ((userDAO.updateUser(user) > 0) ? "" : "не ") + "редаговано");

            // Update Hz cache
            List<User> hzUsers =
                    Hazelcast.getHazelcastInstanceByName("USERS").getList("USERS");
            for (User hzUser : hzUsers)
                if (hzUser.getId().equals(user.getId())) {
                    hzUsers.remove(hzUser);
                    hzUsers.add(user);
                }
        }
        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }

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
        user = userDAO.findUser(user);
        request.setAttribute("user", user);
        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }
}