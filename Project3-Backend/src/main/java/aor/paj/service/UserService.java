package aor.paj.service;

import aor.paj.bean.UserBean;
import aor.paj.dto.Task;
import aor.paj.dto.User;
import aor.paj.dto.UserNewPassword;
import aor.paj.dto.UserWithNoPassword;
import aor.paj.service.validator.UserValidator;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;


@Path("/user")

public class UserService {
    @Inject
    UserBean userBean;
    @Inject
    UserValidator userValidator;

    /**
     * This endpoint is responsible for adding a new user to the system. It accepts JSON-formatted requests
     * containing user data and processes the request accordingly.
     * If the provided user data fails validation, it returns a status code of 400 (Bad Request) with the message
     * "Invalid Data".
     * If a user with the same username or email already exists in the system, it returns a status code of 409
     * (Conflict) with the message "Username or Email already Exists".
     * If the user is successfully added to the system, it returns a status code of 200 (OK) with the message
     * "A new user was created".
     */
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(User user) {
        if (!userValidator.validateUserOnRegistration(user)) {
            return Response.status(422).entity("Invalid Data").build();
        }
        if(userBean.userExists(user.getUsername(),user.getEmail())){
            return Response.status(409).entity("Username or Email already Exists").build();
        }
        else{
            userBean.addUser(user);
            return Response.status(200).entity("A new user was created").build();
        }
    }
    /**
     * This endpoint is responsible for user authentication. It accepts JSON-formatted requests containing
     * user credentials (username and password) as headers. It returns appropriate responses indicating the
     * success or failure of the login attempt.
     * Successful login returns a status code of 200, failed login returns 401, and missing username or password
     * returns 422.
     */

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(@HeaderParam("username")String username, @HeaderParam("password")String password) {
        if (username == null || password == null) {
            return Response.status(422)
                    .entity("Missing username or password")
                    .build();
        }
       if(userBean.loginConfirmation(username, password)){
           return Response.status(200)
                   .entity("Successful Login")
                   .build();
       }
       else{
           return Response.status(401)
                   .entity("Login Failed")
                   .build();
       }
    }
    /**
     * Retrieves the photo URL associated with the provided username.
     * If the username and password are not provided in the request headers, returns a status code 401 (Unauthorized)
     * with the error message "User not logged in".
     * If the provided credentials are invalid, returns a status code 403 (Forbidden) with the error message "Access denied".
     * If the photo URL is found for the given username, returns a status code 200 (OK) with the photo URL in JSON format.
     * If no photo URL is found for the given username, returns a status code 404 (Not Found) with the error message "No photo found".
     * */
    @GET
    @Path("/getphoto")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPhoto(@HeaderParam("username")String username, @HeaderParam("password")String password) {
        if (username == null || password == null)
            return Response.status(401)
                    .entity("User not logged in")
                    .build();
        else if (userBean.loginConfirmation(username, password)) {
            String photoUrl = userBean.getPhotoURLByUsername(username);
            if(photoUrl != null) return Response
                    .status(200)
                    .entity("{\"photoUrl\":\"" + photoUrl + "\"}").build();
            return Response.status(404)
                    .entity("No photo found")
                    .build();
        } else
            return Response.status(403)
                    .entity("Access denied")
                    .build();
    }
    /**
     * Retrieves user information for the given username.
     * If the username or password is missing in the request headers, returns a status code 401 (Unauthorized)
     * with the error message "User not logged in".
     * If the provided credentials are invalid, returns a status code 403 (Forbidden) with the error message "Access denied".
     * If the user information is successfully retrieved, returns a status code 200 (OK) with the user information
     * (without the password) in JSON format.
     */
    @GET
    @Path("/userinfo")
    @Produces(MediaType.APPLICATION_JSON)
    public Response userInfo(@HeaderParam("username") String username, @HeaderParam("password")String password) {
        if (username == null || password == null)
            return Response.status(401)
                    .entity("User not logged in")
                    .build();
        else if (userBean.loginConfirmation(username, password)) {
            // Converte User para UserWithNoPassword
            User user = userBean.getUserByUsername(username);
            UserWithNoPassword userWithoutPassword = userBean.convertUserToUserWithNoPassword(user);
            // Retorna a entidade UserWithNoPassword em vez da entidade User completa
            return Response.status(200)
                    .entity(userWithoutPassword)
                    .build();
        } else
            return Response.status(403)
                    .entity("Access denied")
                    .build();
    }
    /**
     * Retrieves a list of all users only to the admin(for now hardcoded).
     */
     @GET
     @Path("/all")
     @Produces(MediaType.APPLICATION_JSON)
     public Response getAllUsers(@HeaderParam("username") String username, @HeaderParam("password")String password) {
         if (username == null || password == null)
             return Response.status(401)
                     .entity("User not logged in")
                     .build();
         else if (username .equals("admin") && password.equals("admin")) {
             return Response.status(200)
                     .entity(userBean.getAllUsers())
                     .build();
         } else {
            return Response.status(403)
                    .entity("Access denied")
                    .build();
            }
     }
    /**
     * Edits the user data for the authenticated user.
     */
    @PATCH
    @Path("/edituserdata")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editUserData(User updatedUser, @HeaderParam("username") String username, @HeaderParam("password")String password) {
        if (username == null || password == null) {
            return Response.status(401)
                    .entity("User not logged in")
                    .build();
        }
        if (!userBean.loginConfirmation(username, password)) {
            return Response.status(401)
                    .entity("Login Failed")
                    .build();
        }
        if (!userValidator.validateUserOnEdit(updatedUser)) {
            return Response.status(400)
                    .entity("Invalid Data")
                    .build();
        }
        boolean updateResult = userBean.updateUser(username, updatedUser);
        if (updateResult) {
            return Response.status(200)
                    .entity("User data updated successfully")
                    .build();
        } else {
            return Response.status(500)
                    .entity("An error occurred while updating user data")
                    .build();
        }
    }
    @POST
    @Path("/edituserpassword")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editUserPassword(UserNewPassword updatedPassword, @HeaderParam("username") String username, @HeaderParam("password")String password) {
        if (username == null || password == null) {
            return Response.status(401)
                    .entity("User not logged in")
                    .build();
        }
        if (!userBean.loginConfirmation(username, password) && !userBean.loginConfirmation(username, updatedPassword.getPassword())) {
            return Response.status(401)
                    .entity("Login Failed or Passwords do not match")
                    .build();
        }
        if (!userValidator.validatePassword(updatedPassword.getNewPassword())) {
            return Response.status(400)
                    .entity("Invalid Data")
                    .build();
        }
        boolean updateResult = userBean.updatePassWord(username, updatedPassword.getNewPassword());
        if (updateResult) {
            return Response.status(200)
                    .entity("User data updated successfully")
                    .build();
        } else {
            return Response.status(500)
                    .entity("An error occurred while updating user data")
                    .build();
        }
    }
}