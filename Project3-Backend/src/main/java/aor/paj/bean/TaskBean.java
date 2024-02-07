package aor.paj.bean;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;

import aor.paj.dto.Task;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
@ApplicationScoped
public class TaskBean {
    final String filename = "activities.json";
    private ArrayList<Task> tasks;
    public TaskBean() {
        File f = new File(filename);
        if(f.exists()){
            try {
                FileReader filereader = new FileReader(f);
                tasks = JsonbBuilder.create().fromJson(filereader, new
                        ArrayList<Task>() {}.getClass().getGenericSuperclass());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }else
            tasks = new ArrayList<Task>();
    }
    public void addActivity(Task a) {
        tasks.add(a);
        writeIntoJsonFile();
    }
    public Task getActivity(int i) {
        for (Task a : tasks) {
            if (a.getId() == i)
                return a;
        }
        return null;
    }
    public ArrayList<Task> getActivities() {
        return tasks;
    }
    public boolean remoreActivity(int id) {
        for (Task a : tasks) {
            if (a.getId() == id) {
                tasks.remove(a);
                return true;
            }
        }
        return false;
    }
    public boolean updateActivity(int id, Task activity) {
        for (Task a : tasks) {
            if (a.getId() == id) {
                a.setTitle(activity.getTitle());
                a.setDescription(activity.getDescription());
                writeIntoJsonFile();
                return true;
            }
        }
        return false;
    }
    private void writeIntoJsonFile(){
        Jsonb jsonb = JsonbBuilder.create(new
                JsonbConfig().withFormatting(true));
        try {
            jsonb.toJson(tasks, new FileOutputStream(filename));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}