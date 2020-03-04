package dashboard;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import login.Banking;
import login.LoginScreenController;

public class DashboardController implements Initializable {
    
    private double xOffset = 0;
    private double yOffset = 0;
    public static String ac = LoginScreenController.acc;
    @FXML
    private Pane dashboard_main;
    @FXML
    private Text name;
    @FXML
    private FontAwesomeIconView ico;
    @FXML
    private Circle profilepic;
    
    @FXML
    private void closeApp(MouseEvent event) {
        Platform.exit();
        System.exit(0);
    }
    
    @FXML
    private void minimizeApp(MouseEvent event) {
        Stage stage = (Stage) ico.getScene().getWindow();
        stage.setIconified(true);
    }
    
    public void setData() {
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
                InputStream is = rs.getBinaryStream("ProfilePic");
                OutputStream os = new FileOutputStream(new File("pic.jpg"));
                byte[] content = new byte[1024];
                int size =0;
                while((size = is.read(content)) != -1) {
                    os.write(content,0,size);
                }
                os.close();
                is.close();
                Image img = new Image("file:pic.jpg", false);
                profilepic.setFill(new ImagePattern(img));
           
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
    @FXML
    public void click(MouseEvent event) {
         xOffset = event.getSceneX();
         yOffset = event.getSceneY();
    }
    
    @FXML
    public void drag(MouseEvent event) {
        LoginScreenController.stage.setX(event.getSceneX()- xOffset);
        LoginScreenController.stage.setY(event.getSceneY()- yOffset);
     }
    
    @FXML
    public void accountInformation(MouseEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("/accountinfo/AccountInformation.fxml"));
        dashboard_main.getChildren().removeAll();
        dashboard_main.getChildren().addAll(fxml);
    }
    
    @FXML
    public void withdraw(MouseEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("/withdraw/WithdrawAmount.fxml"));
        dashboard_main.getChildren().removeAll();
        dashboard_main.getChildren().addAll(fxml);
    }
   
    @FXML
    public void deposit(MouseEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("/deposit/DepositAmount.fxml"));
        dashboard_main.getChildren().removeAll();
        dashboard_main.getChildren().addAll(fxml);
    }
    @FXML
    public void mainScreen() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        dashboard_main.getChildren().removeAll();
        dashboard_main.getChildren().addAll(fxml);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setData();
        try {
            mainScreen();
        }catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Banking.stage.close();
    }    
 }
