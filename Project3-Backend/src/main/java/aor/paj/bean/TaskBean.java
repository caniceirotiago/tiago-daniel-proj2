/**
 * Código Daniel
 */

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
import jakarta.validation.Valid;


// validar dto e bean jakarta api
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

    public void readJsonFile() {
        File f = new File(filename);
        if (f.exists()) {
            try {
                FileReader filereader = new FileReader(f);
                tasks = JsonbBuilder.create().fromJson(filereader, new
                        ArrayList<Task>() {
                        }.getClass().getGenericSuperclass());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
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
        readJsonFile();
    }


    // nota: não é o ultimo criado, mas o maior ID presente (a ultima pode ter sido apagada, por exemplo)
    public int getLastTaskIdCreated() {
        int maxId = 0;
        for (Task t : tasks) {
            if (t.getId() > maxId) maxId = t.getId();
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


    // para R6 - List tasks of a user
    // obter lista de tarefas por user
    public ArrayList<Task> getAllTasksByUser(String username) {
        ArrayList<Task> listOfUserTasks = new ArrayList<>();
        for (Task t : tasks) {
            if (t.getUsername().equals(username)) {
                listOfUserTasks.add(t);
            }
        }
        return orderTasksByPriorityStartAndEndDate(listOfUserTasks);
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

    /**
    métodos para ordenar as tasks
     */

    private ArrayList<Task> orderTasksByPriorityStartAndEndDate(ArrayList<Task> listOfUserTasks) {

        listOfUserTasks.sort(Comparator.comparing(Task::getPriority).reversed()
                .thenComparing(Task::getStartDate, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(Task::getEndDate, Comparator.nullsLast(Comparator.naturalOrder())));
    return listOfUserTasks;}




//    // funciona para ordenar por prioridade, e depois data de inicio e data de fim
//    public ArrayList<Task> orderTasksByPriorityStartAndEndDate(ArrayList<Task> tasks) {
//        Task temp;
//        for (int i = 0; i < tasks.size(); i++) {
//            for (int j = i + 1; j < tasks.size(); j++) {
//                boolean toChange = false;
//
//                Task taskI = tasks.get(i);
//                Task taskJ = tasks.get(j);
//
//                // Verifica se deve trocar com base na prioridade
//                if (taskI.getPriority() < taskJ.getPriority()) {
//                    toChange = true;
//                }
//                // Se as prioridades são iguais, verifica a data de início
//                else if (taskI.getPriority() == taskJ.getPriority() &&
//                        ((taskI.getStartDate() == null && taskJ.getStartDate() != null) ||
//                                (taskI.getStartDate() != null && taskJ.getStartDate() != null &&
//                                        taskI.getStartDate().isAfter(taskJ.getStartDate())))) {
//                    toChange = true;
//                }
//                // Se as prioridades e datas de início são iguais, verifica a data de término
//                else if (taskI.getPriority() == taskJ.getPriority() &&
//                        ((taskI.getStartDate() == null && taskJ.getStartDate() == null) ||
//                                (taskI.getStartDate() != null && taskJ.getStartDate() != null &&
//                                        taskI.getStartDate().isEqual(taskJ.getStartDate()))) &&
//                        ((taskI.getEndDate() == null && taskJ.getEndDate() != null) ||
//                                (taskI.getEndDate() != null && taskJ.getEndDate() != null &&
//                                        taskI.getEndDate().isAfter(taskJ.getEndDate())))) {
//                    toChange = true;
//                }
//
//                if (toChange) {
//                    temp = tasks.get(i);
//                    tasks.set(i, tasks.get(j));
//                    tasks.set(j, temp);
//                }
//            }
//        }
//        return tasks;
//    }



    ////// FALTA O MÉTODO PARA O UPDATE DA TASK

    public void updateTask(int id, Task updatedTask) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                // Atualiza os campos da tarefa, exceto o ID
                if (updatedTask.getTitle() != null && !updatedTask.getTitle().isEmpty()) {
                    task.setTitle(updatedTask.getTitle());
                }
                if (updatedTask.getDescription() != null && !updatedTask.getDescription().isEmpty()) {
                    task.setDescription(updatedTask.getDescription());
                }
                if (updatedTask.getPriority() != 0) { // Assume 0 como valor não válido
                    task.setPriority(updatedTask.getPriority());
                }
                if (updatedTask.getStatus() != 0) { // Assume 0 como valor não válido
                    task.setStatus(updatedTask.getStatus());
                }
                if (updatedTask.getStartDate() != null) {
                    task.setStartDate(updatedTask.getStartDate());
                }
                if (updatedTask.getEndDate() != null) {
                    task.setEndDate(updatedTask.getEndDate());
                }
                // Considere adicionar verificações para outros campos conforme necessário

                // Atualiza o arquivo JSON
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