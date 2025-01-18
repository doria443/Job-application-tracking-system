import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class jobposterr implements JobPposter{
    private JFrame jobPosterFrame;
    private JTabbedPane tabbedPane;
    private JTextField fullNameField;
    private JButton registerButton;
    private JTextField contactInformationField;
    private JTextField jobTitleField;
    private JTextField jtfield;
    private JTextArea jobDescriptionField;
    private JTextField  OpenPositionsFeild;
    private JButton postJobButton;
    private JButton updateButton;
    private JTextField contactInformationUpdateField;
    private JTextField fullNameUpdateField;
    private  JTextField gpaField;
    private JTextField genderField;
    private JTextField experienceField;
    //private JButton reviewFormButton;
    private Connection con;
    public jobposterr(){
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection con = DriverManager.getConnection("jdbc:sqlserver://localhost:56839;" + "databaseName=JobApplication;encrypt=true;trustServerCertificate=true;", "mari", "1244");
            Statement stmt = con.createStatement();
            //String table2 = "CREATE TABLE JobPoster(" + "jobPosterId INT PRIMARY KEY IDENTITY(1,1)," + "FullName VARCHAR(40)," + "contactInfo VARCHAR(15),)";
            // stmt.executeUpdate(table2);
            // System.out.println("Table created successfully");
            //String table3 = "CREATE TABLE postJobs(" + "JobId INT PRIMARY KEY IDENTITY(1,1)," + "jobPosterId INT," + "JobTitle VARCHAR(15)," + "JobDescription VARCHAR(100),"+ "OpenPositions INT,"+"PriorityGPA DECIMAL(5,2),"+" PriorityExperience INT,"+" PriorityGender CHAR,"+")";
            // stmt.executeUpdate(table3);
            //System.out.println("Table created successfully");
            // String query = "ALTER TABLE postJobs ADD CONSTRAINT fk_JobPoster FOREIGN KEY (jobPosterId) REFERENCES JobPoster(jobPosterId)";
            //stmt.executeUpdate(query);
            String table4="CREATE TABLE recentlyPostedJobs("+"id INT PRIMARY KEY IDENTITY(1,1),"+"JobId INT," + "jobPosterId INT," + "JobTitle VARCHAR(15)," + "JobDescription VARCHAR(100),"+ "OpenPositions INT,"+"PriorityGPA DECIMAL(5,2),"+" PriorityExperience INT,"+" PriorityGender CHAR,"+")";
            stmt.executeUpdate(table4);
            System.out.println("Table created successfully");
            String query1 = "ALTER TABLE recentlyPostedJobs ADD CONSTRAINT fk_postJobs FOREIGN KEY (jobId) REFERENCES postJobs(jobId)";
            stmt.executeUpdate(query1);
        }catch(SQLException s){
            System.out.println(s.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        jobPosterFrame = new JFrame("Job Poster");
        jobPosterFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a tabbed pane with two tabs: Register and Post Job
        tabbedPane = new JTabbedPane();

        // Register Tab
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new GridLayout(3, 2));

        JLabel fullNameLabel = new JLabel("Full Name:");
        fullNameField = new JTextField();
        registerPanel.add(fullNameLabel);
        registerPanel.add(fullNameField);

        JLabel contactInformationLabel = new JLabel("Contact Information:");
        contactInformationField = new JTextField();
        registerPanel.add(contactInformationLabel);
        registerPanel.add(contactInformationField);

        registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    registerForm();
                } catch (SQLException sqlException) {
                    JOptionPane.showMessageDialog(null, "Error registering: " + sqlException.getMessage());
                }
            }
        });
        registerPanel.add(registerButton);

        tabbedPane.addTab("Register", registerPanel);

        // Post Job Tab
        JPanel postJobPanel = new JPanel();
        postJobPanel.setLayout(new GridLayout(7, 2));

        JLabel jobTitleLabel = new JLabel("Job Title:");
        jobTitleField = new JTextField();
        jobTitleField.setEditable(true);
        postJobPanel.add(jobTitleLabel);
        postJobPanel.add(jobTitleField);
        JLabel jtLabel = new JLabel("Job Title:");

       // postJobPanel.add(jtLabel);
       // postJobPanel.add(jtfield);

        JLabel jobDescriptionLabel = new JLabel("Job Description:");
        jobDescriptionField = new JTextArea(5, 20);
        jobDescriptionField.setEditable(true);
        postJobPanel.add(jobDescriptionLabel);

        JScrollPane jobDescriptionScrollPane = new JScrollPane(jobDescriptionField);
        postJobPanel.add(jobDescriptionScrollPane);
        JLabel OpenPositionsLabel=new JLabel("number of Open Positions");
        OpenPositionsFeild=new JTextField();
        OpenPositionsFeild.setEditable(true);
        postJobPanel.add(OpenPositionsLabel);
        postJobPanel.add(OpenPositionsFeild);


        JLabel gpaLabel = new JLabel("GPA:");
        gpaField = new JTextField();
        gpaField.setEditable(true);
        postJobPanel.add(gpaLabel);
        postJobPanel.add(gpaField);



        JLabel genderLabel = new JLabel("Gender (M/F):");
        genderField = new JTextField();
        genderField.setEditable(true);
        postJobPanel.add(genderLabel);
        postJobPanel.add(genderField);



        JLabel experienceLabel = new JLabel("Experience (in years):");
        experienceField = new JTextField();
        experienceField.setEditable(true);
        postJobPanel.add(experienceLabel);
        postJobPanel.add(experienceField);







// Create a new JButton to update the status of a job application
        JTable jTable1 = new JTable();
        DefaultTableModel tableModel = new DefaultTableModel();
// Set up the table model
        JButton updateStatusButton = new JButton("Update Status");
        updateStatusButton.addActionListener(new UpdateStatusActionListener());
        JButton reviewFormButton=new JButton("review from");
         reviewFormButton.addActionListener(new ReviewForm());
        //jobApplicationsPanel.add(updateStatusButton);
        // tabbedPane.add("update status",jobApplicationsPanel);


        postJobButton = new JButton("Post Job");
        postJobButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    postJob();
                    postRecentJobs();
                } catch (SQLException sqlException) {
                    JOptionPane.showMessageDialog(null, "Error posting job: " + sqlException.getMessage());
                }
            }
        });
        postJobPanel.add(postJobButton);

        tabbedPane.addTab("Post Job", postJobPanel);
        JPanel updateRegistrationPanel = new JPanel();
        updateRegistrationPanel.setLayout(new GridLayout(3, 2));

        JLabel fullNameUpdateLabel = new JLabel("Full Name:");
        fullNameUpdateField = new JTextField();
        updateRegistrationPanel.add(fullNameUpdateLabel);
        updateRegistrationPanel.add(fullNameUpdateField);

        JLabel contactInformationUpdateLabel = new JLabel("Contact Information:");
        contactInformationUpdateField = new JTextField();
        updateRegistrationPanel.add(contactInformationUpdateLabel);
        updateRegistrationPanel.add(contactInformationUpdateField);


        updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    updateRegistration();
                } catch (SQLException sqlException) {
                    JOptionPane.showMessageDialog(null, "Error updating registration: " + sqlException.getMessage());
                }
            }
        });
        updateRegistrationPanel.add(updateButton);

        tabbedPane.addTab("Update Registration", updateRegistrationPanel);
        JPanel reviewApplicationsPanel = new JPanel();
        reviewApplicationsPanel.add(new JLabel("Enter the job title:"));
        jtfield = new JTextField(20);

        reviewApplicationsPanel.add(jtfield);

        tabbedPane.add("review applications",reviewApplicationsPanel);
        JButton reviewApplicationsButton = new JButton("Review Applications");
        reviewApplicationsPanel.add(reviewApplicationsButton);
        reviewApplicationsPanel.add(updateStatusButton);
        reviewApplicationsPanel.add(reviewFormButton);
        reviewApplicationsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reviewApplications();
                JButton updateStatusButton = new JButton("Update Status");

                updateStatusButton.addActionListener(new UpdateStatusActionListener());
               JButton reviewFormButton=new JButton("review form");
                reviewFormButton.addActionListener(new ReviewForm());
            }});

        //reviewApplicationsPanel.add(updateStatusButton);
        jobPosterFrame.add(tabbedPane);
        jobPosterFrame.pack();
        jobPosterFrame.setVisible(true);

    }


    public void registerForm() throws SQLException {
        try {
            Connection con = DriverManager.getConnection("jdbc:sqlserver://localhost:56839;" + "databaseName=JobApplication;encrypt=true;trustServerCertificate=true;", "mari", "1244");
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO JobPoster (FullName, contactInfo) VALUES (?, ?)");
            pstmt.setString(1, fullNameField.getText());
            pstmt.setString(2, contactInformationField.getText());
            pstmt.executeUpdate();
            con.commit();
            JOptionPane.showMessageDialog(null, "Registered successfully!");
        } catch (SQLException sqlException) {
            JOptionPane.showMessageDialog(null, "Error registering: " + sqlException.getMessage());
        }
    }

    public void postJob() throws SQLException {

        try {
            Connection con = DriverManager.getConnection("jdbc:sqlserver://localhost:56839;" + "databaseName=JobApplication;encrypt=true;trustServerCertificate=true;", "mari", "1244");
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO postJobs(jobPosterId, JobTitle, JobDescription,OpenPositions,PriorityGPA,PriorityExperience, PriorityGender) VALUES (?,?,?,?,?,?,?)");
            int jobPosterId = getJobPosterId()-1;
            String jobTitle = jobTitleField.getText();
            pstmt.setInt(1, jobPosterId);
            if (jobTitle != null && !jobTitle.isEmpty()) {
                pstmt.setString(2, jobTitle);
            } else {
                System.out.println("Job title field is empty");
            }

            pstmt.setString(3, jobDescriptionField.getText());
            pstmt.setInt(4,Integer.parseInt(OpenPositionsFeild.getText()));
            double gpa = Double.parseDouble(gpaField.getText());
            String gender = genderField.getText();
            int experience = Integer.parseInt(experienceField.getText());

            pstmt.setDouble(5, gpa);
            pstmt.setInt(6, experience);
            pstmt.setString(7, gender);
            //System.out.print(jobTitleField.getText());
            System.out.println("Parameters: " + pstmt.toString());
            pstmt.executeUpdate();
            //System.out.println(jobTitleField.getText());

            JOptionPane.showMessageDialog(null, "Job posted successfully!");



            con.commit();
}catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());

        } }

    public void postRecentJobs() throws SQLException {
        try {
            Connection con = DriverManager.getConnection("jdbc:sqlserver://localhost:56839;" + "databaseName=JobApplication;encrypt=true;trustServerCertificate=true;", "mari", "1244");
            // Get the recently posted jobs
            PreparedStatement pstmt2 = con.prepareStatement("SELECT TOP 5 * FROM postJobs ORDER BY JobId DESC");
            ResultSet resultSet = pstmt2.executeQuery();

            // Create a new table for recently posted jobs
            PreparedStatement Pruitt = con.prepareStatement("INSERT INTO recentlyPostedJobs (jobPosterId, JobTitle, JobDescription,OpenPositions,PriorityGPA,PriorityExperience, PriorityGender) VALUES (?, ?, ?, ?,?,?,?)");

            while (resultSet.next()) {
                int jobPosterId = resultSet.getInt("jobPosterId");
                String jobTitle = resultSet.getString("JobTitle");
                String jobDescription = resultSet.getString("JobDescription");
                int openPositions = resultSet.getInt("OpenPositions");
                double gpa = resultSet.getDouble("PriorityGPA");
                int experience = resultSet.getInt("PriorityExperience");
                String gender = resultSet.getString("PriorityGender");
                boolean exists = false;
                PreparedStatement checkExistPstmt = con.prepareStatement("SELECT 1 FROM recentlyPostedJobs WHERE jobPosterId = ? AND JobTitle = ? AND JobDescription = ? AND OpenPositions = ? AND PriorityGPA = ? AND PriorityExperience = ? AND PriorityGender = ?");
                checkExistPstmt.setInt(1, jobPosterId);
                checkExistPstmt.setString(2, jobTitle);
                checkExistPstmt.setString(3, jobDescription);
                checkExistPstmt.setInt(4, openPositions);
                checkExistPstmt.setDouble(5, gpa);
                checkExistPstmt.setInt(6, experience);
                checkExistPstmt.setString(7, gender);
                ResultSet existsResultSet = checkExistPstmt.executeQuery();
                if (existsResultSet.next()) {
                    exists = true;
                }

                if (!exists) {
                    Pruitt.setInt(1, jobPosterId);
                    Pruitt.setString(2, jobTitle);
                    Pruitt.setString(3, jobDescription);
                    Pruitt.setInt(4, openPositions);
                    Pruitt.setDouble(5, gpa);
                    Pruitt.setInt(6, experience);
                    Pruitt.setString(7, gender);
                    Pruitt.executeUpdate();
                }
            }

            // Commit the transaction
            con.commit();
        } catch (SQLException sqlException) {
            JOptionPane.showMessageDialog(null, "Error posting recently posted jobs: " + sqlException.getMessage());
        }
    }


    public int getJobPosterId() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:sqlserver://localhost:56839;" + "databaseName=JobApplication;encrypt=true;trustServerCertificate=true;", "mari", "1244");
        Statement stmt = con.createStatement();
        String query = "SELECT MAX(jobPosterId) FROM JobPoster";
        ResultSet resultSet = stmt.executeQuery(query);
        resultSet.next();
        return resultSet.getInt(1) + 1;
    }
    public void updateRegistration() throws SQLException {
        String fullName = fullNameUpdateField.getText();
        String contactInformation = contactInformationUpdateField.getText();

        try {
            Connection con = DriverManager.getConnection("jdbc:sqlserver://localhost:56839;" + "databaseName=JobApplication;encrypt=true;trustServerCertificate=true;", "mari", "1244");
            PreparedStatement p = con.prepareStatement("SELECT jobPosterId FROM JobPoster WHERE FullName = ?");
            p.setString(1, fullName);
            ResultSet resultSet = p.executeQuery();

            if (resultSet.next()) {
                int jobPosterId = resultSet.getInt("jobPosterId");
                PreparedStatement pstmt = con.prepareStatement("UPDATE JobPoster SET contactInfo = ? WHERE jobPosterId = ?");
                pstmt.setString(1, contactInformation);
                pstmt.setInt(2, jobPosterId);
                pstmt.executeUpdate();
                con.commit();
                JOptionPane.showMessageDialog(null, "Registration updated successfully!");
            } else {
                // Display an error message if the full name does not exist in the database
                JOptionPane.showMessageDialog(null, "Full name does not exist in the database.");
            }

        } catch (SQLException sqlException) {
            JOptionPane.showMessageDialog(null, "Error updating registration: " + sqlException.getMessage());
        }
    }
    public void reviewApplications() {
        String jobTitle = jtfield.getText();
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection("jdbc:sqlserver://localhost:56839;" + "databaseName=JobApplication;encrypt=true;trustServerCertificate=true;", "mari", "1244");
            Statement stmt = con.createStatement();
            // String table2 = "CREATE TABLE ApplicationForm(" +"jobTitle varchar(50),"+ "ApplicantID INT," + "FullName VARCHAR(40)," +"city varchar(50),"+"Address varchar(50),"+"highSchool varchar(50),"+")";
            //stmt.executeUpdate(table2);
            //ystem.out.println("table created successfully");
        }catch(SQLException s) {
            System.out.println(s.getMessage());
        }catch(ClassNotFoundException c){
            throw new RuntimeException(c);

        }


        if (jobTitle != null && !jobTitle.isEmpty()) {
            try {
                PreparedStatement pstmt = con.prepareStatement("SELECT ApplicantID,  FullName, city, Address, highSchool FROM ApplicationForm WHERE JobTitle = ?");
                pstmt.setString(1, jobTitle);
                ResultSet resultSet = pstmt.executeQuery();

                if (resultSet.next()) {
                    int applicantId = resultSet.getInt("ApplicantID");

                    // Create a table model for the applicant's applications
                    DefaultTableModel tableModel = new DefaultTableModel();
                    tableModel.addColumn("Applicant ID");
                    tableModel.addColumn("Full Name");
                    tableModel.addColumn("City");
                    tableModel.addColumn("Address");
                    tableModel.addColumn("High School");

                    // Retrieve all the records for this applicant
                    PreparedStatement retrievePstmt = con.prepareStatement("SELECT * FROM ApplicationForm WHERE ApplicantID = ?");
                    retrievePstmt.setInt(1, applicantId);
                    ResultSet retrieveResultSet = retrievePstmt.executeQuery();

                    while (retrieveResultSet.next()) {
                        tableModel.addRow(new Object[]{applicantId, resultSet.getString("FullName"), resultSet.getString("City"), resultSet.getString("Address"), retrieveResultSet.getString("HighSchool")});
                    }

                    // Create a new JTable with the retrieved data
                    JTable jTable1 = new JTable(tableModel);

                    // Create a new JFrame to display the table
                    JFrame frame = new JFrame();
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.add(new JScrollPane(jTable1));
                    frame.pack();
                    frame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Job title not found in the JobApplications table.");
                }
            } catch (SQLException sqlException) {
                // Handle the exception
                JOptionPane.showMessageDialog(null, sqlException.getMessage());
            }
        }
    }}


