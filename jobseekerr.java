import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Files;
import java.sql.*;

public class jobseekerr implements JobSseeker{
    private JFrame jobSeekerFrame;
    private JTabbedPane tabbedPane;
    private JTextField fullNameField;
    private JTextField ageField;
    private JTextField contactInformationField;
    private JComboBox<String> levelOfQualificationComboBox;
    private JComboBox<String> areaOfSpecializationComboBox;
    private JTextField jobTitleField;
    private JButton submitButton;
    private JTable jobTable;
    private DefaultTableModel jobTableModel;
    private Connection con;
    private JTable jTable1;
    private byte[] credentialsBytes;
    private byte[] recommendationLetterBytes;
    private byte[] photoBytes;


    public jobseekerr() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection("jdbc:sqlserver://localhost:56839;" +
                    "databaseName=JobApplication;encrypt=true;trustServerCertificate=true;", "mari", "1244");
            Statement stmt = con.createStatement();
            String table1 = "CREATE TABLE  AApplicant (" +
                    "ApplicantID INT PRIMARY KEY IDENTITY(1,1), " +
                    "FullName VARCHAR(40), Age INT, ContactInfo VARCHAR(10), " +
                    "LevelOfQualification VARCHAR(60), AreaOfSpecialization VARCHAR(100), " +
                    "AcademicCredentials VARBINARY(MAX), RecommendationLetter VARBINARY(MAX), Photo VARBINARY(MAX))";
            stmt.executeUpdate(table1);
            System.out.println("Table created successfully or already exists.");
        }  catch (SQLException sqlException) {
            System.out.print(sqlException.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.print(e.getMessage());
        }



        jobSeekerFrame = new JFrame("Job Seeker");
        jobSeekerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tabbedPane = new JTabbedPane();

        // Register Tab
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new GridLayout(9, 2));

        JLabel fullNameLabel = new JLabel("Full Name:");
        fullNameField = new JTextField();
        registerPanel.add(fullNameLabel);
        registerPanel.add(fullNameField);

        JLabel ageLabel = new JLabel("Age:");
        ageField = new JTextField();
        registerPanel.add(ageLabel);
        registerPanel.add(ageField);

        JLabel contactInformationLabel = new JLabel("Contact Information:");
        contactInformationField = new JTextField();
        registerPanel.add(contactInformationLabel);
        registerPanel.add(contactInformationField);

        JLabel levelOfQualificationLabel = new JLabel("Level of Qualification:");
        String[] levelOfQualificationOptions = {"Bachelor's", "Master's", "Ph.D."};
        levelOfQualificationComboBox = new JComboBox<>(levelOfQualificationOptions);
        registerPanel.add(levelOfQualificationLabel);
        registerPanel.add(levelOfQualificationComboBox);

        JLabel areaOfSpecializationLabel = new JLabel("Area of Specialization:");
        String[] areaOfSpecializationOptions = {"Computer Science", "Engineering", "Business","accountants","software engineer","civil engineer","nurse"};
        areaOfSpecializationComboBox = new JComboBox<>(areaOfSpecializationOptions);
        registerPanel.add(areaOfSpecializationLabel);
        registerPanel.add(areaOfSpecializationComboBox);

        JLabel academicCredentialsLabel = new JLabel("Academic Credentials:");

        JButton academicCredentialsButton = new JButton("Upload File");
        academicCredentialsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                credentialsBytes = selectFileAndGetBytes();
            }
        });
        registerPanel.add(academicCredentialsLabel);
        registerPanel.add(credentialsFieldPanel(academicCredentialsButton));

        JLabel recommendationLetterLabel = new JLabel("Recommendation Letter:");
        JButton recommendationLetterButton = new JButton("Upload File");
        recommendationLetterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recommendationLetterBytes = selectFileAndGetBytes();
            }
        });
        registerPanel.add(recommendationLetterLabel);
        registerPanel.add(recommendationFieldPanel(recommendationLetterButton));

        JLabel photoLabel = new JLabel("Photo:");
        JButton photoButton = new JButton("Upload File");
        photoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                photoBytes = selectFileAndGetBytes();
            }
        });
        registerPanel.add(photoLabel);
        registerPanel.add(photoFieldPanel(photoButton));

        submitButton = new JButton("Register");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                submitForm();
            }
        });
        registerPanel.add(submitButton);

        tabbedPane.addTab("Register", registerPanel);

        // Search Jobs Tab
        JPanel searchJobsPanel = new JPanel();
        searchJobsPanel.setLayout(new BorderLayout());



        JButton searchButton = new JButton("Search Jobs");
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                searchJobs();

            }
        });
        jobTableModel = new DefaultTableModel();
        jobTableModel.addColumn("Job Title");
        jobTableModel.addColumn("Job Description");
        jobTableModel.addColumn("Open Positions");


        jobTable = new JTable(jobTableModel);
        JScrollPane tableScrollPane = new JScrollPane(jobTable);
        searchJobsPanel.add(searchButton, BorderLayout.NORTH);
        searchJobsPanel.add(tableScrollPane, BorderLayout.CENTER);
        jTable1 = new JTable(jobTableModel);
        JScrollPane scrollPane = new JScrollPane(jTable1);
        searchJobsPanel .add(scrollPane);

        tabbedPane.addTab("Search Jobs", searchJobsPanel);

        // Update Profile Tab
        JPanel updateProfilePanel = new JPanel();
        updateProfilePanel.setLayout(new GridLayout(9, 2));

        JLabel updateFullNameLabel = new JLabel("Full Name:");
        JTextField updateFullNameField = new JTextField();
        updateProfilePanel.add(updateFullNameLabel);
        updateProfilePanel.add(updateFullNameField);

        JLabel updateAgeLabel = new JLabel("Age:");
        JTextField updateAgeField = new JTextField();
        updateProfilePanel.add(updateAgeLabel);
        updateProfilePanel.add(updateAgeField);

        JLabel updateContactInformationLabel = new JLabel("Contact Information:");
        JTextField updateContactInformationField = new JTextField();
        updateProfilePanel.add(updateContactInformationLabel);
        updateProfilePanel.add(updateContactInformationField);

        JLabel updateLevelOfQualificationLabel = new JLabel("Level of Qualification:");
        JComboBox<String> updateLevelOfQualificationComboBox = new JComboBox<>(levelOfQualificationOptions);
        updateProfilePanel.add(updateLevelOfQualificationLabel);
        updateProfilePanel.add(updateLevelOfQualificationComboBox);

        JLabel updateAreaOfSpecializationLabel = new JLabel("Area of Specialization:");
        JComboBox<String> updateAreaOfSpecializationComboBox = new JComboBox<>(areaOfSpecializationOptions);
        updateProfilePanel.add(updateAreaOfSpecializationLabel);
        updateProfilePanel.add(updateAreaOfSpecializationComboBox);

        JButton updateButton = new JButton("Update Profile");
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateProfile(updateFullNameField, updateAgeField, updateContactInformationField, updateLevelOfQualificationComboBox, updateAreaOfSpecializationComboBox);
            }
        });
        updateProfilePanel.add(updateButton);

        tabbedPane.addTab("Update Profile", updateProfilePanel);
        JPanel jobSeekerPanel = new JPanel();
        jobSeekerPanel.add(new JLabel("Enter Job Title:"));
        jobTitleField = new JTextField(10);
        jobSeekerPanel.add(jobTitleField);
        JButton checkStatusButton = new JButton("Check Status");
        checkStatusButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkStatus();
            }
        });
        jobSeekerPanel.add(checkStatusButton);
        tabbedPane.add("check status",jobSeekerPanel);

        jobSeekerFrame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
        jobSeekerFrame.pack();
        jobSeekerFrame.setVisible(true);
    }

    private byte[] selectFileAndGetBytes() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = fileChooser.getSelectedFile();
                selectedFile.getAbsolutePath();
                return Files.readAllBytes(selectedFile.toPath());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error reading file: " + ex.getMessage());
            }
        }
        return null;
    }

    public void submitForm() {
        try {
            // Get the values from the form
            String fullName = fullNameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String contactInfo = contactInformationField.getText();
            String levelOfQualification = (String) levelOfQualificationComboBox.getSelectedItem();
            String areaOfSpecialization = (String) areaOfSpecializationComboBox.getSelectedItem();

            // Prepare SQL statement
            PreparedStatement stmt = con.prepareStatement( "INSERT INTO AApplicant (FullName, Age, ContactInfo, LevelOfQualification, AreaOfSpecialization, AcademicCredentials, RecommendationLetter, Photo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

            stmt.setString(1, fullName);
            stmt.setInt(2, age);
            stmt.setString(3, contactInfo);
            stmt.setString(4, levelOfQualification);
            stmt.setString(5, areaOfSpecialization);
            stmt.setBytes(6, credentialsBytes);
            stmt.setBytes(7, recommendationLetterBytes);
            stmt.setBytes(8, photoBytes);

            // Execute the statement
            stmt.executeUpdate();
            con.commit();

            // Display a success message
            JOptionPane.showMessageDialog(jobSeekerFrame, "Form submitted successfully!");
        } catch (SQLException ex) {
            // Handle SQL errors
            JOptionPane.showMessageDialog(jobSeekerFrame, "Error submitting form: " + ex.getMessage());
        }
    }

    public void searchJobs() {
        try {
            Object[] options = {"Recently Posted Jobs", "All Jobs"};
            String choice = (String) JOptionPane.showInputDialog(null, "Choose the type of jobs to search:", "Job Search", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            if (choice.equals("Recently Posted Jobs")) {
                Statement stmt = con.createStatement();
                String query = "SELECT JobTitle, JobDescription, OpenPositions FROM recentlyPostedJobs";
                ResultSet resultSet = stmt.executeQuery(query);

                jobTableModel.setRowCount(0);


                while (resultSet.next()) {
                    String jobTitle = resultSet.getString("JobTitle");
                    String jobDescription = resultSet.getString("JobDescription");
                    int openPositions = resultSet.getInt("OpenPositions");

                    Object[] rowData = {jobTitle, jobDescription, openPositions};
                    jobTableModel.addRow(rowData);
                }

                resultSet.close();
                stmt.close();
            } else if (choice.equals("All Jobs")) {
                Statement stmt = con.createStatement();
                String query = "SELECT JobTitle, JobDescription, OpenPositions FROM postJobs";
                ResultSet resultSet = stmt.executeQuery(query);
                jobTableModel.setRowCount(0);
                while (resultSet.next()) {
                    String jobTitle = resultSet.getString("JobTitle");
                    String jobDescription = resultSet.getString("JobDescription");
                    int openPositions = resultSet.getInt("OpenPositions");

                    Object[] rowData = {jobTitle, jobDescription, openPositions};
                    jobTableModel.addRow(rowData);
                }
            }

            jTable1.addMouseListener(new MouseAdapter() {
                public void mouseReleased(MouseEvent e) {
                    try {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            int row = jTable1.getSelectedRow();
                            if (row != -1) {
                                String jobTitle = (String) jobTableModel.getValueAt(row, 0);
                                String jobDescription = (String) jobTableModel.getValueAt(row, 1);
                                int openPositions = (int) jobTableModel.getValueAt(row, 2);

                                // Prompt the user to fill in GPA, experience year, and gender
                                double gpa = Double.parseDouble(JOptionPane.showInputDialog(null, "Enter GPA:"));
                                int experienceYear = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter experience year:"));
                                String gender = (String) JOptionPane.showInputDialog(null, "Enter gender:", "Gender", JOptionPane.QUESTION_MESSAGE);


                                PreparedStatement Ps;
                                double requiredGpa;
                                int requiredExperienceYear;
                                if (choice.equals("Recently Posted Jobs")) {
                                    Ps = con.prepareStatement("SELECT PriorityGPA, PriorityExperience FROM recentlyPostedJobs WHERE JobTitle = ?");
                                    Ps.setString(1, jobTitle);
                                    ResultSet PsResultSet = Ps.executeQuery();
                                    PsResultSet.next();
                                    requiredGpa = PsResultSet.getDouble("PriorityGPA");
                                    requiredExperienceYear = PsResultSet.getInt("PriorityExperience");

                                } else {
                                    Ps = con.prepareStatement("SELECT GPA, ExperienceYear FROM postJobs WHERE JobTitle = ?");
                                    Ps.setString(1, jobTitle);
                                    ResultSet PruittResultSet = Ps.executeQuery();
                                    PruittResultSet.next();
                                    requiredGpa = PruittResultSet.getDouble("GPA");
                                    requiredExperienceYear = PruittResultSet.getInt("ExperienceYear");
                                }


                                if (gpa >= requiredGpa && experienceYear >= requiredExperienceYear) {
                                    ApplicationForm a=new ApplicationForm(jobTitle);
                                    Statement stmt = con.createStatement();
                                    //  String query = "CREATE TABLE JobAApplications ("
                                    //   + "JobApplicationID INT PRIMARY KEY IDENTITY(1,1),"
                                    //   + "ApplicantID INT,"
                                    //  + "JobTitle VARCHAR(50),"
                                    //  + "Status VARCHAR(20));";

                                    // stmt.executeUpdate(query);
                                    PreparedStatement pstmt = con.prepareStatement("INSERT INTO JobAApplications (ApplicantID, JobTitle, Status) VALUES (?, ?, 'Under Review')");
                                    int applicantId = getUniqueApplicantId();
                                    pstmt.setInt(1, applicantId);
                                    pstmt.setString(2, jobTitle);
                                    pstmt.executeUpdate();
                                    System.out.println("You qualify for the job!");
                                } else {

                                    JOptionPane.showMessageDialog(null, "You do not qualify for this job.");
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Please select a job first.");
                            }
                        }
                    } catch (SQLException s) {
                        JOptionPane.showMessageDialog(null, "Error searching for jobs: " + s.getMessage());
                    }
                }
            });
        } catch(NullPointerException n){
            JOptionPane.showMessageDialog(null, "Error searching for jobs: " + n.getMessage());
        } catch (SQLException sqlException) {
            JOptionPane.showMessageDialog(null, "Error searching for jobs: " + sqlException.getMessage());
        }
    }

    public int getUniqueApplicantId() {
        // You can use a counter to keep track of the number of applicants
        int counter = 0;
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM ApplicationForm");
            rs.next();
            counter = rs.getInt(1);
        } catch (SQLException e) {
            // Handle the exception
        }
        return ++counter;
    }
    private void updateProfile(JTextField fullNameField, JTextField ageField, JTextField contactInfoField, JComboBox<String> qualificationComboBox, JComboBox<String> specializationComboBox) {
        try {
            // Get the updated values from the form
            String updatedFullName = fullNameField.getText();
            int updatedAge = Integer.parseInt(ageField.getText());
            String updatedContactInfo = contactInfoField.getText();
            String updatedLevelOfQualification = (String) qualificationComboBox.getSelectedItem();
            String updatedAreaOfSpecialization = (String) specializationComboBox.getSelectedItem();

            // Create a prepared statement to select the existing record
            PreparedStatement pstmt = con.prepareStatement("SELECT ApplicantID FROM AApplicant WHERE FullName=?");
            pstmt.setString(1, updatedFullName);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                int applicantId = result.getInt("ApplicantID");


                PreparedStatement stmt = con.prepareStatement("UPDATE AApplicant SET Age=?, ContactInfo=?, LevelOfQualification=?, AreaOfSpecialization=? WHERE ApplicantID=?");
                stmt.setInt(1, updatedAge);
                stmt.setString(2, updatedContactInfo);
                stmt.setString(3, updatedLevelOfQualification);
                stmt.setString(4, updatedAreaOfSpecialization);
                stmt.setInt(5, applicantId);
                stmt.executeUpdate();


                JOptionPane.showMessageDialog(jobSeekerFrame, "Profile updated successfully!");
            } else {

                JOptionPane.showMessageDialog(jobSeekerFrame, "Full name does not exist in the database.");
            }


            result.close();
            pstmt.close();
        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(jobSeekerFrame, "Error updating profile: " + ex.getMessage());
        }
    }

    private JPanel credentialsFieldPanel(JButton button) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(button, BorderLayout.NORTH);

        return panel;
    }

    private JPanel recommendationFieldPanel(JButton button) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(button, BorderLayout.NORTH);

        return panel;
    }

    private JPanel photoFieldPanel(JButton button) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(button, BorderLayout.NORTH);

        return panel;
    }
    public void checkStatus() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection("jdbc:sqlserver://localhost:56839;" + "databaseName=JobApplication;encrypt=true;trustServerCertificate=true;", "mari", "1244");
            Statement stmt = con.createStatement();
        }catch(SQLException s){

        }catch(ClassNotFoundException c){

        }
        String jobTitle = jobTitleField.getText();

        if (jobTitle != null && !jobTitle.isEmpty()) {
            try {
                PreparedStatement pstmt = con.prepareStatement("SELECT Status FROM JobAApplications WHERE JobTitle = ? ");
                pstmt.setString(1, jobTitle);
                ResultSet resultSet=pstmt.executeQuery();
                //int applicantId= Integer.parseInt(resultSet.getString("ApplicantID"));
                // pstmt.setInt(2, applicantId);
                //resultSet = pstmt.executeQuery();

                if (resultSet.next()) {
                    String status = resultSet.getString("Status");
                    JOptionPane.showMessageDialog(null, "The status of the application is: " + status);
                } else {
                    JOptionPane.showMessageDialog(null, "Job title or applicant ID not found in the JobAApplications table.");
                }
            } catch (SQLException sqlException) {

                JOptionPane.showMessageDialog(null, sqlException.getMessage());
            }
        }
    }
}
