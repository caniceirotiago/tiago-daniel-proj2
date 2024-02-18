package aor.paj.bean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.*;

import aor.paj.dto.Task;
import aor.paj.dto.TaskUpdate;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.JsonbException;

/**
 * TaskBean is an application-scoped bean that manages task operations, including reading from and writing to a JSON file,
 * 'allTasks.json'. It supports creating, retrieving, updating, and deleting tasks, as well as retrieving all tasks for a
 * specific user. The bean sorts tasks based on priority, start date, and end date for user-specific queries. It utilizes
 * Jsonb for JSON processing, ensuring tasks are persistently stored and managed efficiently. This bean plays a crucial
 * role in task management within the application, providing a centralized point for task data manipulation and retrieval.
 */

@ApplicationScoped
public class TaskBean {
    final String filename = "allTasks.json";

    private ArrayList<Task> tasks;

    public TaskBean() {
        readJsonFile();
    }
    public void readJsonFile(){
        File f = new File(filename);
        if(f.exists()){
            try {
                FileReader filereader = new FileReader(f);
                tasks = JsonbBuilder.create().fromJson(filereader, new
                        ArrayList<Task>() {}.getClass().getGenericSuperclass());
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Json file not found " + e);
            } catch (JsonbException e) {
                throw new RuntimeException("Json file processing error " + e);
            }
        }else{
            tasks = new ArrayList<Task>();
        }
    }

    public void addTask(Task a) {
        int tempId = getLastTaskIdCreated();
        a.setId(tempId);
        tasks.add(a);
        writeIntoJsonFile();
    }

    public int getLastTaskIdCreated(){
        int maxId = 0;
        for(Task t: tasks){
            if(t.getId() > maxId) maxId = t.getId();
        }
        return maxId + 1;
    }

    public Task getTask(int id) {
        for (Task a : tasks) {
            if (a.getId() == id)
                return a;
        }
        return null;
    }

    public ArrayList<Task> getAllTasks() {
        return tasks;
    }

    public ArrayList<Task> getAllTasksByUser(String username) {
        ArrayList<Task> listOfUserTasks = new ArrayList<>();
        for(Task t : tasks){
            if(t.getUsername().equals(username)) listOfUserTasks.add(t);
        }
        return orderTasksByPriorityStartAndEndDate(listOfUserTasks);
    }
    public boolean removeTask(int id) {
        for (Task a : tasks) {
            if (a.getId() == id) {
                tasks.remove(a);
                writeIntoJsonFile();
                return true;
            }
        }
        return false;
    }
    public ArrayList<Task> orderTasksByPriorityStartAndEndDate(ArrayList<Task> tasks) {
        Task temp;
        for (int i = 0; i < tasks.size(); i++) {
            for (int j = i + 1; j < tasks.size(); j++) {
                boolean toChange = false;

                Task taskI = tasks.get(i);
                Task taskJ = tasks.get(j);
                if (taskI.getPriority() < taskJ.getPriority()) {
                    toChange = true;
                }
                else if (taskI.getPriority() == taskJ.getPriority() &&
                        ((taskI.getStartDate() == null && taskJ.getStartDate() != null) ||
                                (taskI.getStartDate() != null && taskJ.getStartDate() != null &&
                                        taskI.getStartDate().isAfter(taskJ.getStartDate())))) {
                    toChange = true;
                }
                else if (taskI.getPriority() == taskJ.getPriority() &&
                        ((taskI.getStartDate() == null && taskJ.getStartDate() == null) ||
                                (taskI.getStartDate() != null && taskJ.getStartDate() != null &&
                                        taskI.getStartDate().isEqual(taskJ.getStartDate()))) &&
                        ((taskI.getEndDate() == null && taskJ.getEndDate() != null) ||
                                (taskI.getEndDate() != null && taskJ.getEndDate() != null &&
                                        taskI.getEndDate().isAfter(taskJ.getEndDate())))) {
                    toChange = true;
                }
                if (toChange) {
                    temp = tasks.get(i);
                    tasks.set(i, tasks.get(j));
                    tasks.set(j, temp);
                }
            }
        }
        return tasks;
    }

    public void updateTask(int id, TaskUpdate taskUpdate) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                // Atualiza os campos da tarefa, exceto o ID
                if (taskUpdate.getTitle() != null && !taskUpdate.getTitle().isEmpty()) {
                    task.setTitle(taskUpdate.getTitle());
                }
                if (taskUpdate.getDescription() != null && !taskUpdate.getDescription().isEmpty()) {
                    task.setDescription(taskUpdate.getDescription());
                }
                if (taskUpdate.getPriority() != null) { // Assume 0 como valor não válido
                    task.setPriority(taskUpdate.getPriority());
                }
                if (taskUpdate.getStatus() != 0) { // Assume 0 como valor não válido
                    task.setStatus(taskUpdate.getStatus());
                }
                if (taskUpdate.getStartDate() != null) {
                    task.setStartDate(taskUpdate.getStartDate());
                }
                if (taskUpdate.getEndDate() != null) {
                    task.setEndDate(taskUpdate.getEndDate());
                }

                writeIntoJsonFile();
                return;
            }
        }
    }

    private void writeIntoJsonFile() {
        Jsonb jsonb = JsonbBuilder.create(new
                JsonbConfig().withFormatting(true));
        try {
            jsonb.toJson(tasks, new FileOutputStream(filename));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}