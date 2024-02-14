/**Código Daniel*/

package aor.paj.service;
import aor.paj.bean.TaskBean;
import aor.paj.bean.UserBean;
import aor.paj.dto.Task;
import aor.paj.dto.TaskUpdate;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/task")
public class TaskService {
    @Inject
    TaskBean taskBean;
    @Inject
    UserBean userBean;

    // NOTAS
    // tudo isto é em relação a cada USER
    // OS ENDPOINTS são apenas para o user que está logado
    // para serem mostradas todas as tarefas , cada user teria acesso a tarefas
    // de outros users, o que não é suposto. quando existir user admin, cria-se esse endpoint especifico



    //R.EXTRA - Get task by id
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTaskById(@PathParam("id") int id, @HeaderParam("username") String username, @HeaderParam("password") String password) {
        if (username == null || password == null) {
            return Response.status(401).entity("{\"Error\":\"User not logged in\"}").build();
        }
        if (!userBean.loginConfirmation(username, password)) {
            return Response.status(403).entity("{\"Error\":\"Access Denied\"}").build();
        }
        Task task = taskBean.getTask(id);
        if (task == null) {
            return Response.status(404).entity("{\"Error\":\"Task not found\"}").build();
        }
        if (!task.getUsername().equals(username)) {
            return Response.status(403).entity("{\"Error\":\"User permissions violated. Can't view tasks of other users\"}").build();
        }
        return Response.ok(task).build();
    }


    //R6 - List tasks of a user

    // o output será um JSON com todas as tarefas desse user, validado
    // o status code será 200 (no ok), 404 (task não encontrada), 401 (nao autenticado) e 403 (não autorizado)


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/all")
    public Response getAllTasksByUser(@HeaderParam("username") String username, @HeaderParam("password") String password) {
        if (username == null || password == null)
            return Response.status(401).entity("{\"Error\":\"User not logged in\"}").build();
        else if (userBean.loginConfirmation(username, password)) {
            return Response.ok(taskBean.getAllTasksByUser(username)).build();
        } else
            return Response.status(403).entity("{\"Error\":\"User permissions violated. Can't see tasks of other users\"}").build();

    }

    @PATCH
    @Path("/edit/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editTask(@PathParam("id") int id, TaskUpdate taskUpdates, @HeaderParam("username") String username, @HeaderParam("password") String password) {
        if (username == null || password == null) {
            return Response.status(401).entity("{\"Error\":\"User not logged in\"}").build();
        }


        Task existingTask = taskBean.getTask(id);
        if (existingTask == null) {
            return Response.status(404).entity("{\"Error\":\"Task not found\"}").build();
        }
        if (!userBean.loginConfirmation(username, password) || !username.equals(existingTask.getUsername())) {
            return Response.status(403).entity("{\"Error\":\"User permissions violated. Can't edit tasks of other users\"}").build();
        }
        // Atualiza os campos da tarefa com os valores fornecidos
        if (taskUpdates.getTitle() != null) existingTask.setTitle(taskUpdates.getTitle());
        if (taskUpdates.getDescription() != null) existingTask.setDescription(taskUpdates.getDescription());
        if (taskUpdates.getPriority() != null) existingTask.setPriority(taskUpdates.getPriority());
        if (taskUpdates.getStatus() != null) existingTask.setStatus(taskUpdates.getStatus());
        // Trata a remoção ou atualização das datas
        if (taskUpdates.isRemoveStartDate()) existingTask.setStartDate(null);
        else if (taskUpdates.getStartDate() != null) existingTask.setStartDate(taskUpdates.getStartDate());
        if (taskUpdates.isRemoveEndDate()) existingTask.setEndDate(null);
        else if (taskUpdates.getEndDate() != null) existingTask.setEndDate(taskUpdates.getEndDate());


        // como foi criada uma cópia, verifica se os campos são válidos
        if(taskValidation(existingTask)){
            return Response.status(400).entity("{\"Error\":\"Invalid task fields\"}").build();}
        // Persiste as alterações
        taskBean.updateTask(id, existingTask);
        return Response.status(200).entity("Task updated successfully").build();
    }

    // adicionar task
    //R9 - Add task to user tasks
    // retornar o 200, o não autenticado (401) e o nao autorizado (403) , qd tenta outro user
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTask(@HeaderParam("username") String username, @HeaderParam("password") String password, Task a) {
        System.out.println(a);
        if (username == null || password == null)
            return Response.status(401).entity("{\"Error\":\"User not logged in\"}").build();
        else if (userBean.loginConfirmation(username, password) && username.equals(a.getUsername())) {
            // if / else para validar os campos da task
            if(taskValidation(a)){
                return Response.status(400).entity("{\"Error\":\"Invalid task fields\"}").build();}

            taskBean.addTask(a);
            return Response.status(201).entity("A new task has been created").build();
        } else
            return Response.status(403).entity("{\"Error\":\"User permissions violated. Can't add tasks to other users\"}").build();
    }


    // R10 - Delete task of user tasks
    //o status code será 200 (no ok), 401 (nao autenticado) e 403 (não autorizado)
    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeTask(@HeaderParam("username") String username, @HeaderParam("password") String password, @PathParam("id") String id) {
        if (username == null || password == null)
            return Response.status(401).entity("{\"Error\":\"User not logged in\"}").build();
        else if (userBean.loginConfirmation(username, password) && username.equals(taskBean.getTask(Integer.parseInt(id)).getUsername())) {
            boolean deleted = taskBean.removeTask(Integer.parseInt(id));
            if (!deleted)
                return Response.status(404).entity("{\"Error\":\"Task not found\"}").build();
            return Response.status(200).entity("Task deleted").build();
        } else
            return Response.status(403).entity("{\"Error\":\"User permissions violated. Can't delete tasks of other users\"}").build();
    }
    /**Validation of tasks @Backend**/


    //validação dos campos da task, caso true se verifique, invalida a criação/ediçao da task
    private boolean taskValidation(Task a) {

        // verifica se o titulo, descrição e prioridade são válidos
        //title não pode ser null, description não pode ser null, prioridade tem de ser entre 1 e 3 e status tem de ser 100
        // title nao pode ter mais de 20 caracteres e description nao pode ter mais de 180


        return a.getTitle() == null || a.getTitle().length() > 20 || a.getDescription().length() > 180 ||  a.getDescription() == null || !(a.getPriority() >= 1 && a.getPriority() <= 3)
                || a.getStatus() != 100 || a.getStartDate().isAfter(a.getEndDate());
    }




}


