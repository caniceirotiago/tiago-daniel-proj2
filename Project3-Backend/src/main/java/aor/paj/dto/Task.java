package aor.paj.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.HashMap;
import java.util.UUID;

@XmlRootElement
public class Task {

    // todos os atributos têm de ser válidos
    // o id é gerado automaticamente e é único
    // o username associado e o ID não podem ser alterados, logo não têm setters

    @NotNull
    private String id;
    @NotNull
    private String title;
    @NotNull

    private String description;
    @NotNull
    private int priority;
    @NotNull
    private String status;

    @NotNull
    private String username;




    public Task() {
    }
    public Task(String t, String d, int p, String s, String u) {
        this.id= UUID.randomUUID().toString();
        this.title = t;
        this.description= d;
        this.priority = p;
        this.status = s;
        this.username = u;

    }
    @XmlElement
    public String getId() {
        return id;
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
    @XmlElement
    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }
    @XmlElement
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    @XmlElement
    public String getUsername() {
        return username;
    }
    @Override
    public String toString() {
      return "Task{" +
              "id='" + id + '\'' +
              ", title='" + title + '\'' +
              ", description='" + description + '\'' +
              ", priority=" + priority +
              ", status='" + status + '\'' +
              ", username='" + username + '\'' +
              '}';

}
}