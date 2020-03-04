package dashboard;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement; 
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import login.LoginScreenController;

public class MainScreenController implements Initializable {
    
    public static String ac = LoginScreenController.acc;
    @FXML
    private Label name;
    @FXML
    private Label body;

    public void setInfo() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con= DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root", "");
            String sql = "SELECT * FROM userdata WHERE AccountNo=?";
            ps = con.prepareStatement(sql);
            
            ps.setString(1, ac);

            rs = ps.executeQuery();
            if(rs.next()) {
                name.setText(rs.getString("Name"));
                  
            }else {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText("Login Error");
                a.setContentText("Incorrect Account Number or PIN");
                a.showAndWait();
            }
           }catch(Exception e) {
             Alert a = new Alert(Alert.AlertType.ERROR);
             a.setTitle("Error");
             a.setHeaderText("Login Error");
             a.setContentText("There is some error. TRY AGAIN!!"+e.getMessage());
             a.showAndWait();
         }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        body.setText("JDI Bank, LLC is an American bank holding company specializing in credit cards,\n auto loans, banking, and savings accounts, headquartered in New York, New York.\n JDI Bank is ranked 11th on the list of largest banks in the United States by assets.");
        setInfo();
    }    
    
}
