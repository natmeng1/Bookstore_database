import javax.xml.transform.Result;
import java.util.*;
import java.sql.*;
public class TheStore {

    
    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Book Store");
        while (true){
            System.out.println("Enter 1 to be a user");
            System.out.println("Enter 2 to be the owner");
            System.out.println("Enter 3 to quit");

            String input = scanner.nextLine();

            if (input.contains("1")) {
                //Users
                firstRun();
            }
            else if (input.equals("2")) {
                System.out.println("Owner mode!");
                Owner();
            }
            else if (input.equals("3")) {
                break;
            }
            else {
                System.out.println("Invalid command! Please enter a valid command. \n");
                continue;
            }
        }
    }

    static void firstRun() {
        boolean r = true;
        ArrayList<String> cart = new ArrayList<>();
        while (r) {
            Scanner scan = new Scanner(System.in);
            System.out.println("Enter 1 to track an order,  2 to search for a book, or 3 to register a user, or 4 to exit");
            int b = scan.nextInt();

            if (b == 2) {
                System.out.println("Enter the corresponding number of search you would like to excecute:\n1 Book name\n2 author name\n3 ISBN\n4 Genre\n5 Sale price");
                int a = scan.nextInt();

                bookSearch(a, cart);

            } else if (b == 1) {

                System.out.println("Enter order number to be tracked");
                int ordernum = scan.nextInt();

                try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/BookStore", "Koushik", "coolkushy")
                ) {
                    Statement statement = connection.createStatement();

                    ResultSet resultSet = statement.executeQuery("select * " +
                            "from tracking " +
                            " where order_number = '"+ordernum+"'");

                    if (resultSet.next()){
                        System.out.println("Order Number: " + resultSet.getString("order_number"));
                        System.out.println("Day: " + resultSet.getString("the_day"));
                        System.out.println("Month: " + resultSet.getString("the_month"));
                        System.out.println("Year: " + resultSet.getString("the_year"));
                        System.out.println("City: " + resultSet.getString("City"));
                        System.out.println("Country: " + resultSet.getString("Country"));
                    }
                    else{
                        System.out.println("Tracking number not found!");
                    }

                }catch(Exception sqle) {
                    System.out.println("Exception: " + sqle);
                }

            } else if (b == 3) {
                try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/BookStore", "Koushik", "coolkushy")
                ) {
                    Statement statement = connection.createStatement();
                    System.out.println("Enter a valid username");
                    scan.nextLine();
                    String usern = scan.nextLine();
                    System.out.println(usern);
                    ResultSet resultSet = statement.executeQuery("select * " +
                            "from users " +
                            "where username = '"+ usern+"'");

                    if (!resultSet.next()){
                        System.out.println("Enter a valid password");
                        String password = scan.nextLine();

                        System.out.println("Enter a valid phone number");
                        String phone_num = scan.nextLine();

                        System.out.println("Enter shipping information");
                        String street_name, postal, city, country, bstreet_name, bpostal, bcity, bcountry;

                        System.out.println("street name: ");
                        street_name = scan.nextLine();

                        System.out.println("postal code: ");
                        postal = scan.nextLine();

                        System.out.println("city: ");
                        city = scan.nextLine();

                        System.out.println("country: ");
                        country = scan.nextLine();

                        System.out.println("Enter billing information");
                        System.out.println("street name: ");
                        bstreet_name = scan.nextLine();

                        System.out.println("postal code: ");
                        bpostal = scan.nextLine();

                        System.out.println("city: ");
                        bcity = scan.nextLine();

                        System.out.println("country: ");
                        bcountry = scan.nextLine();

                        statement.executeUpdate(
                                "insert into users values('" + usern + "'" + "," + "'" + password + "'" + "," + "'" + phone_num + "'" + "," + "'" + street_name + "'" + "," +
                                        "'" + postal + "'" + "," + "'" + city + "'" + "," + "'" + country + "'" + "," + "'" + bstreet_name + "'" + "," +
                                        "'" + bpostal + "'" + "," + "'" + bcity + "'" + "," + "'" + bcountry + "')");
                        System.out.println("User created!");
                    }else{
                        System.out.println("Username already exisits");
                        continue;
                    }
                } catch (Exception sqle) {
                    System.out.println("Exception: " + sqle);
                }
            }

            else{
                break;
            }

        }
    }
    static ArrayList<String> bookSearch ( int a, ArrayList<String > cart){

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/BookStore", "Koushik", "coolkushy");
             Statement statement = connection.createStatement();
        ) {
            ResultSet resultSet;

            if (a == 1) {

                Scanner scan = new Scanner(System.in);
                System.out.println("Enter Book Name you would like to search for");
                String cname = scan.nextLine();

                resultSet = statement.executeQuery("select *"
                        + "from book"
                        + " where book_name = '" + cname + "'");

                if (resultSet.next()) {
                    System.out.println("Book Name: " + resultSet.getString("book_name"));
                    System.out.println("Book ID: " + resultSet.getString("book_id"));
                    System.out.println("Author ID: " + resultSet.getString("author_id"));
                    System.out.println("Publisher ID: " + resultSet.getString("publisher_id"));
                    System.out.println("ISBN: " + resultSet.getString("ISBN"));
                    System.out.println("Genre: " + resultSet.getString("genre"));
                    System.out.println("Sale price: " + resultSet.getString("sale_price"));
                    System.out.println("Number of pages: " + resultSet.getString("no_of_pages"));
                    System.out.println("Quantity available: " + resultSet.getString("quantity"));
                } else {
                    System.out.println("Book does not exit!");
                    return cart;
                }
                String genre ="";
                String author_id ="";
                String sale_price = "";
                String purchase_price="";
                //Create var
                while (true) {
                    System.out.println("Would you like to add a book to your cart? Enter 1 for yes, 2 for no");
                    int ab = scan.nextInt();
                    scan.nextLine();
                    if (ab == 1) {
                        System.out.println("Enter book id\n");
                        String ds = scan.nextLine();
                        System.out.println("Book_id " + ds + ".");
                        //add to cart
                        //cart.add(ds);

                        if (validator(ds, "book_id") == 1) {
                            cart.add(ds);
                            System.out.println("Added to cart!");
                            resultSet = statement.executeQuery("Select * " +
                                    " from book " +
                                    " where book_id = '"+ ds+"'");

                            if (resultSet.next()){
                                genre = resultSet.getString("genre");
                                sale_price = resultSet.getString("sale_price");
                                author_id = resultSet.getString("author_id");
                                purchase_price=resultSet.getString("purchase_price");
                            }
                            System.out.println(cart);
                        } else {
                            System.out.println("Book does not exist");
                            continue;
                        }
                        System.out.println("Would you like to checkout? Enter 1 for yes, 2 for no");
                        int answer = scan.nextInt();
                        scan.nextLine();
                        if (answer == 1) {
                            String usern, street_name, postal, city, country, bstreet_name, bpostal, bcity, bcountry;
                            System.out.println("Enter username: ");
                            usern = scan.nextLine();
                            System.out.println("Enter Password: ");
                            String password = scan.nextLine();
                            System.out.println("username: " + usern);
                            //search query to check if username exists
                            //if it exists

                            resultSet = statement.executeQuery("select * " +
                                    "from users " +
                                    "where username = '" + usern + "' and the_password = '" + password + "'");

                            if (resultSet.next()) {

                                System.out.println("Would you like to use the same shipping and billing information?");
                                System.out.println("Enter 1 to use a different shipping and billing information");
                                System.out.println("Enter 2 or anything else to use the same shipping and billing information");
                                String input = scan.nextLine();
                                if (input.equals("1")){
                                    System.out.println("Enter shipping information");
                                    System.out.println("street name: ");
                                    street_name = scan.nextLine();

                                    System.out.println("postal code: ");
                                    postal = scan.nextLine();

                                    System.out.println("city: ");
                                    city = scan.nextLine();

                                    System.out.println("country: ");
                                    country = scan.nextLine();

                                    System.out.println("street name: " + street_name + ", postal code: " + postal + ", city: " + city + ", country: " + country);

                                    System.out.println("Enter billing information");
                                    System.out.println("street name: ");
                                    bstreet_name = scan.nextLine();

                                    System.out.println("postal code: ");
                                    bpostal = scan.nextLine();

                                    System.out.println("city: ");
                                    bcity = scan.nextLine();

                                    System.out.println("country: ");
                                    bcountry = scan.nextLine();

                                    System.out.println("street name: " + bstreet_name + ", postal code: " + bpostal + ", city: " + bcity + ", country: " + bcountry);
                                }
                                Random rand = new Random();

                                while(true) {
                                    int rand_num = rand.nextInt(20000);
                                    int rand_day = rand.nextInt(7);
                                    int rand_month = rand.nextInt(12);

                                    resultSet = statement.executeQuery("select * " +
                                            " from Orders " +
                                            " where order_number = '" + rand_num + "'");

                                    if (!resultSet.next()) {
                                        statement.executeUpdate("insert into orders values('" + rand_num + "', '" + usern + "')");
                                        statement.executeUpdate("insert into tracking values('" + rand_num + "', '" + rand_day + "',' " + rand_month + "', '"+2021+
                                                "' , 'Toronto' ,'Canada')");
                                        for (String i:cart){

                                            statement.executeUpdate("insert into sales values('" + i + "', '" + author_id + "',' " + genre + "', '"+
                                                    sale_price+"', '" + purchase_price + "')");

                                            statement.executeUpdate("Update book" +
                                                    " set quantity = quantity - 1" +
                                                    " where book_id = '" + i +"'");
                                        }
                                        System.out.println("Your order has been placed! The order number is " + rand_num);
                                        break;
                                    }
                                }


                            } else {
                                System.out.println("Login failed! Returning to user menu");
                                break;
                            }
                        }


                    } else {
                        break;
                    }
                }

            } else if (a == 2) {
                Scanner scan = new Scanner(System.in);
                System.out.println("Enter the fisrt name of the Author you would like to search for");
                String cname = scan.nextLine();
                resultSet = statement.executeQuery("select *"
                        + "from book natural join author"
                        + " where fname = '" + cname + "'");
                if (!resultSet.next()) {
                    System.out.println("Author does not exist");
                    return cart;
                }

                do {
                    System.out.println("Book Name: " + resultSet.getString("book_name"));
                    System.out.println("Book ID: " + resultSet.getString("book_id"));
                    System.out.println("Author First Name: " + resultSet.getString("fname"));
                    System.out.println("Publisher ID: " + resultSet.getString("publisher_id"));
                    System.out.println("ISBN: " + resultSet.getString("ISBN"));
                    System.out.println("Genre: " + resultSet.getString("genre"));
                    System.out.println("Sale price: " + resultSet.getString("sale_price"));
                    System.out.println("Number of pages: " + resultSet.getString("no_of_pages"));
                    System.out.println("Quantity available: " + resultSet.getString("quantity"));
                } while (resultSet.next());

                while (true) {
                    String genre ="";
                    String author_id ="";
                    String sale_price = "";
                    String purchase_price="";
                    System.out.println("Would you like to add a book to your cart? Enter 1 for yes, 2 for no");
                    int ab = scan.nextInt();
                    scan.nextLine();
                    if (ab == 1) {
                        System.out.println("Enter book id\n");
                        String ds = scan.nextLine();
                        System.out.println("Book_id " + ds + ".");
                        //add to cart
                        if (validator(ds, "book_id") == 1) {
                            cart.add(ds);
                            System.out.println("Added to cart!");
                            resultSet = statement.executeQuery("Select * " +
                                    " from book " +
                                    " where book_id = '"+ ds+"'");

                            if (resultSet.next()){
                                genre = resultSet.getString("genre");
                                sale_price = resultSet.getString("sale_price");
                                author_id = resultSet.getString("author_id");
                                purchase_price=resultSet.getString("purchase_price");
                            }
                            System.out.println(cart);
                        } else {
                            System.out.println("Book does not exist");
                            continue;
                        }

                        System.out.println("Would you like to checkout? Enter 1 for yes, 2 for no");
                        int answer = scan.nextInt();
                        scan.nextLine();
                        if (answer == 1) {
                            String usern, street_name, postal, city, country, bstreet_name, bpostal, bcity, bcountry;
                            System.out.println("Enter username");
                            usern = scan.nextLine();
                            System.out.println("username: " + usern);
                            System.out.println("Enter Password: ");
                            String password = scan.nextLine();
                            System.out.println("username: " + usern);
                            //search query to check if username exists
                            //if it exists

                            resultSet = statement.executeQuery("select * " +
                                    "from users " +
                                    "where username = '" + usern + "' and the_password = '" + password + "'");

                            if (resultSet.next()) {
                                System.out.println("Would you like to use the same shipping and billing information?");
                                System.out.println("Enter 1 to use a different shipping and billing information or anything else to use the same");
                                System.out.println("Enter 2 or anything else to use the same shipping and billing information");
                                String input = scan.nextLine();
                                if (input.equals("1")){
                                    System.out.println("Enter shipping information");
                                    System.out.println("street name: ");
                                    street_name = scan.nextLine();

                                    System.out.println("postal code: ");
                                    postal = scan.nextLine();

                                    System.out.println("city: ");
                                    city = scan.nextLine();

                                    System.out.println("country: ");
                                    country = scan.nextLine();

                                    System.out.println("street name: " + street_name + ", postal code: " + postal + ", city: " + city + ", country: " + country);

                                    System.out.println("Enter billing information");
                                    System.out.println("street name: ");
                                    bstreet_name = scan.nextLine();

                                    System.out.println("postal code: ");
                                    bpostal = scan.nextLine();

                                    System.out.println("city: ");
                                    bcity = scan.nextLine();

                                    System.out.println("country: ");
                                    bcountry = scan.nextLine();

                                    System.out.println("street name: " + bstreet_name + ", postal code: " + bpostal + ", city: " + bcity + ", country: " + bcountry);
                                }
                                Random rand = new Random();

                                while(true) {
                                    int rand_num = rand.nextInt(20000);
                                    int rand_day = rand.nextInt(7);
                                    int rand_month = rand.nextInt(12);

                                    resultSet = statement.executeQuery("select * " +
                                            " from Orders " +
                                            " where order_number = '" + rand_num + "'");

                                    if (!resultSet.next()) {


                                        statement.executeUpdate("insert into orders values('" + rand_num + "', '" + usern + "')");
                                        statement.executeUpdate("insert into tracking values('" + rand_num + "', '" + rand_day + "',' " + rand_month + "', '"+2021+
                                                "' , 'Toronto' ,'Canada')");

                                        //Loop through it here for the cart
                                        for (String i:cart){

                                            statement.executeUpdate("insert into sales values('" + i + "', '" + author_id + "',' " + genre + "', '"+
                                                    sale_price+"', '" + purchase_price + "')");

                                            statement.executeUpdate("Update book" +
                                                    " set quantity = quantity - 1" +
                                                    " where book_id = '" + i +"'");
                                        }
                                      //  statement.executeUpdate("insert into sales values('" + ds + "', '" + author_id + "',' " + genre + "', '"+
                                                //sale_price+"', '" + purchase_price + "')");
                                        System.out.println("Your order has been placed! The order number is " + rand_num);
                                        break;
                                    }
                                }

                            } else {
                                System.out.println("Login failed! Returning to user menu");
                                break;
                            }

                        }
                    } else {
                        break;
                    }
                }


            } else if (a == 3) {
                Scanner scan = new Scanner(System.in);
                System.out.println("Enter ISBN number you would like to search for");
                String cname = scan.nextLine();

                resultSet = statement.executeQuery("select *"
                        + "from book"
                        + " where ISBN = '" + cname + "'");

                if (resultSet.next()) {
                    System.out.println("Book Name: " + resultSet.getString("book_name"));
                    System.out.println("Book ID: " + resultSet.getString("book_id"));
                    System.out.println("Author ID: " + resultSet.getString("author_id"));
                    System.out.println("Publisher ID: " + resultSet.getString("publisher_id"));
                    System.out.println("ISBN: " + resultSet.getString("ISBN"));
                    System.out.println("Genre: " + resultSet.getString("genre"));
                    System.out.println("Sale price: " + resultSet.getString("sale_price"));
                    System.out.println("Number of pages: " + resultSet.getString("no_of_pages"));
                    System.out.println("Quantity available: " + resultSet.getString("quantity"));
                } else {
                    System.out.println("Book does not exit!");
                    return cart;
                }
                String genre ="";
                String author_id ="";
                String sale_price = "";
                String purchase_price="";
                while (true) {
                    System.out.println("Would you like to add a book to your cart? Enter 1 for yes, 2 for no");
                    int ab = scan.nextInt();
                    scan.nextLine();
                    if (ab == 1) {
                        System.out.println("Enter book id\n");
                        String ds = scan.nextLine();
                        System.out.println("Book_id " + ds + ".");
                        //add to cart
                        if (validator(ds, "book_id") == 1) {
                            cart.add(ds);
                            System.out.println("Added to cart!");
                            resultSet = statement.executeQuery("Select * " +
                                    " from book " +
                                    " where book_id = '"+ ds+"'");

                            if (resultSet.next()){
                                genre = resultSet.getString("genre");
                                sale_price = resultSet.getString("sale_price");
                                author_id = resultSet.getString("author_id");
                                purchase_price=resultSet.getString("purchase_price");
                            }
                            System.out.println(cart);
                        } else {
                            System.out.println("Book does not exist");
                            continue;
                        }
                        System.out.println("Would you like to checkout? Enter 1 for yes, 2 for no");
                        int answer = scan.nextInt();
                        scan.nextLine();
                        if (answer == 1) {
                            String usern, street_name, postal, city, country, bstreet_name, bpostal, bcity, bcountry;
                            System.out.println("Enter username");
                            usern = scan.nextLine();
                            System.out.println("username: " + usern);
                            //search query to check if username exists
                            //if it exists
                            System.out.println("Enter Password: ");
                            String password = scan.nextLine();
                            System.out.println("username: " + usern);
                            //search query to check if username exists
                            //if it exists

                            resultSet = statement.executeQuery("select * " +
                                    "from users " +
                                    "where username = '" + usern + "' and the_password = '" + password + "'");

                            if (resultSet.next()) {
                                System.out.println("Would you like to use the same shipping and billing information?");
                                System.out.println("Enter 1 to use a different shipping and billing information");
                                System.out.println("Enter 2 or anything else to use the same shipping and billing information");
                                String input = scan.nextLine();
                                if (input.equals("1")){
                                    System.out.println("Enter shipping information");
                                    System.out.println("street name: ");
                                    street_name = scan.nextLine();

                                    System.out.println("postal code: ");
                                    postal = scan.nextLine();

                                    System.out.println("city: ");
                                    city = scan.nextLine();

                                    System.out.println("country: ");
                                    country = scan.nextLine();

                                    System.out.println("street name: " + street_name + ", postal code: " + postal + ", city: " + city + ", country: " + country);

                                    System.out.println("Enter billing information");
                                    System.out.println("street name: ");
                                    bstreet_name = scan.nextLine();

                                    System.out.println("postal code: ");
                                    bpostal = scan.nextLine();

                                    System.out.println("city: ");
                                    bcity = scan.nextLine();

                                    System.out.println("country: ");
                                    bcountry = scan.nextLine();

                                    System.out.println("street name: " + bstreet_name + ", postal code: " + bpostal + ", city: " + bcity + ", country: " + bcountry);
                                }
                                Random rand = new Random();

                                while(true) {
                                    int rand_num = rand.nextInt(20000);
                                    int rand_day = rand.nextInt(7);
                                    int rand_month = rand.nextInt(12);

                                    resultSet = statement.executeQuery("select * " +
                                            " from Orders " +
                                            " where order_number = '" + rand_num + "'");

                                    if (!resultSet.next()) {
                                        statement.executeUpdate("insert into orders values('" + rand_num + "', '" + usern + "')");
                                        statement.executeUpdate("insert into tracking values('" + rand_num + "', '" + rand_day + "',' " + rand_month + "', '"+2021+
                                                "' , 'Toronto' ,'Canada')");
                                        statement.executeUpdate("insert into sales values('" + ds + "', '" + author_id + "',' " + genre + "', '"+
                                                sale_price+"', '" + purchase_price + "')");
                                        for (String i:cart){

                                            statement.executeUpdate("insert into sales values('" + i + "', '" + author_id + "',' " + genre + "', '"+
                                                    sale_price+"', '" + purchase_price + "')");

                                            statement.executeUpdate("Update book" +
                                                    " set quantity = quantity - 1" +
                                                    " where book_id = '" + i +"'");
                                        }
                                        System.out.println("Your order has been placed! The order number is " + rand_num);
                                        break;
                                    }
                                }

                            } else {
                                System.out.println("Login failed! Returning to user menu");
                                break;
                            }
                        }
                    } else {
                        break;
                    }
                }

            } else if (a == 4) {
                Scanner scan = new Scanner(System.in);
                System.out.println("Enter Genre you would like to search for");
                String cname = scan.nextLine();
                resultSet = statement.executeQuery("select *"
                        + "from book"
                        + " where genre = '" + cname + "'");

                if (resultSet.next()) {
                    System.out.println("Book Name: " + resultSet.getString("book_name"));
                    System.out.println("Book ID: " + resultSet.getString("book_id"));
                    System.out.println("Author ID: " + resultSet.getString("author_id"));
                    System.out.println("Publisher ID: " + resultSet.getString("publisher_id"));
                    System.out.println("ISBN: " + resultSet.getString("ISBN"));
                    System.out.println("Genre: " + resultSet.getString("genre"));
                    System.out.println("Sale price: " + resultSet.getString("sale_price"));
                    System.out.println("Number of pages: " + resultSet.getString("no_of_pages"));
                    System.out.println("Quantity available: " + resultSet.getString("quantity"));
                } else {
                    System.out.println("Book does not exit!");
                    return cart;
                }
                String genre ="";
                String author_id ="";
                String sale_price = "";
                String purchase_price="";
                while (true) {
                    System.out.println("Would you like to add a book to your cart? Enter 1 for yes, 2 for no");
                    int ab = scan.nextInt();
                    scan.nextLine();
                    if (ab == 1) {
                        System.out.println("Enter book id\n");
                        String ds = scan.nextLine();
                        System.out.println("Book_id " + ds + ".");
                        //add to cart
                        if (validator(ds, "book_id") == 1) {
                            cart.add(ds);
                            System.out.println("Added to cart!");
                            resultSet = statement.executeQuery("Select * " +
                                    " from book " +
                                    " where book_id = '"+ ds+"'");

                            if (resultSet.next()){
                                genre = resultSet.getString("genre");
                                sale_price = resultSet.getString("sale_price");
                                author_id = resultSet.getString("author_id");
                                purchase_price=resultSet.getString("purchase_price");
                            }
                            System.out.println(cart);
                        } else {
                            System.out.println("Book does not exist");
                            continue;
                        }
                        System.out.println("Would you like to checkout? Enter 1 for yes, 2 for no");
                        int answer = scan.nextInt();
                        scan.nextLine();
                        if (answer == 1) {
                            String usern, street_name, postal, city, country, bstreet_name, bpostal, bcity, bcountry;
                            System.out.println("Enter username");
                            usern = scan.nextLine();
                            System.out.println("username: " + usern);
                            //search query to check if username exists
                            //if it exists

                            System.out.println("Enter Password: ");
                            String password = scan.nextLine();
                            System.out.println("username: " + usern);
                            //search query to check if username exists
                            //if it exists

                            resultSet = statement.executeQuery("select * " +
                                    "from users " +
                                    "where username = '" + usern + "' and the_password = '" + password + "'");

                            if (resultSet.next()) {
                                System.out.println("Would you like to use the same shipping and billing information?");
                                System.out.println("Enter 1 to use a different shipping and billing information");
                                System.out.println("Enter 2 or anything else to use the same shipping and billing information");
                                String input = scan.nextLine();
                                if (input.equals("1")){
                                    System.out.println("Enter shipping information");
                                    System.out.println("street name: ");
                                    street_name = scan.nextLine();

                                    System.out.println("postal code: ");
                                    postal = scan.nextLine();

                                    System.out.println("city: ");
                                    city = scan.nextLine();

                                    System.out.println("country: ");
                                    country = scan.nextLine();

                                    System.out.println("street name: " + street_name + ", postal code: " + postal + ", city: " + city + ", country: " + country);

                                    System.out.println("Enter billing information");
                                    System.out.println("street name: ");
                                    bstreet_name = scan.nextLine();

                                    System.out.println("postal code: ");
                                    bpostal = scan.nextLine();

                                    System.out.println("city: ");
                                    bcity = scan.nextLine();

                                    System.out.println("country: ");
                                    bcountry = scan.nextLine();

                                    System.out.println("street name: " + bstreet_name + ", postal code: " + bpostal + ", city: " + bcity + ", country: " + bcountry);
                                }
                                Random rand = new Random();

                                while(true) {
                                    int rand_num = rand.nextInt(20000);
                                    int rand_day = rand.nextInt(31);
                                    int rand_month = rand.nextInt(12);

                                    resultSet = statement.executeQuery("select * " +
                                            " from Orders " +
                                            " where order_number = '" + rand_num + "'");

                                    if (!resultSet.next()) {
                                        statement.executeUpdate("insert into orders values('" + rand_num + "', '" + usern + "')");
                                        statement.executeUpdate("insert into tracking values('" + rand_num + "', '" + rand_day + "',' " + rand_month + "', '"+2021+
                                                "' , 'Toronto' ,'Canada')");
                                        statement.executeUpdate("insert into sales values('" + ds + "', '" + author_id + "',' " + genre + "', '"+
                                                sale_price+"', '"+purchase_price+"')");

                                        for (String i:cart){
                                            statement.executeUpdate("insert into sales values('" + i + "', '" + author_id + "',' " + genre + "', '"+
                                                    sale_price+"', '" + purchase_price + "')");

                                            statement.executeUpdate("Update book" +
                                                    " set quantity = quantity - 1" +
                                                    " where book_id = '" + i +"'");
                                        }

                                        System.out.println("Your order has been placed! The order number is " + rand_num);
                                        break;
                                    }
                                }
                            } else {
                                System.out.println("Login failed! Returning to user menu");
                                break;
                            }
                        }

                    } else {
                        break;
                    }
                }
            } else {
                System.out.println("Invalid search query");
            }
        } catch (Exception sqle) {
            System.out.println("Exception: " + sqle);
        }
        return cart;
    }
    public static void Owner () {
        Scanner scanner = new Scanner(System.in);

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/BookStore", "Koushik", "coolkushy");
             Statement statement = connection.createStatement();
        ) {
            System.out.println("Enter 1 to add books");
            System.out.println("Enter 2 to remove books");
            System.out.println("Enter 3 to view reports");
            System.out.println("Enter anything other than 1,2, or 3 to return to menu ");

            String input = scanner.nextLine();

            if (input.equals("1")) {

                System.out.println("Enter 1 to increase the quantity for an exisiting book?");
                System.out.println("Enter 2 to add a new book");

                input = scanner.nextLine();

                if (input.equals("1")) {
                    System.out.println("Please enter the book name");
                    input = scanner.nextLine();
                    ResultSet resultSet = statement.executeQuery("select *"
                            + " from book" +
                            " where book_name = '" + input + "'");

//                    if (resultSet.getFetchSize()){
//                        System.out.println("Book not found");
//                    }
                    String bookName = "";
                    if (resultSet.next()) {
                        System.out.print("Book Name: " + resultSet.getString("book_name") + " Quantity: " + resultSet.getString("quantity") + "\n");
                        bookName = resultSet.getString("book_name");

                        System.out.println("How many more books do you want to add");
                        input = scanner.nextLine();

                        statement.executeUpdate("UPDATE book" +
                                " SET quantity = quantity+" + input +
                                " WHERE book_name = '" + bookName + "'");

                        System.out.println("UPDATE HAS BEEN MADE");

                        resultSet = statement.executeQuery("select  * " +
                                " from book " +
                                " where book_name = '" + bookName + "'");
                        if (resultSet.next()) {
                            System.out.print("Book Name: " + resultSet.getString("book_name") + " Quantity: " + resultSet.getString("quantity") + "\n");
                        }
                    }
                } else if (input.equals("2")) {
                    System.out.println("Please enter a book name");
                    input = scanner.nextLine();

                    ResultSet resultSet = statement.executeQuery("select *"
                            + " from book" +
                            " where book_name = '" + input + "'");

                    String[] newString = new String[10];
                    if (!resultSet.next()) {
                        String bookName = input;
                        System.out.println("This book does not exist in the database!");

                        System.out.println("Please enter book id: ");
                        input = scanner.nextLine();

                        if (validator(input, "book_id") == -1) {
                            newString[0] = input;
                        } else {
                            System.out.println("This book is already exists.");
                            return;
                        }

                        System.out.println("Please enter author id: ");
                        input = scanner.nextLine();

                        newString[1] = input;


                        System.out.println("Please enter publisher id: ");
                        input = scanner.nextLine();

                        newString[2] = input;

                        newString[3] = bookName;

                        System.out.println("Please enter ISBN: ");
                        input = scanner.nextLine();

                        if (validator(input, "ISBN") == -1) {
                            newString[4] = input;
                        } else {
                            System.out.println("This book is already exists.");
                            return;
                        }

                        System.out.println("Please enter a genre: ");
                        input = scanner.nextLine();

                        newString[5] = input;

                        System.out.println("Please enter a purchase price: ");
                        input = scanner.nextLine();

                        newString[6] = input;

                        System.out.println("Please enter a sale price: ");
                        input = scanner.nextLine();

                        newString[7] = input;

                        System.out.println("Please enter the number of pages: ");
                        input = scanner.nextLine();

                        newString[8] = input;

                        System.out.println("Please enter the quantity: ");
                        int NewInput = scanner.nextInt();

                        if (NewInput > 10) {
                            input = "" + NewInput;
                            newString[9] = input;
                        } else {
                            return;
                        }

                        statement.executeUpdate(
                                "insert into book values('" + newString[0] + "'" + "," + "'" + newString[1] + "'" + "," + "'" + newString[2] + "'" + "," + "'" + newString[3] + "'" + "," +
                                        "'" + newString[4] + "'" + "," + "'" + newString[5] + "'" + "," + "'" + newString[6] + "'" + "," + "'" + newString[7] + "'" + "," +
                                        "'" + newString[8] + "'" + "," + "'" + newString[9] + "')");

                        System.out.println("Insertion complete!");
                    } else {
                        System.out.println("This book already exists in the database!");
                    }

                }
            } else if (input.equals("2")) {
                System.out.println("Please enter a book name");
                input = scanner.nextLine();

                ResultSet resultSet = statement.executeQuery("select *"
                        + " from book" +
                        " where book_name = '" + input + "'");

                if (resultSet.next()) {

                    statement.executeUpdate("Delete from book"
                            + " where book_name = '" + input + "'");

                    System.out.println("The book has been deleted!");
                } else {
                    System.out.println("This book does not exist in the database!");
                }
            } else if (input.equals("3")){
                System.out.println("Please enter 1 to view the sales vs expenditure report");
                System.out.println("Please enter 2 to view the sales per genre");
                System.out.println("Please enter 3 to view the sales per author");

                input = scanner.nextLine();

                if (input.equals("1")){
                    ResultSet resultSet = statement.executeQuery("select book_id,SUM(sale_price) as sale_price, SUM(purchase_price) as purchase_price " +
                            " from sales" +
                            " group by book_id");

                    double sales = 0.0;
                    double expense = 0.0;
                    while (resultSet.next()){
                        sales += resultSet.getFloat("sale_price");
                        expense += resultSet.getFloat("purchase_price");
                    }
                    expense = sales- expense;
                    System.out.println("The Sales vs Expenditure report");
                    System.out.println("Total sales: $" +sales);
                    System.out.println("Total expenses: $" + expense);
                }
                else if (input.equals("2")){
                    ResultSet resultSet = statement.executeQuery("select genre,SUM(sale_price) as sale_price" +
                            " from sales" +
                            " group by genre");

                    System.out.println("The Sales per genre");
                    while (resultSet.next()){
                        System.out.printf(resultSet.getString("genre") + "-> $" + resultSet.getFloat("sale_price")+ "\n");
                    }
                }

                else if (input.equals("3")){
                    ResultSet resultSet = statement.executeQuery(" select author_id, SUM(sale_price) as sale_price, fname, lname" +
                            " from author natural join sales" +
                            " group by author_id");

                    System.out.println("The Sales per author");
                    while (resultSet.next()){
                        System.out.printf(resultSet.getString("fname") + " " + resultSet.getString("lname") + " -> $" + resultSet.getFloat("sale_price")+ "\n");
                    }
                }
            }


        } catch (Exception sqle) {
            System.out.println("Exception: " + sqle);
        }
    }


    public static int validator (String item, String attribute){
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/BookStore", "Koushik", "coolkushy");
             Statement statement = connection.createStatement();
        ) {
            ResultSet resultSet = statement.executeQuery("select *"
                    + " from book"
                    + " where " + attribute + "= '" + item + "'");

            if (!resultSet.next()) {
                return -1;
            }

        } catch (Exception sqle) {
            System.out.println("Exception: " + sqle);
        }
        return 1;
    }

}
