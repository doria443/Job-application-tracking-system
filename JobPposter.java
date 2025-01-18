import java.sql.SQLException;

public interface JobPposter {

        void registerForm()throws SQLException;
        void postJob()throws SQLException;
        void  postRecentJobs()throws SQLException;
        int getJobPosterId() throws SQLException;
        void updateRegistration() throws SQLException;
        void reviewApplications();

    }


