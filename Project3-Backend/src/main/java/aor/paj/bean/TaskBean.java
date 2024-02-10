package aor.paj.bean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.*;

import aor.paj.dto.Task;
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

    private Map<String, ArrayList<Task>> userTasksMap;


    public TaskBean() {
        File f = new File(filename);
        if (f.exists()) {
            try {
                FileReader filereader = new FileReader(f);
                // reconstruir a lista de tasks a partir do ficheiro
                tasks = JsonbBuilder.create().fromJson(filereader, new
                        ArrayList<Task>() {
                        }.getClass().getGenericSuperclass());

                // reconstruir o hashmap a partir da lista de atividades
                userTasksMap = new HashMap<>();
                for (Task a : tasks) {
                    if (userTasksMap.containsKey(a.getUsername())) {
                        userTasksMap.get(a.getUsername()).add(a);
                    } else {
                        ArrayList<Task> userTasks = new ArrayList<>();
                        userTasks.add(a);
                        userTasksMap.put(a.getUsername(), userTasks);
                    }

                }

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else
            tasks = new ArrayList<Task>();
        userTasksMap = new HashMap<>();

    }

    public void addTask(Task a) {
        // adiciona a task à lista de tasks
        tasks.add(a);
        // adiciona a task à lista de tasks do utilizador correspondente no hashmap ou cria uma nova lista
        if (userTasksMap.containsKey(a.getUsername())) {
            userTasksMap.get(a.getUsername()).add(a);
        } else {
            ArrayList<Task> userTasks = new ArrayList<>();
            userTasks.add(a);
            userTasksMap.put(a.getUsername(), userTasks);
        }
        // atualiza o ficheiro de tasks
        writeIntoJsonFile();


    }

    public Task getTask(String id) {
        for (Task a : tasks) {
            if (a.getId().equals(id))
                return a;
        }
        return null;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    // getter do hashmap
    public Map<String, ArrayList<Task>> getUserTasksMap() {
        return userTasksMap;
    }

    // obter lista de tarefas por user
    public ArrayList<Task> getAllTasksByUser(String user) {
        return userTasksMap.get(user);
    }

    public boolean removeTask(String id) {
        for (Task a : tasks) {
            if (a.getId().equals(id)) {
                tasks.remove(a);
                // Atualiza o HashMap
                getUserTasksMap().get(a.getUsername()).remove(id);
                if (getUserTasksMap() != null) {
                    getUserTasksMap().remove(a);
                    if (getUserTasksMap().isEmpty()) {
                        userTasksMap.remove(a.getUsername());
                    }
                }
                // atualiza o ficheiro de tasks
                writeIntoJsonFile();
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