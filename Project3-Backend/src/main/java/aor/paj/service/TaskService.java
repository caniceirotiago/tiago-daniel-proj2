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

    // NOTA :  a classe UserSession não está a ser usada, pq não é necessário
    // pq o user e pass está a ser passado no header através do sessionstorage,
    // vamos apenas comparar se o user que está a tentar fazer a ação é o mesmo que está logado
    // se não for, não deixa fazer a ação, sem alterar classes de estado de sessão em back end
    // porque isto é uma API RESTFUL, logo stateless, não pode ter estados


    // tudo isto é em relação a cada USER
    // OS ENDPOINTS são apenas para o user que está logado
    // para serem mostradas todas as tarefas , cada user teria acesso a tarefas
    // de outros users, o que não é suposto. quando existir user admin, cria-se esse endpoint especifico


    //R6 - List tasks of a user

    // fazer um get de todas as tarefas de um user, com um filtro por user
    // o path será /rest/task/all/query?user={username}
    // o método será GET
    // o output será um JSON com todas as tarefas desse user, validado
    // o status code será 200 (no ok), 401 (nao autenticado) e 403 (não autorizado)

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
    /**Edit*/// Garanta que este import esteja correto

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

        // Persiste as alterações
        taskBean.updateTask(id, taskUpdates);
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
}