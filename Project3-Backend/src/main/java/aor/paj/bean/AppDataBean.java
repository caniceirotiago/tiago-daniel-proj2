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
public class AppDataBean {
    final String filename = "allAppData.json";
    private ArrayList<Task> tasks;
    private ArrayList<User> users;
    public AppDataBean() {
        File f = new File(filename);
        if(f.exists()){
            try {
                FileReader filereader = new FileReader(f);
                activities = JsonbBuilder.create().fromJson(filereader, new
                        ArrayList<Activity>() {}.getClass().getGenericSuperclass());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }else
            activities = new ArrayList<Activity>();
    }
    public void addActivity(Activity a) {
        activities.add(a);
        writeIntoJsonFile();
    }
    public Activity getActivity(int i) {
        for (Activity a : activities) {
            if (a.getId() == i)
                return a;
        }
        return null;
    }
    public ArrayList<Activity> getActivities() {
        return activities;
    }
    public boolean remoreActivity(int id) {
        for (Activity a : activities) {
            if (a.getId() == id) {
                activities.remove(a);
                return true;
            }
        }
        return false;
    }
    public boolean updateActivity(int id, Activity activity) {
        for (Activity a : activities) {
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
            jsonb.toJson(activities, new FileOutputStream(filename));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}