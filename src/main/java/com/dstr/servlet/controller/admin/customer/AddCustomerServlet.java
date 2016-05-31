package com.dstr.servlet.controller.admin.customer;

import com.hazelcast.core.Hazelcast;
import com.dstr.dao.PostgresCustomerDAO;
import com.dstr.model.customer.Customer;

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

@WebServlet(name = "AddCustomer", urlPatterns = "/admin/customers/add")
public class AddCustomerServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String errtype = null;
        request.getAttribute("name");
        Customer customer = new Customer();
        customer.setName(name);
        customer.setSurname(surname);
        customer.setEmail(email);
        customer.setPassword(password);
        if (name == null || name.equals(""))
            errtype = "name";
        if (surname == null || surname.equals(""))
            errtype = "surname";
        else if (email == null || ! email.matches("\\S+@\\w+\\.\\w+"))
            errtype = "email";
        else if (password == null || password.length() < 8)
            errtype = "password";
        if (errtype != null) {
            request.setAttribute("errtype", errtype);
            request.setAttribute("addcustomer", customer);
        } else {
            DataSource source = (DataSource) request.getServletContext()
                    .getAttribute("POSTGRES_CONNECTION_POOL");

            try {
                PostgresCustomerDAO customerDAO = new PostgresCustomerDAO(source);
                if (customerDAO.insertCustomer(customer)) {
                    request.setAttribute("success", "Нового користувача додано");
                } else {
                    request.setAttribute("error", "Нового користувача не додано");
                }
                customerDAO.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Update Hz cache
            Hazelcast.getHazelcastInstanceByName("HZ_CONFIG")
                    .getList("CUSTOMERS").add(customer);
        }
        request.getRequestDispatcher("/admin/customers").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        request.setAttribute("customer", null);
        request.getRequestDispatcher("/admin/customers").forward(request, response);
    }
}
