/**Código Daniel*/

package aor.paj.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@XmlRootElement
public class Task {

    // todos os atributos têm de ser válidos
    // o id é gerado automaticamente e é único
    // o username associado e o ID não podem ser alterados, logo não têm setters

    int TODO_COLUMN = 100;
    int DOING_COLUMN = 200;
    int DONE_COLUMN = 300;
    private LocalDate startDate;
    private LocalDate endDate;
    private int id;
    @NotNull
    private String title;
    @NotNull @Size(max=180)
    private String description;
    @NotNull
    private int priority;
    @NotNull
    private int status;

    @NotNull @Size (min = 3, max = 20)
    private String username;


    public Task() {
    }


    public LocalDate getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDate endDate) {
        try{
            this.endDate = endDate;
        }catch (DateTimeParseException e){
            this.endDate = null;
        }
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate) {
        try{
            this.startDate = startDate;
        }catch (DateTimeParseException e){
            this.startDate = null;
        }
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
    public @NotNull int getStatus() {
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