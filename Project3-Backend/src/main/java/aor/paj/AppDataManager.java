package aor.paj;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;

import aor.paj.dto.Task;
import aor.paj.dto.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
/**
 * Manages application data including users and tasks, utilizing a JSON file for persistence.
 *
 */
public class AppDataManager {
    final String filename = "allAppData.json";
    private ArrayList<Task> tasks;
    private ArrayList<User> users;
    public AppDataManager() {
        File f = new File(filename);
        if(f.exists()){
            try {
                FileReader filereader = new FileReader(f);
                users = JsonbBuilder.create().fromJson(filereader, new
                        ArrayList<User>() {}.getClass().getGenericSuperclass());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }else{
            users = new ArrayList<User>();
            tasks = new ArrayList<Task>();
        }
    }
    public boolean confirmUserExists(String username, String email){
        for(User us : users){
            if(us.getEmail().equals(email) || us.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }
    public void addUser(User a) {
        users.add(a);
        writeIntoJsonFile();
    }
    private void writeIntoJsonFile(){
        Jsonb jsonb = JsonbBuilder.create(new
                JsonbConfig().withFormatting(true));
        try {
            jsonb.toJson(users, new FileOutputStream(filename));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}