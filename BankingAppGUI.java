import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class BankDetails {
    private String accno;
    private String name;
    private String acc_type;
    private long balance;

    public BankDetails(String accno, String name, String acc_type, long balance) {
        this.accno = accno;
        this.name = name;
        this.acc_type = acc_type;
        this.balance = balance;
    }

    public void deposit(long amt) {
        balance += amt;
    }

    public void withdraw(long amt) {
        if (balance >= amt) {
            balance -= amt;
            JOptionPane.showMessageDialog(null, "Withdrawal successful. New balance: " + balance);
        } else {
            JOptionPane.showMessageDialog(null, "Insufficient balance. Transaction failed.");
        }
    }

    public boolean search(String acc_no) {
        return accno.equals(acc_no);
    }

    public String display() {
        return "Account No: " + accno + "\nName: " + name + "\nAccount Type: " + acc_type + "\nBalance: " + balance + "\n";
    }
}

public class BankingAppGUI {
    static BankDetails[] accounts = new BankDetails[100];
    static int accountCount = 0;

    // Define colors for dark theme
    static Color backgroundColor = new Color(45, 45, 45);
    static Color foregroundColor = new Color(225, 225, 225);
    static Color buttonColor = new Color(75, 75, 75);
    static Color textFieldColor = new Color(60, 60, 60);
    static Color selectedButtonColor = new Color(100, 100, 100);

    static String selectedAccountType = "Savings"; // Default account type

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Banking System");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(backgroundColor);

        // Components
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(backgroundColor);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel accNoLabel = createLabel("Account No:");
        JTextField accNoField = createTextField();
        JLabel nameLabel = createLabel("Name:");
        JTextField nameField = createTextField();
        JLabel accTypeLabel = createLabel("Account Type:");
        
        JPanel accountTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        accountTypePanel.setBackground(backgroundColor);
        JButton savingsBtn = createButton("Savings");
        JButton fixedBtn = createButton("Fixed");
        
        savingsBtn.addActionListener(e -> {
            selectedAccountType = "Savings";
            savingsBtn.setBackground(selectedButtonColor);
            fixedBtn.setBackground(buttonColor);
        });
        fixedBtn.addActionListener(e -> {
            selectedAccountType = "Fixed";
            fixedBtn.setBackground(selectedButtonColor);
            savingsBtn.setBackground(buttonColor);
        });
        
        accountTypePanel.add(savingsBtn);
        accountTypePanel.add(fixedBtn);

        JLabel balanceLabel = createLabel("Balance:");
        JTextField balanceField = createTextField();

        JTextArea displayArea = new JTextArea(10, 30);
        displayArea.setEditable(false);
        displayArea.setBackground(textFieldColor);
        displayArea.setForeground(foregroundColor);
        displayArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(buttonColor));

        JButton addAccountBtn = createButton("Add Account");
        JButton searchBtn = createButton("Search Account");
        JButton depositBtn = createButton("Deposit");
        JButton withdrawBtn = createButton("Withdraw");

        // Add components to panel
        gbc.gridx = 0; gbc.gridy = 0; panel.add(accNoLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(accNoField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(nameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(accTypeLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(accountTypePanel, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(balanceLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(balanceField, gbc);
        gbc.gridx = 0; gbc.gridy = 4; panel.add(addAccountBtn, gbc);
        gbc.gridx = 1; gbc.gridy = 4; panel.add(searchBtn, gbc);
        gbc.gridx = 0; gbc.gridy = 5; panel.add(depositBtn, gbc);
        gbc.gridx = 1; gbc.gridy = 5; panel.add(withdrawBtn, gbc);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Event handling for Add Account
        addAccountBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String accno = accNoField.getText();
                
                boolean accountExists = false;
                for (int i = 0; i < accountCount; i++) {
                    if (accounts[i].search(accno)) {
                        accountExists = true;
                        break;
                    }
                }
                
                if (accountExists) {
                    JOptionPane.showMessageDialog(frame, "Error: Account number already exists!", "Duplicate Account", JOptionPane.ERROR_MESSAGE);
                } else {
                    String name = nameField.getText();
                    long balance = Long.parseLong(balanceField.getText());

                    accounts[accountCount++] = new BankDetails(accno, name, selectedAccountType, balance);
                    JOptionPane.showMessageDialog(frame, "Account added successfully!");

                    accNoField.setText("");
                    nameField.setText("");
                    balanceField.setText("");
                    selectedAccountType = "Savings";
                    savingsBtn.setBackground(selectedButtonColor);
                    fixedBtn.setBackground(buttonColor);
                }
            }
        });

        // Event handling for Search Account
        searchBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String accno = JOptionPane.showInputDialog("Enter Account No to Search:");
                boolean found = false;
                for (int i = 0; i < accountCount; i++) {
                    if (accounts[i].search(accno)) {
                        displayArea.setText(accounts[i].display());
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    JOptionPane.showMessageDialog(frame, "Account not found!");
                }
            }
        });

        // Event handling for Deposit
        depositBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String accno = JOptionPane.showInputDialog("Enter Account No:");
                boolean found = false;
                for (int i = 0; i < accountCount; i++) {
                    if (accounts[i].search(accno)) {
                        long amount = Long.parseLong(JOptionPane.showInputDialog("Enter Deposit Amount:"));
                        if (amount < 0) {
                            JOptionPane.showMessageDialog(frame, "Error: Deposit amount cannot be negative!", "Invalid Amount", JOptionPane.ERROR_MESSAGE);
                        } else {
                            accounts[i].deposit(amount);
                            JOptionPane.showMessageDialog(frame, "Deposit successful!");
                        }
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    JOptionPane.showMessageDialog(frame, "Account not found!");
                }
            }
        });

        // Event handling for Withdraw
        withdrawBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String accno = JOptionPane.showInputDialog("Enter Account No:");
                boolean found = false;
                for (int i = 0; i < accountCount; i++) {
                    if (accounts[i].search(accno)) {
                        long amount = Long.parseLong(JOptionPane.showInputDialog("Enter Withdraw Amount:"));
                        if (amount < 0) {
                            JOptionPane.showMessageDialog(frame, "Error: Withdrawal amount cannot be negative!", "Invalid Amount", JOptionPane.ERROR_MESSAGE);
                        } else {
                            accounts[i].withdraw(amount);
                        }
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    JOptionPane.showMessageDialog(frame, "Account not found!");
                }
            }
        });

        frame.setVisible(true);
    }

    private static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(foregroundColor);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        return label;
    }

    private static JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setBackground(textFieldColor);
        textField.setForeground(foregroundColor);
        textField.setCaretColor(foregroundColor); // Set caret color
        return textField;
    }

    private static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(buttonColor);
        button.setForeground(Color.BLACK); // Change text color to white
        return button;
    }
}
