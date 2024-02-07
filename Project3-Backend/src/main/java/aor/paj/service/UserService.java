package aor.paj.service;
import java.util.List;

import aor.paj.bean.AppDataBean;
import aor.paj.bean.UserBean;
import aor.paj.dto.Task;
import aor.paj.dto.User;
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


@Path("/user")

public class UserService {
    @Inject
    UserBean userBean;

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

    // obter todos os utilizadores e resposta com status 200
//    @GET
//    @Path("/all")
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<User> getAllUsers() {
//        return userBean.getAllUsers();
//    }

    // obter um utilizador
//    @GET
//    @Path("/{id}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public User getUser(@PathParam("id") int id) {
//        return userBean.getUser(id);
//    }
}