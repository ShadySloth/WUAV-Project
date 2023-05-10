package GUI.Controller;

import BE.Customer;
import BE.Pictures;
import BE.TechDoc;
import BE.User;
import GUI.Model.TechDocModel;
import GUI.Model.UsersModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TechDocEditorController extends BaseController {
    @FXML
    private Label lblNoPictures;
    @FXML
    private Button btnDeletePicture;
    @FXML
    private Button btnAddPicture;
    @FXML
    private ImageView imageViewTechDoc;
    @FXML
    private Button btnNextPicture;
    @FXML
    private TextArea taExtraInfo;
    @FXML
    private Label lblSaveStatus;
    @FXML
    private TextArea taDeviceInfo;
    @FXML
    private TextArea taSetupDescription;
    @FXML
    private TextField tfTitle;
    @FXML
    private Button btnClose;
    private TechDoc techDoc;
    private Customer customer;
    private Pictures pictures;
    private User user;
    private boolean isEdit;
    private List<Image> imageList = new ArrayList<>();
    private int currentImageIndex = -1;

    @Override
    public void setup() throws Exception {
        super.setTModel(new TechDocModel());
        lblNoPictures.setVisible(true);
    }

    @FXML
    private void handleClose(ActionEvent actionEvent) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/View/CustomerView.fxml"));
        Parent root = loader.load();

        CustomerViewController controller = loader.getController();
        controller.setCustomer(customer);
        controller.setUModel(new UsersModel());
        controller.setUser(user);
        controller.setup();

        Stage currentStage = (Stage) btnClose.getScene().getWindow();
        currentStage.setScene(new Scene(root));
        currentStage.centerOnScreen();
        currentStage.show();
    }

    @FXML
    private void handleSave(ActionEvent actionEvent) throws SQLException {
        if (isEdit) {
            doEditOfDoc();
        } else {
            saveNewDoc();
            addTech(techDoc, user);
        }
        lblSaveStatus.setText("Saved successfully!");
        clearSavedLabelText();
    }

    private void saveNewDoc() throws SQLException {
        TechDoc newDoc = new TechDoc(tfTitle.getText(), customer.getId());
        newDoc.setSetupDescription(taSetupDescription.getText());
        newDoc.setDeviceLoginInfo(taDeviceInfo.getText());
        newDoc.setExtraInfo(taExtraInfo.getText());
        techDoc = getTModel().createTechDoc(newDoc);
    }

    private void doEditOfDoc() throws SQLException {
        techDoc.setSetupDescription(taSetupDescription.getText());
        techDoc.setDeviceLoginInfo(taDeviceInfo.getText());
        techDoc.setExtraInfo(taExtraInfo.getText());
        getTModel().updateTechDoc(techDoc);
    }

    public void setIsEdit(TechDoc techDoc) {
        this.techDoc = techDoc;
        isEdit = true;
        fillFields();
        tfTitle.setDisable(true);
    }

    private void fillFields() {
        taSetupDescription.setText(techDoc.getSetupDescription());
        taDeviceInfo.setText(techDoc.getDeviceLoginInfo());
        tfTitle.setText(techDoc.getSetupName());
        taExtraInfo.setText(techDoc.getExtraInfo());
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private void addTech(TechDoc techDoc, User user) {
        try {
            super.getTModel().addTech(techDoc, user);
        } catch (SQLException e) {
            e.printStackTrace();
            displayError(e);
        }
    }

    private void clearSavedLabelText() {
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> lblSaveStatus.setText(""));
            }
        };
        timer.schedule(task, 5000);
    }

    public void handleAddPicture(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Picture");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Files", "*.png", "*.jpg", "*.jpeg"));
        Stage stage = (Stage) btnAddPicture.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            ImageView imageView = imageViewTechDoc;
            Pictures pictures = new Pictures();
            getTModel().getGetTechPictures(image);
            if (currentImageIndex == -1) {
                currentImageIndex = 0;
                displayCurrentImage();
            }
            imageViewTechDoc.setImage(image);
            imageViewTechDoc.setFitWidth(400);
            imageViewTechDoc.setFitHeight(400);

            lblNoPictures.setVisible(false);
        }
    }
    private void displayCurrentImage() {
        if (currentImageIndex >= 0 && currentImageIndex < imageList.size()) {
            Image currentImage = imageList.get(currentImageIndex);
            imageViewTechDoc.setImage(currentImage);
            imageViewTechDoc.setFitWidth(400);
            imageViewTechDoc.setFitHeight(400);
        }
    }

    public void handleNextPicture(ActionEvent actionEvent) {
        if (imageList.size() > 1) {
            currentImageIndex++;
            if (currentImageIndex >= imageList.size()) {
                currentImageIndex = 0;
            }
            displayCurrentImage();
        }
    }

    public void handleDeletePicture(ActionEvent actionEvent) {
        if (currentImageIndex >= 0 && currentImageIndex < imageList.size()) {
            imageList.remove(currentImageIndex);
            if (imageList.isEmpty()) {
                currentImageIndex = -1;
                imageViewTechDoc.setImage(null);
                lblNoPictures.setVisible(true);
            } else {
                if (currentImageIndex >= imageList.size()) {
                    currentImageIndex = 0;
                }
                displayCurrentImage();
            }
        }
    }
}
