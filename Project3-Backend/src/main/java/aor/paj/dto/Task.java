package aor.paj.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Task is a core entity representing a task in the system, including details such as title, description, priority,
 * status, and associated username. It also includes start and end dates, with logic to handle date parsing exceptions.
 * The class is designed with data validation annotations to ensure that essential fields are not null. The username and
 * ID are immutable post-creation, reflecting task ownership and identity.
 */

@XmlRootElement
public class Task {

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