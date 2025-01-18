import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UpdateStatusActionListener implements ActionListener {
    private JTable jTable1;
    private DefaultTableModel tableModel;

    private Connection con;

    public void actionPerformed(ActionEvent e) {
        // Create a dialog to prompt the user for the job title
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection("jdbc:sqlserver://localhost:56839;" + "databaseName=JobApplication;encrypt=true;trustServerCertificate=true;", "mari", "1244");
            Statement stmt = con.createStatement();
        }catch(SQLException s){

        }catch(ClassNotFoundException c){

        }
        String[] options = {"Under Review", "Rejected", "Interview Scheduled Soon"};
        String[] optionsArray = {"Under Review", "Rejected", "Interview will be Scheduled  Soon"};
        String[] optionsText = {"Select a new status"};

        String jobTitle = (String) JOptionPane.showInputDialog(null, "Enter the job title:", "Update Job Status", JOptionPane.QUESTION_MESSAGE);

        if (jobTitle != null) {
            // Check if the job title exists in the JobApplications table
            try {
                PreparedStatement pstmt = con.prepareStatement("SELECT * FROM JobAApplications WHERE JobTitle = ?");
                pstmt.setString(1, jobTitle);
                ResultSet resultSet = pstmt.executeQuery();

                if (resultSet.next()) {
                    int applicantId = resultSet.getInt("ApplicantID");
                    // Create a dialog to prompt the user for the new status
                    Object[] options2 = options;
                    String[] optionsArray2 = optionsArray;
                    String[] optionsText2 = {"Select a new status"};

                    JPanel panel = new JPanel();
                    panel.add(new JLabel("New Status:"));
                    JComboBox<String> comboBox = new JComboBox<>(optionsArray2);
                    panel.add(comboBox);

                    int result = JOptionPane.showOptionDialog(null, panel, "Update Job Status", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, optionsText2, options[0]);

                    if (result == JOptionPane.OK_OPTION) {
                        String newStatus = (String) comboBox.getSelectedItem();

                        PreparedStatement updatePstmt = con.prepareStatement("UPDATE JobAApplications SET Status = ? WHERE ApplicantID = ?");
                        updatePstmt.setString(1, newStatus);
                        updatePstmt.setInt(2, applicantId);
                        updatePstmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Status updated successfully!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Job title not found in the JobApplications table.");
                }
            } catch (SQLException sqlException) {
                // Handle the exception
                JOptionPane.showMessageDialog(null, sqlException.getMessage());
            }
        }
    }
}

