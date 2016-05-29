package controller.admin.order;

import com.hazelcast.core.Hazelcast;
import com.mongodb.MongoClient;
import dao.MongoItemDAO;
import dao.MongoOrderDAO;
import model.item.Item;
import model.order.Order;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by deoxys on 29.05.16.
 */
@WebServlet("/admin/showOrders")
public class ShowOrdersServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        MongoClient mongo = (MongoClient) request.getServletContext()
                .getAttribute("MONGO_CLIENT");
        MongoOrderDAO orderDAO = new MongoOrderDAO(mongo);

        // Too heavy, should be moved into Context Initialization ???
        List<Order> orders = orderDAO.findAllOrders();

        List<Order> hzOrders = Hazelcast.getHazelcastInstanceByName("HZ_CONFIG")
                .getList("ORDERS");

        // Update Hz cache
        for (Order order : orders)
            if ( ! hzOrders.contains(order))
                hzOrders.add(order);
        hzOrders.retainAll(orders);

        request.getRequestDispatcher("/admin/orders").forward(request, response);
    }
}
