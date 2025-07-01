import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ATM implements ATMInterface{

    private int index = -1;
    /* 
    private String ID[] = {"101","102","103","104","105"};
    private String name[] = {"Alice Smith","Bob Johnson", "Charlie Brown", "Diana Prince", "Ethan Hunt"};
    private String PIN[] = {"1234","2345","3456","4587","5678"};
    private double balance[] = {1000.00,1500.50,2000.75,2500.00,3000.25};
    */
    private Scanner scanner = new Scanner(System.in);

    private String ID[];
    private String name[];
    private String PIN[];
    private double balance[];

    public void parseTxtFile(String filename) {
        int lineCount = 0;

        // First, count the number of lines to initialize the arrays
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                lineCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Initialize arrays based on the number of lines
        //checks the number of lines first so it can accurately define array size
        ID = new String[lineCount];
        name = new String[lineCount];
        PIN = new String[lineCount];
        balance = new double[lineCount];

        // Read the file again and populate the arrays
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int index = 0;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    ID[index] = parts[0].trim();
                    name[index] = parts[1].trim();
                    PIN[index] = parts[2].trim();
                    balance[index] = Double.parseDouble(parts[3].trim());
                    index++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveDataToFile(String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (int i = 0; i < ID.length; i++) {
                bw.write(ID[i] + "," + name[i] + "," + PIN[i] + "," + balance[i]);
                bw.newLine(); //move down a line
            }System.out.println("Data saved successfully to " + filename);
            
            } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    public boolean newUser(){
        //this will be added as an option at the start that instead of logging in a user can create a new account
        //
        int tries = 3;
        System.out.println("Please Enter your full name:");
        String fullName = scanner.nextLine();
        while(tries > 0){
            System.out.println("Please Enter your desired PIN:");
            String newPin = scanner.nextLine();
            if(newPin.length() != 4){
                System.out.println("PIN must be 4 digits");
                tries--;
                System.out.println("Attempts left: " + tries);
            }
            else{
                int length = ID.length;
                ID[length+1] = String.valueOf(length + 1);
                name[length+1] = fullName;
                PIN[length+1] = newPin;
                balance[length+1] = 0.00;
                System.out.println("Account Created Successfully - Your Details are:");
                saveDataToFile("UserAccounts");
                return true;

            }
            
        }
        return false;

    }
    

    //@Override
    public boolean authenticateUser(String accountId, String pin) {
        int attempts = 3;
        while (attempts > 0) {
            System.out.println("Please enter UserId and 4 digit Pin separated by one space:");
            String input = scanner.nextLine();
            String[] parts = input.split(" ");
            accountId = parts[0];
            pin = parts[1];
            if (checkDetails(accountId,pin)) {
                System.out.println("Details Correct: Welcome " + name[index]);
                return true;
            } else {
                attempts--;
                System.out.println("Details Incorrect. Attempts Remaining: " + attempts);

            }
        }
        System.out.println("No Attempts Left. Application Closing...");
        return false;
    }
         
    

    //@Override
    public boolean checkDetails(String userId, String pin) {
        for (int j = 0; j < ID.length; j++) { //this can be changed to a for each loop
            //System.out.println(ID[j]);
            //System.out.println(PIN[j]);
            if (ID[j].equals(userId)) {
                index = j; // Store the index of the authenticated user
                //if (PIN[index] == Integer.parseInt(pin)) {
                //if (PIN[j] == pin) {
                if(PIN[index].equals(pin)){
                    return true;
                } else {
                    System.out.println("Error: Invalid Pin for chosen User");
                    return false;
                }
            }
        }
        return false; // No match found
    }

    //@Override
    public void displayMainMenu(){
        int option = 0;

        while(option != 5){
            System.out.println("Welcome " + name[index] + " Please enter the number corresponding with the service you require");
            System.out.println("1 - Check Balance");
            System.out.println("2 - Deposit");
            System.out.println("3 - Withdraw");
            System.out.println("4 - Change Pin");
            System.out.println("5 - Exit");

            //Scanner scanner = new Scanner(System.in);
            option = scanner.nextInt();
            scanner.nextLine();

            switch(option){
                case 1:
                    System.out.println("Your balance is: " + checkBalance(ID[index]));
                    //check balance function
                    break; //important as if you dont specify it will run every case after
                case 2: 
                    //deposit function
                    System.out.println("Enter an amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    deposit(ID[index] , depositAmount);
                    break;
                case 3: 
                    //withdraw function
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawAmount = scanner.nextDouble();
                    if (withdraw(ID[index], withdrawAmount)) {
                        System.out.println("Withdrawal successful.");
                    } 
                    else {
                        System.out.println("Withdrawal failed.");
                    }
                    break;
                case 4: 
                    //change pin function
                    System.out.println("Please Enter Current Pin:");
                    String OldPin = scanner.nextLine();
                    System.out.println("Please Enter New Pin:");
                    String NewPin = scanner.nextLine();
                    changePIN(" ",OldPin,NewPin);
                    break;
                case 5:
                    //exit function
                    exitATM();
                    break;
                default:
                    //probably break if number entered isnt 1 - 5
                    System.out.println("Invalid Entry: Please try again");
                    break;
            }
        }
        //allow user to enter number 
        //need some sort of loop to return and output the above main menu text until options = 5
        return;

    }
    //@Override
    public double checkBalance(String userId){
        return balance[index]; //return the element in the balance array in the index 
    }
    //@Override
    public boolean withdraw(String accountId, double amount){
        //take userID and find balance and subtract amount
        //output message saying the new balance afterwards
        //returning a bool so this functions purpose is to ensure that the withdrawl is physically possible
        if(amount > balance[index]){
            System.out.println("Test Message: withdraw function successfully flags impossible withdraw");
            return false;
        }
        else{
            balance[index] = balance[index] - amount;
            System.out.println("Test Message: Withdraw was possible, new balance is " + balance[index]);
            return true;
        }
    }
    //@Override
    public boolean changePIN(String accountId, String oldPin, String newPin){
        //Change PIN: Allow users to change their PIN by entering the current PIN and the new PIN. Validate the new PIN.
        //perhaps set it up so that you enter old pin and ensure this pin is correct and then enter the new pin 
        //after this new pin is entered maybe send used back to the start screen and get them to login again with the new pin
        //validate new pin could mean ensure its 4 digits -> research how the best way to confirm that
        if (PIN[index].equals(oldPin)) { //checks if current pin that was inputted is valid
            if (newPin.length() == 4) { //ensure new PIN is 4 digits
                PIN[index] = newPin; //change the new pin to be stored in PIN array at position index
                System.out.println("Test Message: Pin has been changed to " + PIN[index]);
                return true; //return true to show success 
            } else {
                System.out.println("New PIN must be 4 digits."); //fail because pin isnt 4 digits 
                return false;
            }
        } else {
            System.out.println("Current PIN is incorrect."); //if old pin is inputted incorrectly dont let user change the pin
            return false;
        }

    }
    //@Override
    public void exitATM(){
        //exiting atm would be the equivelant of returning to the login screen 
        //call the authenticate user function
        System.out.println("Exiting Session...");
        saveDataToFile("UserAccounts.txt");
        scanner.close();
        //return to authenticate user 
    }
    //@Override
    public void deposit(String accountId, double amount){
        //allows user to enter the amount they want to deposit and update the balance afterwards 
        balance[index] = balance[index] + amount;
        System.out.println("Balance has been updated to " + balance[index]);
        return;
    }



    public static void main(String[] a){
    //setup how the system works
        ATM atm = new ATM();
        //step 1: request user to input UserID and Pin
        //authenticateUser function
        //first parse txt file to populate the 4 arrays holding users data
        String filename = "UserAccounts.txt";
        atm.parseTxtFile(filename);

        //add starter menu - that will navigate to create an account or login
        


        //call the authenticate function by passing the userId and userPin through
        //this returns a boolean which will either approve access to main menu or quit main function
        if(atm.authenticateUser("","") == true){
            System.out.println("Test Point: Authenticate User returned true -> call main menu");
            atm.displayMainMenu(); //call main menu -> inside this method the sub methods (atm service options) can be called
        }
        else{
            return; //if the authenticate user returns false then just quit the main method
        }




    //step 2: Once user has authenticated themselves call the main menu 
    //user can go all sorts of ways from here 
    //make sure once a user finishes one of the options it brings them back to the main menu

    //Step 3: Gracefully leave the application when the user quits 


    }


}

    
