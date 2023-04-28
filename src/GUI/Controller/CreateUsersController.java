package GUI.Controller;

import BE.User;
import BLL.BCrypt;
import GUI.Model.UsersModel;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class CreateUsersController extends BaseController{
    public TextField txtNameUser;
    public TextField txtUsernameUser;
    public TextField txtConfirmPwUser;
    public TextField txtPasswordUser;
    public Button btnSaveUser;
    public Button deleteUser;
    public Button cancel;
    public TableView<User> userList;
    public RadioButton techChecker;
    public RadioButton salesChecker;
    public RadioButton managerChecker;
    public TableColumn userscln;
    private boolean txtInUsernameField;
    private boolean txtInNameField;
    private boolean txtInPasswordField;
    private boolean txtInConfirmField;
    private boolean checkerSalesField;
    private boolean checkerManagerField;
    private boolean checkerTechField;


    @Override
    public void setup() throws Exception {
        super.setUModel(new UsersModel());
        insertIntoTable();
       // btnSaveUser.setDisable(true);
        beGoneButton();
        enableTheButtons();
    }

    private void beGoneButton(){
        txtUsernameUser.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtInUsernameField = true;
            } else {
                txtInUsernameField = false;
            }
        });
        txtNameUser.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtInNameField = true;
            } else {
                txtInNameField = false;
            }
        });
        txtPasswordUser.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtInPasswordField = true;
            } else {
                txtInPasswordField = false;
            }
        });
        txtConfirmPwUser.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtInConfirmField = true;
            } else {
                txtInConfirmField = false;
            }
        });

        techChecker.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                checkerTechField = true;
            } else {
                checkerTechField = false;
            }
        });
        managerChecker.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                checkerManagerField = true;
            } else {
                checkerManagerField = false;
            }
        });
        salesChecker.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                checkerSalesField = true;
            } else {
                checkerSalesField = false;
            }
        });

    }

    private void enableTheButtons() {
        if (checkerSalesField && checkerManagerField && checkerTechField && txtInNameField && txtInConfirmField && txtInPasswordField && txtInUsernameField) {
            btnSaveUser.setDisable(false);
        }
    }

    public void handleSaveUser(ActionEvent actionEvent) {
        String username = txtUsernameUser.getText();
        String name = txtNameUser.getText();
        String password = txtPasswordUser.getText();
        String confirmPassword = txtConfirmPwUser.getText();

        int userType = -1;
        if(techChecker.isSelected()) {
            userType = 2;
        } else if (managerChecker.isSelected()) {
            userType = 1;
        } else if (salesChecker.isSelected()) {
            userType = 3;
        }
        
        String salt = BCrypt.gensalt(10);
        String hashedPassword1 = BCrypt.hashpw(password, salt);
        String hashedPassword2 = BCrypt.hashpw(confirmPassword, salt);

        try {
            if (hashedPassword1.equals(hashedPassword2)) {
                User u = new User(username, hashedPassword1, name,userType);
                super.getUModel().createUser(u);
                //closeWindow(saveUser);
            }
        } catch (Exception e) {
            displayError(e);
            e.printStackTrace();
        }
    }

    public void handleDeleteUser(ActionEvent actionEvent) {
        try {
            User user = userList.getSelectionModel().getSelectedItem();
            super.getUModel().deleteUser(user);
        } catch (Exception e) {
            displayError(e);
        }
    }

    public void handleCancelWindow(ActionEvent actionEvent) {
        closeWindow(cancel);
    }

    private void insertIntoTable(){
        userscln.setCellValueFactory(new PropertyValueFactory<>("Users"));
        userList.getColumns().addAll();
        userList.setItems(super.getUModel().getObservableUsers());
    }

}
