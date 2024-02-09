package aor.paj.service;

import aor.paj.bean.UserBean;
import aor.paj.dto.Task;
import aor.paj.dto.User;
import aor.paj.dto.UserWithNoPassword;
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
    UserSession userSession;

    // adicionar um utilizador
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(User user) {
        if (!validateUserOnRegistration(user)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Data").build();
        }
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
    @GET
    @Path("/getphoto")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPhoto(@HeaderParam("username")String username) {
        if(userSession.getCurrentUser().equals(username)){
            String photoUrl = userBean.getPhotoURLByUsername(username);
            System.out.println(photoUrl);
            if(photoUrl != null) return Response.status(200).entity("{\"photoUrl\":\"" + photoUrl + "\"}").build();
            return Response.status(404).entity("{\"error\":\"No photo found\"}").build();
        }
        return Response.status(403).entity("{\"error\":\"Access denied\"}").build();
    }
    @GET
    @Path("/userinfo")
    @Produces(MediaType.APPLICATION_JSON)
    public Response userInfo(@HeaderParam("username") String username) {
        if (userSession.getCurrentUser().equals(username)) {
            User user = userBean.getUserByUsername(username);
            System.out.println(user);
            if (user != null) {
                // Converte User para UserWithNoPassword
                UserWithNoPassword userWithoutPassword = new UserWithNoPassword(
                        user.getUsername(),
                        user.getPhoneNumber(),
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getPhotoURL());

                // Retorna a entidade UserWithNoPassword em vez da entidade User completa
                return Response.status(200).entity(userWithoutPassword).build();
            }
            return Response.status(404).entity("{\"error\":\"No user found\"}").build();
        }
        return Response.status(403).entity("{\"error\":\"Access denied\"}").build();
    }
    @POST
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logout(@HeaderParam("username")String username) {
        System.out.println(username);
        if(userSession.getCurrentUser().equals(username)){
            userSession.logout();
            System.out.println(userSession.getCurrentUser() + " Current User");
            return Response.status(200).entity("Successful Logout").build();
        }
        return Response.status(403).entity("Access denied").build();
    }

     //obter todos os utilizadores e resposta com status 200
     @GET
     @Path("/all")
     @Produces(MediaType.APPLICATION_JSON)
     public List<User> getAllUsers() {
         return userBean.getAllUsers();
     }

    @PATCH
    @Path("/edituserdata")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editUserData(User updatedUser, @HeaderParam("username") String username) {
        if (!userSession.getCurrentUser().equals(username)) {
            return Response.status(403).entity("{\"error\":\"Access denied\"}").build();
        }
        if (!validateUserOnEdit(updatedUser)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Data").build();
        }

        boolean updateResult = userBean.updateUser(username, updatedUser);

        if (updateResult) {
            return Response.status(Response.Status.OK).entity("User data updated successfully").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"User not found\"}").build();
        }
    }

    /**Validation*/
    private boolean validateUsername(String username) {
        return username != null && username.length() >= 2 && username.length() <= 20;
    }

    private boolean validateEmail(String email) {
        return email != null && email.contains("@") && email.indexOf('@') < email.lastIndexOf('.');
    }

    private boolean validatePhone(String phone) {
        if (phone == null || phone.length() < 9 || phone.length() > 20) return false;
        if (phone.startsWith("+")) {
            for (int i = 1; i < phone.length(); i++) {
                if (!Character.isDigit(phone.charAt(i))) {
                    return false;
                }
            }
        } else {
            // Verifica se todos os caracteres são dígitos
            for (int i = 0; i < phone.length(); i++) {
                if (!Character.isDigit(phone.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean validateName(String firstName, String lastName) {
        if (firstName == null || lastName == null) return false;
        return firstName.length() >= 3 && firstName.length() <= 25 &&
                lastName.length() >= 3 && lastName.length() <= 25;
    }

    private boolean validatePhotoURL(String photoURL) {
        return photoURL != null && photoURL.length() >= 3 && photoURL.length() <= 500;
    }
    private boolean validatePassword(String password) {
        // Verifica se a senha não é nula e se tem pelo menos 6 caracteres
        return password != null && password.length() >= 6;
    }
    private boolean validateUserOnRegistration(User user) {
        return validateUsername(user.getUsername()) &&
                validatePassword(user.getPassword()) &&
                validateEmail(user.getEmail()) &&
                validatePhone(user.getPhoneNumber()) &&
                validateName(user.getFirstName(), user.getLastName()) &&
                validatePhotoURL(user.getPhotoURL());
    }
    private boolean validateUserOnEdit(User user) {
        return validateEmail(user.getEmail()) &&
                validatePhone(user.getPhoneNumber()) &&
                validateName(user.getFirstName(), user.getLastName()) &&
                validatePhotoURL(user.getPhotoURL());
    }
}