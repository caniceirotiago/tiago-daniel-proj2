package aor.paj.dto;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class Task {
    int id;
    String title;
    String description;
    public Task() {
    }
    public Task(int i, String t, String d) {
        this.id=i;
        this.title = t;
        this.description= d;
    }
    @XmlElement
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    @XmlElement
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    @XmlElement
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}