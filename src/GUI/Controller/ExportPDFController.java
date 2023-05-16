package GUI.Controller;

import BE.Device;
import BE.Pictures;
import BE.TechDoc;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.FileChooser;

import javax.swing.text.StyleConstants;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ExportPDFController extends BaseController {

    @FXML
    private CheckBox cbDescription;
    @FXML
    private CheckBox cbDrawing;
    @FXML
    private CheckBox cbPhotos;
    @FXML
    private CheckBox cbDevices;
    @FXML
    private CheckBox cbExtra;
    @FXML
    private Button btnExport;
    @FXML
    private Button btnCancel;
    private TechDoc techDoc;
    private ArrayList<Device> deviceList;

    @Override
    public void setup() throws Exception {
        disableBoxes();
    }

    private void disableBoxes() {
        if (techDoc.getFilePathDiagram().isEmpty() || techDoc.getFilePathDiagram() == "") {
            cbDrawing.setDisable(true);
        }

        if (techDoc.getPictures().isEmpty()) {
            cbPhotos.setDisable(true);
        }

        if (deviceList.isEmpty()) {
            cbDevices.setDisable(true);
        }

        if (techDoc.getSetupDescription().isEmpty()) {
            cbDescription.setSelected(false);
            cbDescription.setDisable(true);
        }

        if (techDoc.getExtraInfo().isEmpty()) {
            cbExtra.setDisable(true);
        }
    }

    @FXML
    private void handleExport(ActionEvent actionEvent) {
        generatePdf();
    }

    @FXML
    private void handleCancel(ActionEvent actionEvent) {
        closeWindow(btnCancel);
    }

    private void generatePdf() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
            fileChooser.setInitialFileName(techDoc.getSetupName());

            File fileToSave = fileChooser.showSaveDialog(btnExport.getScene().getWindow());
            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(fileToSave));
            Document document = new Document(pdfDoc, PageSize.A4);

            PdfFont font = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
            PdfFont bold = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
            PdfFont italic = PdfFontFactory.createFont(StandardFonts.TIMES_ITALIC);
            PdfFont bolditalic = PdfFontFactory.createFont(StandardFonts.TIMES_BOLDITALIC);

            Image logo = new Image(ImageDataFactory.create("resources/Photatoes/logo.png"));
            float logoScaleFactor = 0.20F;
            logo.scaleAbsolute(logo.getImageWidth() * logoScaleFactor, logo.getImageHeight() * logoScaleFactor);
            logo.setHorizontalAlignment(HorizontalAlignment.LEFT);

            document.add(logo);

            Paragraph title = new Paragraph(techDoc.getSetupName());
            title.setFont(bold);
            title.setFontSize(18);
            title.setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = currentTime.format(formatter);
            Paragraph date = new Paragraph(formattedDate);
            date.setFont(font);
            date.setFontSize(15);
            date.setTextAlignment(TextAlignment.RIGHT);
            document.add(date);

            if (cbDescription.isSelected()) {
                Paragraph description = new Paragraph(techDoc.getSetupDescription());
                description.setFont(font);
                description.setFontSize(14);
                document.add(description);
            }

            if (cbDevices.isSelected()) {
                Paragraph devices = new Paragraph();
                devices.add("Device credentials:" + "\n");
                devices.add(formatDevices());
                devices.setFont(font);
                devices.setFontSize(14);
                document.add(devices);
            }

            if (cbDrawing.isSelected() && !techDoc.getFilePathDiagram().isEmpty()) {
                Paragraph text = new Paragraph("Technical drawing");
                text.setFontSize(14);
                text.setFont(font);
                Image drawing = new Image(ImageDataFactory.create(techDoc.getFilePathDiagram()));
                drawing.scaleAbsolute(200, 150);
                drawing.setHorizontalAlignment(HorizontalAlignment.CENTER);
                document.add(drawing);
            }

            if (cbPhotos.isSelected()) {
                ArrayList<Pictures> pictures = (ArrayList<Pictures>) techDoc.getPictures();
                Table pictureContainer = new Table(pictures.size());
                Image picture;
                pictureContainer.setBorder(Border.NO_BORDER);
                for (Pictures p: pictures) {
                    picture = new Image(ImageDataFactory.create(p.getFilePath()));
                    picture.scaleAbsolute(200, 200);
                    pictureContainer.addCell(createImageCell(picture));
                }
                pictureContainer.setHorizontalAlignment(HorizontalAlignment.CENTER);
                document.add(pictureContainer);
            }

            if (cbExtra.isSelected()) {
                Paragraph extraInfo = new Paragraph();
                Paragraph extraTitle = new Paragraph();

                extraTitle.add("Additional Info:");
                extraTitle.setFont(bolditalic);
                extraTitle.setFontSize(15);
                document.add(extraTitle);

                extraInfo.add(techDoc.getExtraInfo());
                extraInfo.setFont(italic);
                extraInfo.setFontSize(14);
                document.add(extraInfo);
            }

            //kunde addresse
            //sted til at skriveunder

            document.close();

        } catch (Exception e) {
            displayError(e);
        }
    }

    private static Cell createImageCell(Image image) {
        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell.add(image.setAutoScale(true));
        return cell;
    }

    private String formatDevices() {
        StringBuilder result = new StringBuilder();

        for (Device d: deviceList) {
            result.append("Name: ").append(d.getDevice())
                    .append(", username: ").append(d.getUsername())
                    .append(", password: ").append(d.getPassword())
                    .append("\n");
        }


        return result.toString();
    }

    public void setDeviceList(ArrayList<Device> deviceList) {
        this.deviceList = deviceList;
    }

    public void setTechDoc(TechDoc techDoc) {
        this.techDoc = techDoc;
    }
}
