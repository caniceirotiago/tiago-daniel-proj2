package aor.paj.service;

import aor.paj.bean.UserBean;
import aor.paj.dto.Task;
import aor.paj.dto.User;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;


@Path("/user")

public class UserService {
    @Inject
    UserBean userBean;
    UserSession userSession = new UserSession();

    // adicionar um utilizador
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(User user) {
        if(userBean.userExists(user.getUsername(),user.getEmail())){
            return Response.status(409).entity("Username or Email already Exists").build();
        }
        else{
            userBean.addUser(user);
            return Response.status(200).entity("A new user is created").build();
        }
    }
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(@HeaderParam("username")String username, @HeaderParam("password")String password) {
       if(userBean.loginConfirmation(username, password)){
           userSession.setCurrentUser(username);
           System.out.println(userSession.getCurrentUser() + " Current User");
           return Response.status(200).entity("Successful Login").build();
       }
       else{
           return Response.status(401).entity("Login Failed").build();
       }
    }

     //obter todos os utilizadores e resposta com status 200
     @GET
     @Path("/all")
     @Produces(MediaType.APPLICATION_JSON)
     public List<User> getAllUsers() {
         return userBean.getAllUsers();
     }
}