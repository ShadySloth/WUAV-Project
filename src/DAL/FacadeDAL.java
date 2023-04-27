package DAL;

import BE.Customer;
import BE.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class FacadeDAL {
    private CustomerDAO customerDAO;
    private UsersDAO usersDAO;

    public FacadeDAL() throws Exception {
        customerDAO = new CustomerDAO();
        usersDAO = new UsersDAO();
    }

    public Customer createCustomer(Customer customer) throws SQLException {
        return customerDAO.createCustomer(customer);
    }

    public void deleteCustomer(Customer customer) throws SQLException {
        customerDAO.deleteCustomer(customer);
    }

    public Map<Integer, List<Customer>> getCustomers() throws Exception{
        return customerDAO.returnCustomersByType();
    }

    public User createUser(User user) throws SQLException {
        return usersDAO.createUser(user);
    }

    public void deleteUser(User user) throws SQLException {
        usersDAO.deleteUser(user);
    }

    public Map<Integer, List<User>> getUsers() throws SQLException {
        return usersDAO.returnUsersByType();
    }

}
