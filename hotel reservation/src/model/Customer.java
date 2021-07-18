package model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Customer {

    private String firstName;
    private String lastName;
    private String email;
    private final String emailRegex = "^(.+)@(.+).(.+)$";
    private final Pattern pattern = Pattern.compile(emailRegex);


    public Customer(String firstName, String lastName, String email){

        this.firstName = firstName;
        this.lastName = lastName;


        if (!pattern.matcher(email).matches()){

            throw new IllegalArgumentException("Error, Invalid email format");
        }

        this.email = email;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
         }

    public void setLastName(String lastN){
        this.lastName = lastName;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getFirstName(){

        return firstName;
    }

    public String getLastName(){

        return lastName;
    }

    public String getEmail(){

        return email;
    }



    @Override
    public String toString(){

        return "First name: " + firstName + " Last Name: " + lastName + " Email: "  + email;
    }

}
