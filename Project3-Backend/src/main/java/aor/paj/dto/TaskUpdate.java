package aor.paj.dto;

import java.time.LocalDate;

public class TaskUpdate {
    private String title;
    private String description;
    private Integer priority;
    private Integer status;
    private LocalDate startDate;
    private boolean removeStartDate = false;
    private LocalDate endDate;
    private boolean removeEndDate = false;

    // Getters e setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public boolean isRemoveStartDate() {
        return removeStartDate;
    }

    public void setRemoveStartDate(boolean removeStartDate) {
        this.removeStartDate = removeStartDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isRemoveEndDate() {
        return removeEndDate;
    }

    public void setRemoveEndDate(boolean removeEndDate) {
        this.removeEndDate = removeEndDate;
    }
}
