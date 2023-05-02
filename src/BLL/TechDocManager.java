package BLL;

import BE.Customer;
import BE.TechDoc;
import BE.User;
import DAL.FacadeDAL;

import java.sql.SQLException;
import java.util.List;

public class TechDocManager {
    private final FacadeDAL facadeDAL;

    public TechDocManager() throws Exception {
        facadeDAL = new FacadeDAL();
    }

    public TechDoc createTechDoc(TechDoc techDoc) throws SQLException {
        return facadeDAL.createTechDoc(techDoc);
    }

    public void addTech(TechDoc techDoc, User user) throws SQLException {
        facadeDAL.addTech(techDoc, user);
    }
    public List<TechDoc> getTechDocs(Customer customer) throws SQLException {
        return facadeDAL.getTechDocs(customer);
    }
}
