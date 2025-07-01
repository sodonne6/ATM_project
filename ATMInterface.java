/*
 * 
 *      -Fix syntax errors and errors
 *      -fill in blank bits and switch statement for function calls
 *      -Maybe try to change it so it parses the txt file 
 *      -set up main function and structure the methods and ensure theres no deadends 
 *          -eg. if in main menu ensure when you call a service it always brings you back to main menu as theres an exit path there
 *          
 */

public interface ATMInterface {
    
    // Method to authenticate the user
    boolean authenticateUser(String accountId, String pin);
    
    // Method to check the account balance
    double checkBalance(String accountId);
    
    // Method to deposit money into the account
    void deposit(String accountId, double amount);
    
    // Method to withdraw money from the account
    boolean withdraw(String accountId, double amount);
    
    // Method to change the user's PIN
    boolean changePIN(String accountId, String oldPin, String newPin);
    
    // Method to display the main menu options
    void displayMainMenu();
    
    // Method to exit the ATM application
    void exitATM();
}

//data access from txt file
//structured like a csv so split up data for each user accordingly
//prompt user for name first and then pin



