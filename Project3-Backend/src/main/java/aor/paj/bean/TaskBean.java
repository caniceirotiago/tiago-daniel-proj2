/**Código Daniel*/

package aor.paj.bean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.*;

import aor.paj.dto.Task;
import aor.paj.dto.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

@ApplicationScoped
public class TaskBean {
    final String filename = "allTasks.json";

    // guarda todas as tasks
    private ArrayList<Task> tasks;
    // estabelece relação entre utilizador e quais as suas tasks
    // chave: username, valor: lista de tasks
    // implementado como um HashMap para possibilitar no futuro ter mais do que uma tasks por utilizador
    // o hashmap não precisa de ser guardado em ficheiro, pq é reconstruido a partir da lista de tasks


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
                throw new RuntimeException(e);
            }
        }else{
            tasks = new ArrayList<Task>();
        }
    }

    public void addTask(Task a) {
        int tempId = getLastTaskIdCreated();
        a.setId(tempId);
        // adiciona a task à lista de tasks
        tasks.add(a);
        // atualiza o ficheiro de tasks
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

    // getter do hashmap

    // obter lista de tarefas por user
    public ArrayList<Task> getAllTasksByUser(String username) {
        ArrayList<Task> listOfUserTasks = new ArrayList<>();
        for(Task t : tasks){
            if(t.getUsername().equals(username)) listOfUserTasks.add(t);
        }
        return listOfUserTasks;
    }

    public boolean removeTask(int id) {
        for (Task a : tasks) {
            if (a.getId() == id) {
                tasks.remove(a);
                // atualiza o ficheiro de tasks
                writeIntoJsonFile();
                readJsonFile();
                return true;
            }
        }
        // não encontrou a task
        return false;
    }

    ////// FALTA O MÉTODO PARA O UPDATE DA TASK

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