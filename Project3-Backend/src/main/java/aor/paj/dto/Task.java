/**Código Daniel*/

package aor.paj.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.UUID;

@XmlRootElement
public class Task {

    // todos os atributos têm de ser válidos
    // o id é gerado automaticamente e é único
    // o username associado e o ID não podem ser alterados, logo não têm setters
    private LocalDate startDate;
    private LocalDate endDate;
    @NotNull
    private int id;
    @NotNull
    private String title;
    @NotNull

    private String description;
    @NotNull
    private int priority;
    @NotNull
    private int status;

    @NotNull
    private String username;


    public Task() {
    }
    public Task(int id, String t, String d, int p, int s, String u, LocalDate startDate, LocalDate endDate) {
        this.id= id;
        this.title = t;
        this.description= d;
        this.priority = p;
        this.status = s;
        this.username = u;

    }

    public LocalDate getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement
    public int getId() {
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
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    @XmlElement
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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