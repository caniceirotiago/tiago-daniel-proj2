
package aor.paj.service;
import aor.paj.bean.TaskBean;
import aor.paj.bean.UserBean;
import aor.paj.dto.Task;
import aor.paj.dto.TaskUpdate;
import aor.paj.service.validator.TaskValidator;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/task")
public class TaskService {
    @Inject
    TaskBean taskBean;
    @Inject
    TaskValidator taskValidator;
    @Inject
    UserBean userBean;

    /**
     * Retrieves the details of a specific task identified by its ID. This endpoint requires user authentication
     * and ensures that a task can only be accessed by the user who created it or who has been granted permission.
     * Requires authentication through request headers "username" and "password". The task's ID is specified in the
     * URL path.
     * Responses:
     * - 401 Unauthorized: Indicates the user is not logged in or the username/password is not provided.
     * - 403 Forbidden: Occurs if the login credentials are incorrect or if the authenticated user does not have
     *   permission to view the task (i.e., attempting to access a task owned by another user).
     * - 404 Not Found: Returned if there is no task matching the provided ID in the system.
     * - 200 OK: Success response, indicating the task was found and the user has permissions to view it. The
     *   response body includes the task's details in JSON format.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTaskById(@PathParam("id") int id, @HeaderParam("username") String username,
                                @HeaderParam("password") String password) {
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
        return Response.status(200).entity(task).build();
    }

    /**
     * Retrieves all tasks associated with the authenticated user. This endpoint ensures that users can only access
     * their own tasks, maintaining privacy and security. Authentication is required, using the "username" and "password"
     * provided in the request headers.
     * Responses:
     * - 401 Unauthorized: Returned if either the username or password header is not provided, indicating that the user
     *   is not logged in or authentication details are missing.
     * - 403 Forbidden: Occurs if the authentication fails, either due to incorrect credentials or an attempt to access
     *   tasks associated with a different user, violating user permissions.
     * - 200 OK: Success response, indicating that the tasks associated with the authenticated user were successfully
     *   retrieved. The response body contains an array of tasks in JSON format, specific to the user.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/all")
    public Response getAllTasksByUser(@HeaderParam("username") String username, @HeaderParam("password") String password) {
        if (username == null || password == null)
            return Response.status(401).entity("User not logged in").build();
        else if (userBean.loginConfirmation(username, password)) {
            return Response.ok(taskBean.getAllTasksByUser(username)).build();
        } else
            return Response.status(403).entity("User permissions violated. Can't see tasks of other users").build();

    }

    /**
     * Allows an authenticated user to edit the details of a specific task identified by its ID. This endpoint
     * ensures that a user can only edit their own tasks, requiring both user authentication and authorization
     * based on task ownership. The task's new details are provided in the request body in JSON format.
     * Responses:
     * - 401 Unauthorized: Returned if the "username" or "password" header is not provided, indicating that the user
     *   is not logged in or authentication details are missing.
     * - 404 Not Found: Occurs if no task matching the provided ID exists in the system, indicating an invalid task ID.
     * - 403 Forbidden: Returned if the authentication fails, either due to incorrect credentials, or if the authenticated
     *   user does not own the task specified by the ID, preventing editing of tasks owned by other users.
     * - 200 OK: Success response, indicating that the task was successfully updated with the new details. A message
     *   confirming the successful update is included in the response body.
     */

    @PATCH
    @Path("/edit/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editTask(@PathParam("id") int id, TaskUpdate taskUpdates, @HeaderParam("username") String username,
                             @HeaderParam("password") String password) {
        if (username == null || password == null) {
            return Response.status(401).entity("User not logged in").build();
        }
        Task existingTask = taskBean.getTask(id);
        if (existingTask == null) {
            return Response.status(404).entity("Task not found").build();
        }
        if (!userBean.loginConfirmation(username, password) || !username.equals(existingTask.getUsername())) {
            return Response.status(403).entity("User permissions violated. Can't edit tasks of other users").build();
        }

        // Persiste as alterações
        taskBean.updateTask(id, taskUpdates);
        return Response.status(200).entity("Task updated successfully").build();
    }

    /**
     * Creates a new task associated with the authenticated user. This endpoint requires user authentication and checks
     * that the task being created belongs to the user making the request. The details of the task to be created are
     * provided in the request body in JSON format.
     * Responses:
     * - 401 Unauthorized: Returned if the "username" or "password" header is not provided, indicating that the user
     *   is not logged in or authentication details are missing.
     * - 403 Forbidden: Occurs if the authenticated user does not match the username associated with the task being
     *   created, preventing users from adding tasks on behalf of others.
     * - 400 Bad Request: Returned if the task data fails validation checks, indicating that the provided task
     *   details are invalid or incomplete.
     * - 201 Created: Success response, indicating that the new task has been successfully created and stored. A
     *   message confirming the creation of the task is included in the response body.
     */
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTask(@HeaderParam("username") String username, @HeaderParam("password") String password, Task a) {
        System.out.println(a);
        if (username == null || password == null)
            return Response.status(401).entity("{\"Error\":\"User not logged in\"}").build();
        else if (userBean.loginConfirmation(username, password) && username.equals(a.getUsername())) {
            if(taskValidator.validateTask(a)){
                taskBean.addTask(a);
                return Response.status(201).entity("A new task has been created").build();
            }else return Response.status(400).entity("Invalid task data").build();
        } return Response.status(403).entity("User permissions violated. Can't add tasks to other users").build();
    }

    /**
     * Deletes a specific task identified by its ID, accessible only by the authenticated user who owns the task.
     * This endpoint requires user authentication, and authorization is verified to ensure that users can only
     * delete their own tasks. The task ID to be deleted is specified in the URL path.
     * Responses:
     * - 401 Unauthorized: Returned if either the "username" or "password" header is not provided, indicating
     *   that the user is not logged in or authentication details are missing.
     * - 404 Not Found: Occurs if no task matching the provided ID exists in the system, indicating an invalid
     *   task ID or that the task has already been deleted.
     * - 403 Forbidden: Returned in two scenarios: 1) if the authentication fails due to incorrect credentials,
     *   or 2) if the authenticated user does not own the task specified by the ID, preventing deletion of tasks
     *   owned by other users.
     * - 200 OK: Success response, indicating that the task was successfully deleted. A message confirming the
     *   successful deletion of the task is included in the response body.
     */
    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeTask(@HeaderParam("username") String username, @HeaderParam("password") String password,
                               @PathParam("id") String id) {
        if (username == null || password == null)
            return Response.status(401)
                    .entity("User not logged in")
                    .build();
        else if (userBean.loginConfirmation(username, password)) {
            if (taskBean.getTask(Integer.parseInt(id)) == null)
                return Response.status(404)
                        .entity("Task not found")
                        .build();
            else if(!username.equals(taskBean.getTask(Integer.parseInt(id)).getUsername())){
                return Response.status(403)
                        .entity("User permissions violated. Can't delete tasks of other users")
                        .build();
            }
            boolean deleted = taskBean.removeTask(Integer.parseInt(id));
            if (deleted) return Response.status(200)
                    .entity("Task deleted")
                    .build();
        } return Response.status(403)
                .entity("User permissions violated. Can't delete tasks of other users")
                .build();
    }
}