package controller.admin.customer;

import com.hazelcast.core.Hazelcast;
import dao.PostgresCustomerDAO;
import model.customer.Customer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by deoxys on 28.05.16.
 */
@WebServlet("/admin/deleteCustomer")
public class DeleteCustomerServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        if (email == null || email.equals("")) {
            throw new ServletException("Невірний email користувача");
        }

        DataSource source = (DataSource) request.getServletContext()
                .getAttribute("POSTGRES_CONNECTION_POOL");

        Customer customer = null;
        try {
            PostgresCustomerDAO customerDAO = new PostgresCustomerDAO(source);
            customer = customerDAO.selectCustomer(email);
            if (customerDAO.deleteCustomer(email)) {
                request.setAttribute("success", "Користувача видалено");
            } else {
                request.setAttribute("error", "Користувача не видалено");
            }
            customerDAO.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Update Hz cache
        Hazelcast.getHazelcastInstanceByName("HZ_CONFIG")
                .getList("CUSTOMERS").remove(customer);

        request.getRequestDispatcher("/admin/customers").forward(request, response);
    }
}