import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

class Account {
    protected String accountNumber;
    protected String accountHolderName;
    protected double balance;
    private ArrayList<String> transactionHistory;

    public Account(String accountNumber, String accountHolderName, double initialDeposit) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialDeposit;
        this.transactionHistory = new ArrayList<>();
        this.transactionHistory.add("Account Created: Initial Deposit $" + String.format("%.2f", initialDeposit));
    }

    public boolean deposit(double amount, boolean record) {
        balance += amount;
        if (record) {
            transactionHistory.add("Deposit: $" + String.format("%.2f", amount));
        }
        return true;
    }

    public boolean withdraw(double amount, boolean record) {
        if (amount <= balance) {
            balance -= amount;
            if (record) {
                transactionHistory.add("Withdrawal: $" + String.format("%.2f", amount));
            }
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Insufficient balance in Account " + accountNumber + "!", "Transfer Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public double getBalance() {
        return balance;
    }

    public ArrayList<String> getTransactionHistory() {
        return transactionHistory;
    }

    @Override
    public String toString() {
        return accountNumber;
    }
}

public class BankGUI extends JFrame {
    ArrayList<Account> accounts = new ArrayList<>();
    private static final String VALID_PIN = "1234"; 
    private JTextArea detailsArea;
    private JList<Account> accountList;
    private DefaultListModel<Account> accountListModel;
    private JButton viewTransactionsBtn;
    private JButton transferBtn;

    public BankGUI() {
        if (!validatePin()) {
            JOptionPane.showMessageDialog(this, "Access Denied. Incorrect PIN.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        setTitle("Enhanced Bank Management System - Comprehensive Transactions");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Welcome to Secure Bank Portal", JLabel.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setForeground(new Color(40, 60, 90));
        header.setOpaque(true);
        header.setBackground(new Color(220, 230, 245));
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(header, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(240, 245, 250));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBackground(new Color(230, 235, 245));

        JPanel buttonPanel = new JPanel(new GridLayout(7, 1, 10, 10));
        buttonPanel.setBackground(new Color(230, 235, 245));

        JButton createBtn = new JButton("Create Account");
        JButton depositBtn = new JButton("Deposit");
        JButton withdrawBtn = new JButton("Withdraw");
        JButton viewDetailsBtn = new JButton("View Details");
        viewTransactionsBtn = new JButton("View Transactions");
        transferBtn = new JButton("Transfer Funds");
        JButton exitBtn = new JButton("Exit");

        buttonPanel.add(createBtn);
        buttonPanel.add(depositBtn);
        buttonPanel.add(withdrawBtn);
        buttonPanel.add(viewDetailsBtn);
        buttonPanel.add(viewTransactionsBtn);
        buttonPanel.add(transferBtn);
        buttonPanel.add(exitBtn);

        Color buttonColor = new Color(70, 130, 180);
        Color textColor = Color.WHITE;

        for (Component c : buttonPanel.getComponents()) {
            if (c instanceof JButton btn) {
                btn.setBackground(buttonColor);
                btn.setForeground(textColor);
                btn.setFocusPainted(false);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            }
        }

        leftPanel.add(buttonPanel, BorderLayout.NORTH);

        accountListModel = new DefaultListModel<>();
        accountList = new JList<>(accountListModel);
        accountList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        accountList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        accountList.setBackground(new Color(250, 250, 255));

        JScrollPane listScrollPane = new JScrollPane(accountList);
        listScrollPane.setBorder(BorderFactory.createTitledBorder("Account Numbers"));
        leftPanel.add(listScrollPane, BorderLayout.CENTER);

        mainPanel.add(leftPanel, BorderLayout.WEST);

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        detailsArea.setBackground(new Color(255, 255, 255));
        detailsArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JScrollPane detailsScrollPane = new JScrollPane(detailsArea);
        detailsScrollPane.setBorder(BorderFactory.createTitledBorder("Account Details"));
        mainPanel.add(detailsScrollPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        createBtn.addActionListener(e -> createAccount());
        depositBtn.addActionListener(e -> deposit());
        withdrawBtn.addActionListener(e -> withdraw());
        viewDetailsBtn.addActionListener(e -> displaySelectedAccountDetails());
        viewTransactionsBtn.addActionListener(e -> viewTransactionHistory());
        transferBtn.addActionListener(e -> transferFunds());
        exitBtn.addActionListener(e -> System.exit(0));

        accountList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    displaySelectedAccountDetails();
                }
            }
        });

        detailsArea.setText("Select an account from the list to view its details.\n");
        setVisible(true);
    }

    private boolean validatePin() {
        String enteredPin = JOptionPane.showInputDialog(this, "Enter PIN to access your account:", "Login", JOptionPane.PLAIN_MESSAGE);
        return VALID_PIN.equals(enteredPin);
    }

    void createAccount() {
        JTextField accNoField = new JTextField(15);
        JTextField nameField = new JTextField(15);
        JTextField depositField = new JTextField(15);

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Account Number:"));
        panel.add(accNoField);
        panel.add(new JLabel("Account Holder Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Initial Deposit:"));
        panel.add(depositField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Create New Account", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String accNo = accNoField.getText().trim();
                String name = nameField.getText().trim();
                String depositStr = depositField.getText().trim();
                if (accNo.isEmpty() || name.isEmpty() || depositStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All fields are required!", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                double deposit = Double.parseDouble(depositStr);
                if (findAccount(accNo) == null) {
                    Account newAccount = new Account(accNo, name, deposit);
                    accounts.add(newAccount);
                    accountListModel.addElement(newAccount);
                    JOptionPane.showMessageDialog(this, "Account Created Successfully! Account Number: " + accNo, "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearDetailsArea();
                    displayAccountDetails(newAccount);
                } else {
                    JOptionPane.showMessageDialog(this, "Account number already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid deposit amount!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    Account findAccount(String accNo) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber().equals(accNo)) {
                return acc;
            }
        }
        return null;
    }

    void deposit() {
        String accNo = JOptionPane.showInputDialog(this, "Enter Account Number for Deposit:", "Deposit", JOptionPane.PLAIN_MESSAGE);
        Account acc = findAccount(accNo);

        if (acc != null) {
            String amtStr = JOptionPane.showInputDialog(this, "Enter Amount to Deposit:", "Deposit", JOptionPane.PLAIN_MESSAGE);
            try {
                double amt = Double.parseDouble(amtStr);
                if (amt > 0) {
                    acc.deposit(amt, true);
                    updateAccountListDisplay();
                    displayAccountDetails(acc);
                    JOptionPane.showMessageDialog(this, "Deposited $" + String.format("%.2f", amt) + " into Account " + accNo, "Deposit Successful", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Please enter a positive amount.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Account not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    void withdraw() {
        String accNo = JOptionPane.showInputDialog(this, "Enter Account Number for Withdrawal:", "Withdraw", JOptionPane.PLAIN_MESSAGE);
        Account acc = findAccount(accNo);

        if (acc != null) {
            String amtStr = JOptionPane.showInputDialog(this, "Enter Amount to Withdraw:", "Withdraw", JOptionPane.PLAIN_MESSAGE);
            try {
                double amt = Double.parseDouble(amtStr);
                if (amt > 0) {
                    if (acc.withdraw(amt, true)) {
                        updateAccountListDisplay();
                        displayAccountDetails(acc);
                        JOptionPane.showMessageDialog(this, "Withdrew $" + String.format("%.2f", amt) + " from Account " + accNo, "Withdrawal Successful", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Please enter a positive amount.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Account not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    void transferFunds() {
        String fromAccNo = JOptionPane.showInputDialog(this, "Enter Account Number to Transfer From:", "Transfer Funds", JOptionPane.PLAIN_MESSAGE);
        Account fromAcc = findAccount(fromAccNo);

        if (fromAcc != null) {
            String toAccNo = JOptionPane.showInputDialog(this, "Enter Account Number to Transfer To:", "Transfer Funds", JOptionPane.PLAIN_MESSAGE);
            Account toAcc = findAccount(toAccNo);

            if (toAcc != null) {
                if (fromAcc == toAcc) {
                    JOptionPane.showMessageDialog(this, "Cannot transfer funds to the same account.", "Transfer Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String amtStr = JOptionPane.showInputDialog(this, "Enter Amount to Transfer:", "Transfer Funds", JOptionPane.PLAIN_MESSAGE);
                try {
                    double amt = Double.parseDouble(amtStr);
                    if (amt > 0) {
                        if (fromAcc.withdraw(amt, true)) {
                            toAcc.deposit(amt, true);
                            updateAccountListDisplay();
                            displayAccountDetails(fromAcc);
                            JOptionPane.showMessageDialog(this, "Successfully transferred $" + String.format("%.2f", amt) + " from Account " + fromAccNo + " to Account " + toAccNo, "Transfer Successful", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Please enter a positive amount.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Recipient account not found!", "Transfer Failed", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Source account not found!", "Transfer Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    void displayAccountDetails(Account acc) {
        detailsArea.setText("");
        if (acc != null) {
            detailsArea.append("Account Number: " + acc.getAccountNumber() + "\n");
            detailsArea.append("Account Holder: " + acc.getAccountHolderName() + "\n");
            detailsArea.append("Balance: $" + String.format("%.2f", acc.getBalance()) + "\n");
        } else {
            detailsArea.append("No account selected or found.\n");
        }
    }

    void displaySelectedAccountDetails() {
        Account selectedAccount = accountList.getSelectedValue();
        displayAccountDetails(selectedAccount);
    }

    void viewTransactionHistory() {
        Account selectedAccount = accountList.getSelectedValue();
        if (selectedAccount != null) {
            ArrayList<String> history = selectedAccount.getTransactionHistory();
            if (!history.isEmpty()) {
                JTextArea transactionTextArea = new JTextArea();
                transactionTextArea.setEditable(false);
                for (String transaction : history) {
                    transactionTextArea.append(transaction + "\n");
                }
                JScrollPane scrollPane = new JScrollPane(transactionTextArea);
                scrollPane.setPreferredSize(new Dimension(450, 350));
                JOptionPane.showMessageDialog(this, scrollPane, "Transaction History for Account " + selectedAccount.getAccountNumber(), JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No transaction history available for this account.", "Transaction History", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an account to view transaction history.", "Transaction History", JOptionPane.WARNING_MESSAGE);
        }
    }

    void updateAccountListDisplay() {
        int selectedIndex = accountList.getSelectedIndex();
        accountListModel.clear();
        for (Account acc : accounts) {
            accountListModel.addElement(acc);
        }
        if (selectedIndex != -1 && selectedIndex < accountListModel.size()) {
            accountList.setSelectedIndex(selectedIndex);
        }
    }

    void clearDetailsArea() {
        detailsArea.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BankGUI());
    }
}