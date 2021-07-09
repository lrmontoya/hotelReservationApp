public class Driver {

    public static void main (String [] args)
    {

    try {



        //Customer customer1 = new Customer ("first", "second", "j@domain.com");

        //Customer customer3 = new Customer ("first", "second", "t@domain.com");

        //System.out.println(customer1);

        //System.out.println(customer3);

        //Customer customer2 = new Customer("first2", "second2", "email");

        MainMenu.launchMainMenu();


    } catch (IllegalArgumentException ex)
    {
        System.out.println(ex.getLocalizedMessage());
    }

    }
}
