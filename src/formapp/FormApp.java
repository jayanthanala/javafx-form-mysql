// 19BCE2048 - Jayanth Anala
package formapp;
import javafx.scene.image.Image;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.Window;

class dbmscon{

    public Connection getConnection() throws Exception{
        Connection con;
        Class.forName("com.mysql.cj.jdbc.Driver"); 
        con=DriverManager.getConnection("jdbc:mysql://localhost:3306/eventdb", "root", "19bce2048");
        //System.out.println("Connection Established Successfully");
        return con;
    }
    public void closeConnection(Connection con,Statement stmt) throws SQLException{
        stmt.close();
        con.close();
        //System.out.println("The Connection with MySQL is closed");
    }
}

class Visitor{
    private final String name;
    private final String email;
    private final String role;
    private final String seats;
    private final long phone;

    public Visitor(String name,String email,String role,String seats,long phone){   
        this.name=name;
        this.email=email;
        this.role=role;
        this.seats=seats;
        this.phone=phone;
    }
    
    public String getInfo(){
        return name+" - "+phone+" - "+ role+ " - "+seats+" - "+email;
    }
    
    static int getCount(String g){
        try{
            dbmscon dbmsconnect=new dbmscon();
            Connection conn=dbmsconnect.getConnection();
            String query = "select count(*) from Visitors WHERE role=?";
            PreparedStatement stmt1=conn.prepareStatement(query);
            stmt1.setString(1,g);
            ResultSet rs = stmt1.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            dbmsconnect.closeConnection(conn,stmt1);
            return count;
        }catch(Exception e){
            System.out.println("Failed to create JDBC db connection: "+e.getMessage());
            return -1;
        }
        
    }
    
    public boolean insertRecord()
    {
        try{
            
            dbmsconnection dbmsconnect=new dbmsconnection();
            Connection con=dbmsconnect.getConnection();
            String sql="insert into Visitors (name,email,role,seat,phone) values(?,?,?,?,?);";
            PreparedStatement stmt=con.prepareStatement(sql);
            stmt.setString(1,name);
            stmt.setString(2,email);
            stmt.setString(3,role);
            stmt.setString(4,seats);
            stmt.setLong(5,phone);
            int i=stmt.executeUpdate();
            dbmsconnect.closeConnection(con,stmt);
            return i>0;
            
        }catch(Exception e){
            System.out.println("Failed to create JDBC db connection: "+e.getMessage());
            return false;
        }   
    }
}

public class FormApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("19BCE2048 - Event Registration");
        GridPane gridPane = createRegistrationFormPane();
        addUIControls(gridPane);
        Scene scene = new Scene(gridPane, 800, 600);	
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createRegistrationFormPane() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(40, 40, 40, 40));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200,200, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);
        return gridPane;
    }

    private void addUIControls(GridPane gridPane) {
        Image image = new Image("https://upload.wikimedia.org/wikipedia/en/thumb/c/c5/Vellore_Institute_of_Technology_seal_2017.svg/1200px-Vellore_Institute_of_Technology_seal_2017.svg.png");
        //Image image = new Image("/Users/j/Desktop/seat.png");
        ImageView pic = new ImageView(image);
        pic.setFitWidth(70);
        pic.setFitHeight(70);
        gridPane.add(pic, 1, 0);
        
        Label headerLabel = new Label("Event Registration Form");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

        // Name 
        Label nameLabel = new Label("Full Name ");
        gridPane.add(nameLabel, 0,1);

        TextField nameField = new TextField();
        nameField.setPrefHeight(40);
        gridPane.add(nameField, 1,1);

        //Phone 
        Label phoneLabel = new Label("Phone ");
        gridPane.add(phoneLabel, 0, 2);

        TextField phoneField = new TextField();
        phoneField.setPrefHeight(40);
        gridPane.add(phoneField, 1, 2);
        
        phoneField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                phoneField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        
        //Email
        Label emailLabel = new Label("Email ID ");
        gridPane.add(emailLabel, 0, 3);

        TextField emailField = new TextField();
        emailField.setPrefHeight(40);
        gridPane.add(emailField, 1, 3);
        
        // Role 
        Label roleLabel = new Label("Role/Gender ");
        gridPane.add(roleLabel, 0, 4);

        ToggleGroup roleField = new ToggleGroup();
        RadioButton rb1 = new RadioButton("Male");
        rb1.setToggleGroup(roleField);
        RadioButton rb2 = new RadioButton("Female");
        rb2.setToggleGroup(roleField);
        RadioButton rb3 = new RadioButton("Child");
        rb3.setToggleGroup(roleField);
        HBox box = new HBox(40, rb1,rb2,rb3);
        gridPane.add(box, 1, 4);
        
        // Seat 
        Label seatLabel = new Label("Seat No ");
        gridPane.add(seatLabel, 0, 5);
 
        ObservableList<String> seatOptions = FXCollections.observableArrayList(
                "A1",
                "B1",
                "C1",
                "D1",
                "E1",
                "F1"
            );
        ComboBox seatField = new ComboBox(seatOptions);
        seatField.setPrefHeight(40);
        gridPane.add(seatField, 1, 5);
        seatField.setValue("Choose a Seat");
        

        // Submit Button
        Button submitButton = new Button("Register");
        submitButton.setPrefHeight(40);
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(100);
        gridPane.add(submitButton, 0, 6, 2, 1);
        GridPane.setHalignment(submitButton, HPos.CENTER);
        GridPane.setMargin(submitButton, new Insets(20, 0,20,0));
        
        // Count Button
        Button countButton = new Button("Gender Count");
        countButton.setPrefHeight(40);
        countButton.setDefaultButton(true);
        countButton.setPrefWidth(100);
        gridPane.add(countButton, 0, 7, 2, 1);
        GridPane.setHalignment(countButton, HPos.CENTER);
        GridPane.setMargin(countButton, new Insets(20, 0,20,0));

        submitButton.setOnAction((ActionEvent event) -> {
            if(nameField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter your name");
                return;
            }
            if(emailField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter your email");
                return;
            }
            if(!emailField.getText().matches("^(.+)@(.+)$")){
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter your email properly");
                return;
            }
            if(phoneField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter your phone number");
                return;
            }
            if(rb1.isSelected()==false && rb2.isSelected()==false && rb3.isSelected()==false) {
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please select a role from the group");
                return;
            }
            if("Choose a Seat".equals((String) seatField.getValue())){
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please select a Seat from the list");
                return;
            }
            
            RadioButton selectedRadioButton = (RadioButton) roleField.getSelectedToggle();
            String toogleGroupValue = selectedRadioButton.getText();
            
            //System.out.print(emailField.getText().matches("^(.+)@(.+)$"));
            Visitor s=new Visitor(nameField.getText(),emailField.getText(),toogleGroupValue, (String) seatField.getValue(),Long.parseLong(phoneField.getText()));
            System.out.println(s.getInfo());
            seatOptions.remove((String) seatField.getValue());
            boolean result;
            
            result = s.insertRecord();
            if(result){
                showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), "Registration Successful!", "Welcome "+nameField.getText());
                nameField.clear();
                emailField.clear();
                phoneField.clear();
                //seatField.setValue("Choose a Seat");
                selectedRadioButton.setSelected(false);
            }else{
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please Try Again, Something went wrong!"); 
            }
        });
        
        countButton.setOnAction((ActionEvent event) -> {
            int male = Visitor.getCount("Male");
            int female = Visitor.getCount("Female");
            int child = Visitor.getCount("Child");
            int total = male+female+child;
            showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), "Total Counts",
                    "Total: "+total+" - "+"Male: "+male+" - "+"Female: "+female+" - "+"Children: "+child);
        });
    }

    private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
