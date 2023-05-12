package GUI.Model;

import BE.Customer;
import BE.Pictures;
import BE.TechDoc;
import BE.User;
import BLL.TechDocManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;

public class TechDocModel {
    private final TechDocManager techDocManager;
    private final ObservableList<TechDoc> techDocList;
    private final ObservableList<Pictures> techPictures;

    public TechDocModel() throws Exception {
        techDocManager = new TechDocManager();
        techDocList = FXCollections.observableArrayList();
        techPictures = FXCollections.observableArrayList();
    }

    public TechDoc createTechDoc(TechDoc techDoc) throws SQLException {
        TechDoc newDoc = techDocManager.createTechDoc(techDoc);
        techDocList.add(newDoc);
        return newDoc;
    }

    public void addTech(TechDoc techDoc, User user) throws SQLException {
        techDocManager.addTech(techDoc, user);
    }

    public void removeTech(TechDoc techDoc, User user) throws SQLException {
        techDocManager.removeTech(techDoc, user);
    }

    public ObservableList<TechDoc> getTechDocs(Customer customer, User user) throws SQLException {
        techDocList.addAll(techDocManager.getTechDocs(customer, user));
        return techDocList;
    }

    public void updateTechDoc(TechDoc techDoc) throws SQLException {
        techDocManager.updateTechDoc(techDoc);
    }

    public Pictures addTechPictures(Pictures pictures, TechDoc techDoc) throws SQLException {
        Pictures newPicture = techDocManager.addTechPictures(pictures, techDoc);
        techPictures.add(newPicture);
        return newPicture;
    }

    public void deletePictures(Pictures pictures) throws SQLException {
        techDocManager.deletePictures(pictures);
        techPictures.remove(pictures);
    }

    public void deleteTechDoc(TechDoc techDoc) throws SQLException {
        techDocManager.deleteTechDoc(techDoc);
    }

    public void updateDrawing(String filePath, TechDoc techDoc) throws SQLException {
        techDocManager.updateDrawing(filePath, techDoc);
    }

    public TechDoc getTechDoc(TechDoc techDoc) throws SQLException {
        return techDocManager.getTechDoc(techDoc);
    }
}
