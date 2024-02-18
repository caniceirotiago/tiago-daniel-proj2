import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Random;

public class TaskClient {
    private final Client client;
    private final String taskBaseUrl;
    private final Gson gson;
    private final Random random;

    public TaskClient(String taskBaseUrl) {
        this.client = ClientBuilder.newClient();
        this.taskBaseUrl = taskBaseUrl;
        this.gson = new Gson();
        this.random = new Random();
    }

    public void addRandomTasksToUser(String username, String password, int numberOfTasks) {

        for (int i = 0; i < numberOfTasks; i++) {
            String randomTaskApiUrl = "https://www.boredapi.com/api/activity";
            Response response = client.target(randomTaskApiUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
                String tasksJson = response.readEntity(String.class);
                Task task = convertJsonToTask(tasksJson, username);
                addTaskToSystem(username, password, task);
            } else {
                System.out.println("Erro ao encontrar tarefas aleatórias: " + response.getStatus());
            }
        }
    }

    private Task convertJsonToTask(String tasksJson, String username) {
        // Cria uma tarefa com alguns dados aleatórios
        Task task = new Task();

        JsonObject taskJson = JsonParser.parseString(tasksJson).getAsJsonObject();



        String titleString = taskJson.get("type").getAsString();
        task.setTitle(titleString);
        String descriptionString = taskJson.get("activity").getAsString();
        task.setDescription(descriptionString);

        String startDate = String.format("2022-01-%02d", (random.nextInt(28) + 1));
        String endDate = String.format("2022-02-%02d", (random.nextInt(28) + 1));
        task.setStartDate(startDate);
        task.setEndDate(endDate);
        task.setPriority(1 + random.nextInt(3)); // Prioridade entre 1 e 3
        task.setStatus(new int[]{100, 200, 300}[random.nextInt(3)]); // Status: 100, 200 ou 300
        task.setUsername(username);
        return task;
    }


    private void addTaskToSystem(String username, String password, Task task) {
        WebTarget target = client.target(taskBaseUrl).path("/create");
        System.out.println("Tentativa de adicionar tarefa ao sistema: " + gson.toJson(task));

        Response response = target.request(MediaType.APPLICATION_JSON)
                .header("username", username)
                .header("password", password)
                .post(Entity.entity(task, MediaType.APPLICATION_JSON));

        if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
            System.out.println("Tarefa adicionada com sucesso.");
            System.out.println("Código de resposta: " + response.getStatus());
            System.out.println("Resposta: " + response.readEntity(String.class));
            System.out.println();
        } else {
            System.out.println("Erro ao adicionar tarefa: " + response.getStatus());
            System.out.println("Resposta: " + response.readEntity(String.class));
            System.out.println();
        }
    }
}
