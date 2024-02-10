/**Código Daniel*/

package aor.paj.service;
import aor.paj.bean.TaskBean;
import aor.paj.bean.UserBean;
import aor.paj.dto.Task;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.Path;
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

    // editar task - Change task of user tasks
    @PUT
    @Path("/edit")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editTask(@HeaderParam("username") String username, @HeaderParam("password") String password, Task a) {
        if (username == null || password == null)
            return Response.status(401).entity("{\"Error\":\"User not logged in\"}").build();
        else if (userBean.loginConfirmation(username, password) && username.equals(a.getUsername())) {
            // taskBean.editTask(a);
            // chamar get task atualizada
            return Response.status(200).entity("Task edited").build();
        } else
            return Response.status(403).entity("{\"Error\":\"User permissions violated. Can't edit tasks of other users\"}").build();
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
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeTask(@HeaderParam("username") String username, @HeaderParam("password") String password, @QueryParam("id") String id) {
        if (username == null || password == null)
            return Response.status(401).entity("{\"Error\":\"User not logged in\"}").build();
        else if (userBean.loginConfirmation(username, password) && username.equals(taskBean.getTask(id).getUsername())) {
            boolean deleted = taskBean.removeTask(id);
            if (!deleted)
                return Response.status(404).entity("{\"Error\":\"Task not found\"}").build();
            return Response.status(200).entity("Task deleted").build();
        } else
            return Response.status(403).entity("{\"Error\":\"User permissions violated. Can't delete tasks of other users\"}").build();
    }
}