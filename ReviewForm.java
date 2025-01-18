import javax.swing.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class ReviewForm implements ActionListener{



            public void actionPerformed(ActionEvent e) {
                String jobTitle = JOptionPane.showInputDialog(null, "Enter the Job Title", "Job Selection", JOptionPane.QUESTION_MESSAGE);

                if (jobTitle != null) {
                    try {
                        Connection con = DriverManager.getConnection("jdbc:sqlserver://localhost:56839;" + "databaseName=JobApplication;encrypt=true;trustServerCertificate=true;", "mari", "1244");
                        PreparedStatement pstmt1 = con.prepareStatement("SELECT applicantId FROM JobAApplications WHERE JobTitle = ?");
                        pstmt1.setString(1, jobTitle);
                        ResultSet rs1 = pstmt1.executeQuery();

                        if (rs1.next()) {
                            int applicantId = rs1.getInt("applicantId");

                            PreparedStatement pstmt2 = con.prepareStatement("SELECT * FROM AApplicant WHERE ApplicantId = ?");
                            pstmt2.setInt(1, applicantId);
                            ResultSet rs2 = pstmt2.executeQuery();

                            if (rs2.next()) {
                                String seekerName = rs2.getString("FullName");
                                String photoPath = rs2.getString("Photo");
                                String academicCredentials = rs2.getString("AcademicCredentials");
                                String recommendationLetter = rs2.getString("RecommendationLetter");
                                if (seekerName != null && photoPath != null && academicCredentials != null && recommendationLetter != null) {

                                // Display the details in a dialog or a new form or however you want to display them
                                JOptionPane.showMessageDialog(null, "Applicant Details:\n"
                                        + "Name: " + seekerName + "\n"
                                        + "Photo: " + photoPath + "\n"
                                        + "Academic Credentials: " + academicCredentials + "\n"
                                        + "Recommendation Letter: " + recommendationLetter);}
                            } else {
                                JOptionPane.showMessageDialog(null, "No applicant found with the given ID.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "No applicant found for this job.");
                        }
                    } catch (SQLException e1) {
                        System.out.println("Error: " + e1.getMessage());
                    }
                }
            }
        }
        // Add the button to your GUI or do whatever you want with it



