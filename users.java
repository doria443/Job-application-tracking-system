import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class users extends JFrame {
        private JTextField usernameField;
        private JPasswordField passwordPasswordField;
        private JRadioButton jobSeeker;
        private JRadioButton jobPoster;
        private Connection con;

        public users() {
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                con = DriverManager.getConnection("jdbc:sqlserver://localhost:56839;databaseName=JobApplication;encrypt=true;trustServerCertificate=true;", "mari", "1244");
                Statement stmt = con.createStatement();
                //String tableName = "CREATE TABLE USERS(" + "UserName VARCHAR(20)," + "Password VARCHAR(10)," + "role VARCHAR(15)" + ")";
               // stmt.executeUpdate(tableName);
               System.out.println("Table created successfully");
            } catch (SQLException sqlException) {
                System.out.print(sqlException.getMessage());
            } catch (ClassNotFoundException e) {
                System.out.print(e.getMessage());
            }

            setLayout(new GridLayout(4,2));

            JLabel usernameLabel = new JLabel("Username:");
            usernameField = new JTextField(10);
            add(usernameLabel);
            add(usernameField);

            JLabel passwordLabel = new JLabel("Password:");
            passwordPasswordField = new JPasswordField(10);
            add(passwordLabel);
            add(passwordPasswordField);

            JLabel roleLabel = new JLabel("Role:");
            ButtonGroup group = new ButtonGroup();
            jobSeeker = new JRadioButton("Job Seeker");
            jobPoster = new JRadioButton("Job Poster");
            group.add(jobSeeker);
            group.add(jobPoster);
            add(roleLabel);
            add(jobSeeker);
            add(jobPoster);


            JButton addButton = new JButton("Add");
            addButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String username = usernameField.getText();
                    String password = new String(passwordPasswordField.getPassword());
                    String role = jobSeeker.isSelected() ? "Job Seeker" : jobPoster.isSelected() ? "Job Poster" : "";

                    if (username.isEmpty() || password.isEmpty() || role.isEmpty()) {
                        JOptionPane.showMessageDialog(users.this, "Please fill all fields");
                        return;
                    }

                    try {
                        if (!isUsernameExist(username)) {
                            addRecord(username, password, role);
                        } else {
                            if (isTrue(username, password)) {

                                if (role.equals("Job Seeker")) {

                                    jobseekerr j = new jobseekerr();

                                } else {

                                   jobposterr jobPosterInstance = new jobposterr();
                                }
                            } else {
                                JOptionPane.showMessageDialog(users.this, "Invalid username or password");
                            }
                        }
                        usernameField.setText("");
                        passwordPasswordField.setText("");
                        jobSeeker.setSelected(false);
                        jobPoster.setSelected(false);
                    } catch (SQLException sqlException) {
                        JOptionPane.showMessageDialog(users.this, "Failed to add record: " + sqlException.getMessage());
                    }
                }
            });
            add(addButton);

            setSize(400, 200);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setVisible(true);
        }

        public void addRecord(String username, String password, String role) throws SQLException {
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                con = DriverManager.getConnection("jdbc:sqlserver://localhost:56839;" +
                        "databaseName=JobApplication;encrypt=true;trustServerCertificate=true;", "mari", "1244");
                Statement stmt = con.createStatement();
            PreparedStatement ps = con.prepareStatement("INSERT INTO USERS(UserName, Password, role) VALUES(?,?,?)");
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);
            int i = ps.executeUpdate();
            System.out.println(i + " records inserted");
        } catch(SQLException sqlException) {
        System.out.print(sqlException.getMessage());
    } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
    }
        }

        public boolean isUsernameExist(String username) throws SQLException {
            PreparedStatement ps = con.prepareStatement("SELECT * from USERS where UserName=?");
            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();
            return resultSet.next();
        }

        public boolean isTrue(String username, String password) throws SQLException {
            PreparedStatement ps = con.prepareStatement("SELECT * from USERS where UserName=? AND Password=?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet resultSet = ps.executeQuery();
            return resultSet.next();
        }


    }

