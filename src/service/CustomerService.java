package service;

import model.Customer;
import model.IRoom;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class CustomerService {

    private Map<String, Customer> mapOfCustomers = new HashMap<String, Customer>();
    private Collection<Customer> customerHashSet = new HashSet<>();
    Customer client;

    private static CustomerService customerServiceInstance = null;

    private CustomerService(){

    }

    public static CustomerService getInstance(){

        if (customerServiceInstance == null ) {
            customerServiceInstance = new CustomerService();
        }
        return customerServiceInstance;
    }

     public void addCustomer(String email, String firstName, String lastName)
    {

        try {
        if (mapOfCustomers.containsKey(email))
        {

            System.out.println("Error: This email is already included in the database");
            return;
        }

            client = new Customer (firstName, lastName, email);
            mapOfCustomers.put(email, client);
            customerHashSet.add(client);
        }catch(IllegalArgumentException ex){
            System.out.println(ex.getLocalizedMessage());
        }


    }

    public Customer getCustomer(String customerEmail){

             if (!mapOfCustomers.containsKey(customerEmail)) {

                //System.out.println("Error: there are no customers registered with the email provided\n");
                return null;
                //throw new IllegalArgumentException();
                // 7/9/2022 I should not thrown an unhandled exception, better return null and handle the issue on the receiving function

            }
           else {

                 return mapOfCustomers.get(customerEmail);

           }

    }

    public Collection<Customer> getAllCustomers(){

        return customerHashSet;

    }
}
