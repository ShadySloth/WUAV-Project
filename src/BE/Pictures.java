package BE;

public class Pictures {
    private int id;
    private String description;
    private String filePath;

    public Pictures(int id, String filePath) {
        this.id = id;
        this.filePath = filePath;
    }

    public Pictures(String filePath) {
        this.filePath = filePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "Pictures{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
