import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class User {
    public String userId;
    public String pin;
    private double balance;
    ArrayList<String> transactionHistory;

    public User(String userId, String pin, double balance) {
        this.userId = userId;
        this.pin = pin;
        this.balance = balance;
        this.transactionHistory = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public double getBalance() {
        return balance;
    }

    public void addTransaction(String transaction) {
        transactionHistory.add(transaction);
    }

    public void addBalance(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public void subtractBalance(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }
}

class ATM {
    private final Map<String, User> users;
    private User currentUser;

    public ATM() {
        users = new HashMap<>();
        users.put("Sandeep123", new User("Sandeep123", "9392", 1000.0));
        users.put("Swaroop456", new User("Swaroop456", "5678", 1500.0));
        users.put("Roshitha789", new User("Roshitha789", "4854", 1700.0));
        users.put("Umesh011", new User("Umesh011", "5088", 1300.0));
        users.put("Raja678", new User("Raja678", "0999", 1600.0));
        users.put("Shiva087", new User("Shiva087", "8730", 1900.0));

        // Add more users as needed
    }

    public boolean authenticateUser(String userId, String pin) {
        if (users.containsKey(userId)) {
            User user = users.get(userId);
            if (user != null && user.getUserId().equals(userId) && user.pin.equals(pin)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    public void displayMainMenu() {
        System.out.println("Welcome, " + currentUser.getUserId() + "!");
        System.out.println("1. View Balance");
        System.out.println("2. Withdraw");
        System.out.println("3. Deposit");
        System.out.println("4. Transfer");
        System.out.println("5. Transaction History");
        System.out.println("6. Quit");
    }

    public void performTransaction(int choice) {
        Scanner scanner = new Scanner(System.in);
        switch (choice) {
            case 1:
                System.out.println("Balance: $" + currentUser.getBalance());
                break;
            case 2:
                System.out.print("Enter the withdrawal amount: $");
                double withdrawalAmount = scanner.nextDouble();
                scanner.nextLine(); // Consume the newline character

                if (currentUser.withdraw(withdrawalAmount)) {
                    currentUser.addTransaction("Withdrawn $" + withdrawalAmount);
                    System.out.println("Withdrawal successful!");
                } else {
                    System.out.println("Invalid withdrawal amount or insufficient balance.");
                }
                break;
            case 3:
                System.out.print("Enter the deposit amount: $");
                double depositAmount = scanner.nextDouble();
                scanner.nextLine(); // Consume the newline character

                if (depositAmount > 0) {
                    currentUser.addTransaction("Deposited $" + depositAmount);
                    currentUser.addBalance(depositAmount);
                    System.out.println("Deposit successful!");
                } else {
                    System.out.println("Invalid deposit amount. Please enter a positive amount.");
                }
                break;
            case 4:
                System.out.print("Enter the recipient's User ID: ");
                String recipientId = scanner.nextLine();
                System.out.print("Enter the transfer amount: $");
                double transferAmount = scanner.nextDouble();
                scanner.nextLine(); // Consume the newline character

                if (users.containsKey(recipientId)) {
                    if (currentUser.getBalance() >= transferAmount && transferAmount > 0) {
                        User recipient = users.get(recipientId);
                        currentUser.addTransaction("Transferred $" + transferAmount + " to " + recipientId);
                        recipient.addTransaction("Received $" + transferAmount + " from " + currentUser.getUserId());
                        currentUser.subtractBalance(transferAmount);
                        recipient.addBalance(transferAmount);
                        System.out.println("Transfer successful!");
                    } else {
                        System.out.println("Invalid transfer amount or insufficient balance.");
                    }
                } else {
                    System.out.println("Recipient not found.");
                }
                break;
            case 5:
                System.out.println("Transaction History:");
                for (String transaction : currentUser.transactionHistory) {
                    System.out.println(transaction);
                }
                break;
            case 6:
                System.out.println("Thank you for using the ATM. Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
}

public class ATMInterface {
    public static void main(String[] args) {
        ATM atm = new ATM();
        Scanner scanner = new Scanner(System.in); 

        while (true) {
            System.out.print("Enter your User ID: ");
            String userId = scanner.nextLine();
            System.out.print("Enter your PIN: ");
            String pin = scanner.nextLine();

            if (atm.authenticateUser(userId, pin)) {
                while (true) {
                    atm.displayMainMenu();
                    System.out.print("Enter your choice: ");
                    int choice = scanner.nextInt();
                    scanner.nextLine(); 

                    atm.performTransaction(choice);
                }
            } else {
                System.out.println("Authentication failed. Please try again.");
            }
        }
    }
}

