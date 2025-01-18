import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ApplicationForm {
    private String jobId; // Unique identifier for the job

    // Add necessary components for the application form
    private JTextField nameField;
    private JTextField cityField;
    private JTextField addressField;
    private JTextField highSchoolField;
    private JTextArea coverLetterArea;
    private JButton submitButton;
    private Connection con;
    private JFrame j;

    public ApplicationForm(String jobId) {

        this.jobId = jobId;
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection("jdbc:sqlserver://localhost:56839;" + "databaseName=JobApplication;encrypt=true;trustServerCertificate=true;", "mari", "1244");
            Statement stmt = con.createStatement();
            String table2 = "CREATE TABLE ApplicationForm(" +"jobTitle varchar(50),"+ "ApplicantID INT," + "FullName VARCHAR(40)," +"city varchar(50),"+"Address varchar(50),"+"highSchool varchar(50),"+")";
            stmt.executeUpdate(table2);
            System.out.println("table created successfully");
        }catch(SQLException s) {
            System.out.println(s.getMessage());
        }catch(ClassNotFoundException c){
            throw new RuntimeException(c);

        }

        j = new JFrame("Job Application Form"); // Create an instance of JFrame
        j.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        j.setSize(400, 300);

        JPanel formPanel = new JPanel();
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Create and add form components
        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField(20);
        formPanel.add(nameField);

        formPanel.add(new JLabel("City:"));
        cityField = new JTextField(20);
        formPanel.add(cityField);

        formPanel.add(new JLabel("Address:"));
        addressField = new JTextField(20);
        formPanel.add(addressField);

        formPanel.add(new JLabel("High School:"));
        highSchoolField = new JTextField(20);
        formPanel.add(highSchoolField);


        submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitForm();


            }
        });
        formPanel.add(submitButton);

        j.setContentPane(formPanel); // Set formPanel as the content pane
        j.setVisible(true); // Make the frame visible
    }

    public void submitForm () {
        if (jobId == null) {
            JOptionPane.showMessageDialog(null, "Error submitting application: JobId is null");
            return;
        }
        try{
            String name = nameField.getText();
            String city = cityField.getText();
            String address = addressField.getText();
            String highSchool = highSchoolField.getText();

            // Get the JobPosterId from the JobPoster table
            Statement stmt = con.createStatement();
            String query = "SELECT ApplicantID FROM AApplicant WHERE FullName = '" + name + "'";
            ResultSet rs = stmt.executeQuery(query);
            int applicantId;
            if (rs.next()) {
                applicantId = rs.getInt("ApplicantID");
                // Insert the application data into the database
                PreparedStatement stmt2 = con.prepareStatement("INSERT INTO ApplicationForm (jobTitle, ApplicantID, FullName, City, Address, HighSchool) VALUES (?, ?, ?, ?, ?, ?)");
                stmt2.setString(1, jobId);
                stmt2.setInt(2, applicantId);
                stmt2.setString(3, name);
                stmt2.setString(4, city);
                stmt2.setString(5, address);
                stmt2.setString(6, highSchool);
                stmt2.executeUpdate();

                // Close the connection
                con.close();

                // Close the frame after submission
                System.exit(0);

            } else {
                JOptionPane.showMessageDialog(null, "Error submitting application: Job Poster not found");
            }
        } catch (SQLException s) {
            JOptionPane.showMessageDialog(null, "Error submitting application: " + s.getMessage());
        }
    }}

