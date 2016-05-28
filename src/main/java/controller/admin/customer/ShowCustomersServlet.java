package controller.admin.customer;

import com.hazelcast.core.Hazelcast;
import dao.PostgresCustomerDAO;
import model.Customer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by deoxys on 28.05.16.
 */
@WebServlet("/admin/showCustomers")
public class ShowCustomersServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        DataSource source = (DataSource)
                request.getServletContext().getAttribute("POSTGRES_CONNECTION_POOL");

        List<Customer> customers, hzCustomers;

        try {
            PostgresCustomerDAO customerDAO = new PostgresCustomerDAO(source);

            // Too heavy, should be moved into Context Initialization ???
            customers = customerDAO.selectAllCustomers();

            hzCustomers = Hazelcast.getHazelcastInstanceByName("HZ_CONFIG")
                    .getList("CUSTOMERS");

            // Update Hz cache
            for (Customer customer : customers)
                if ( ! hzCustomers.contains(customer))
                    hzCustomers.add(customer);
            for (Customer hzCustomer : hzCustomers)
                if ( ! customers.contains(hzCustomer))
                    hzCustomers.remove(hzCustomer);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        request.getRequestDispatcher("/customers.jsp").forward(request, response);
    }
}
