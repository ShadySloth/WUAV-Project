package GUI.Model;

import BE.Customer;
import BLL.CustomerManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

import java.sql.SQLException;

public class CustomerModel {
    private CustomerManager customerManager;
    private ObservableList<Customer> businessCustomer;
    private ObservableList<Customer> privateCustomer;
    private ObservableList<Customer> governmentCustomer;


    public CustomerModel() throws Exception {
        customerManager = new CustomerManager();
    }

    public void createCustomer(Customer customer) throws SQLException {
        customerManager.createCustomer(customer);
        if (customer.getCustomerType() == 1) {
            businessCustomer.add(customer);
        } else if (customer.getCustomerType() == 2) {
            governmentCustomer.add(customer);
        } else {
            privateCustomer.add(customer);
        }
    }

    public ObservableList<Customer> getBusinessCustomer() throws Exception {
        List<Customer> customers = new ArrayList<>();
        customers.addAll(customerManager.getCustomers().get(1));
        businessCustomer = FXCollections.observableArrayList();
        businessCustomer.addAll(customers);
        return businessCustomer;
    }

    public ObservableList<Customer> getGovernmentCustomer() throws Exception{
        List<Customer> customers = new ArrayList<>();
        customers.addAll(customerManager.getCustomers().get(2));
        governmentCustomer = FXCollections.observableArrayList();
        governmentCustomer.addAll(customers);
        return governmentCustomer;
    }

    public ObservableList<Customer> getPrivateCustomer() throws Exception{
        List<Customer> customers = new ArrayList<>();
        customers.addAll(customerManager.getCustomers().get(3));
        privateCustomer = FXCollections.observableArrayList();
        privateCustomer.addAll(customers);
        return privateCustomer;
    }

    public void deleteCustomer(Customer customer) throws SQLException {
        customerManager.deleteCustomer(customer);
        if (customer.getCustomerType() == 1) {
            businessCustomer.remove(customer);
        } else if (customer.getCustomerType() == 2) {
            governmentCustomer.remove(customer);
        } else {
            privateCustomer.remove(customer);
        }
    }

    public void updateCustomer(Customer c) throws SQLException {
        customerManager.updateCustomer(c);
    }

    public void customerSearch(String searchQuery) throws Exception {
        businessSearch(searchQuery);
        governmentSearch(searchQuery);
        privateSearch(searchQuery);

    }
    private void businessSearch(String searchQuery) throws Exception{
        List<Customer> searchResult = customerManager.searchCustomer(businessCustomer, searchQuery);
        businessCustomer.clear();
        businessCustomer.addAll(searchResult);
    }
    private void governmentSearch(String searchQuery) throws Exception{
        List<Customer> searchResult = customerManager.searchCustomer(governmentCustomer, searchQuery);
        governmentCustomer.clear();
        governmentCustomer.addAll(searchResult);
    }
    private void privateSearch(String searchQuery) throws Exception{
        List<Customer> searchResult = customerManager.searchCustomer(privateCustomer, searchQuery);
        privateCustomer.clear();
        privateCustomer.addAll(searchResult);
    }
}
