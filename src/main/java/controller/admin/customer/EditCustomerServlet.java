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
import java.util.List;

/**
 * Created by deoxys on 28.05.16.
 */
@WebServlet("/admin/editCustomer")
public class EditCustomerServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String email = request.getParameter("email");
        String errtype = null;
        if (email == null || email.equals("")) {
            throw new ServletException("Невірний email користувача");
        }
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String password = request.getParameter("password");
        if (name == null || name.equals(""))
            errtype = "name";
        else if (surname == null || surname.equals(""))
            errtype = "surname";
        else if (email == null || !email.matches("\\w+@\\w+\\.\\w+"))
            errtype = "email";
        else if (password == null || password.length() < 8)
            errtype = "password";
        Customer customer = new Customer(name, surname, email, password);

        if (errtype != null) {
            request.setAttribute("customer", customer);
            request.setAttribute("errtype", errtype);
        } else {
            DataSource source = (DataSource) request.getServletContext()
                    .getAttribute("POSTGRES_CONNECTION_POOL");
            try {
                PostgresCustomerDAO customerDAO = new PostgresCustomerDAO(source);

                // Check if user with such credentials already exists
                Customer newCustomer = customerDAO.selectCustomer(email);
                if (newCustomer != null) {
                    boolean exists = newCustomer.getName().equals(name) &&
                            newCustomer.getSurname().equals(surname) &&
                            newCustomer.getEmail().equals(email) &&
                            newCustomer.getPassword().equals(password);
                    if (exists) {
                        request.setAttribute("error", "Користувач з такими даними вже існує");
                        request.setAttribute("customer", customer);
                    } else {
                        if (customerDAO.updateCustomer(customer)) {
                            request.setAttribute("success", "Користувача редаговано");

                            // Update Hz cache
                            List<Customer> hzCustomers =
                                    Hazelcast.getHazelcastInstanceByName("HZ_CONFIG").getList("CUSTOMERS");

                            for (Customer hzCustomer : hzCustomers)
                                if (hzCustomer.getEmail().equals(customer.getEmail())) {
                                    hzCustomers.remove(hzCustomer);
                                    hzCustomers.add(customer);
                                }
                        } else {
                            request.setAttribute("error", "Користувача не редаговано");
                        }
                    }
                }
                customerDAO.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        request.getRequestDispatcher("/admin/customers").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String email = request.getParameter("email");
        if (email == null || email.equals("")) {
            throw new ServletException("Невірний email користувача");
        }
        DataSource source = (DataSource) request.getServletContext()
                .getAttribute("POSTGRES_CONNECTION_POOL");

        PostgresCustomerDAO customerDAO;
        Customer customer = null;
        try {
            customerDAO = new PostgresCustomerDAO(source);
            customer = customerDAO.selectCustomer(email);
            customerDAO.closeConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        request.setAttribute("customer", customer);
        request.getRequestDispatcher("/admin/customers").forward(request, response);
    }
}