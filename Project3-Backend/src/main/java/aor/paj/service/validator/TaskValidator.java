package aor.paj.service.validator;

import aor.paj.dto.Task;

import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;

@ApplicationScoped
public class TaskValidator {
    public TaskValidator() {
    }

    public boolean validateTaskTitle(String taskName) {
        return taskName != null && taskName.length() >= 3 && taskName.length() <= 50;
    }

    public boolean validateTaskDescription(String taskDescription) {
        return taskDescription != null && taskDescription.length() >= 3 && taskDescription.length() <= 500;
    }
    public boolean validateTaskPriority(int taskPriority) {
        return taskPriority >= 0 && taskPriority <= 400;
    }
    public boolean validateTaskStatus(int taskStatus) {
        return  taskStatus == 100 || taskStatus == 200 || taskStatus == 300;
    }
    // If you want to add more statues tha validation should be updated
    public boolean isStartDateAfterEndDate(Task task) {
        if(task.getStartDate() == null || task.getEndDate() == null) return true;
        return task.getStartDate().isBefore(task.getEndDate());
    }
    public boolean validateTask(Task task) {
        return validateTaskTitle(task.getTitle()) &&
                validateTaskDescription(task.getDescription()) &&
                validateTaskPriority(task.getPriority()) &&
                validateTaskStatus(task.getStatus()) &&
                isStartDateAfterEndDate(task);
    }

}
