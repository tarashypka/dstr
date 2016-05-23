package controller;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
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
 * Created by deoxys on 18.05.16.
 */
@WebServlet("/showUsers")
public class ShowUsersServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        MongoClient mongo = (MongoClient) request.getServletContext()
                .getAttribute("MONGO_CLIENT");
        MongoUserDAO userDAO = new MongoUserDAO(mongo);
        List<User> users = userDAO.findAllUsers();
        List<User> hzUsers =
                Hazelcast.getHazelcastInstanceByName("USERS").getList("USERS");

        // Update Hz cache
        for (User user : users)
            if ( ! hzUsers.contains(user))
                hzUsers.add(user);
        for (User hzUser : hzUsers)
            if ( ! users.contains(hzUser))
                hzUsers.remove(hzUser);

        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }
}
