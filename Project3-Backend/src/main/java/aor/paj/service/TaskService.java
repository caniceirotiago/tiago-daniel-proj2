package aor.paj.service;
import java.util.List;
import aor.paj.bean.TaskBean;
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

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addActivity(Task a) {
        taskBean.addActivity(a);
        return Response.status(200).entity("A new activity is created").build();
    }
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActivity(@PathParam("id")int id) {
        Task activity = taskBean.getActivity(id);
        if (activity==null)
            return Response.status(200).entity("Activity with this idea is not found").build();
        return Response.status(200).entity(activity).build();
    }
    @DELETE
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeActivity(@QueryParam("id")int id) {
        boolean deleted = taskBean.remoreActivity(id);
        if (!deleted)
            return Response.status(200).entity("Activity with this idea is not found").build();
        return Response.status(200).entity("deleted").build();
    }
    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateActivity(Task a, @HeaderParam("id") int id) {
        boolean updated = taskBean.updateActivity(id, a);
        if (!updated)
            return Response.status(200).entity("Activity with this idea is not found").build();
        return Response.status(200).entity("updated").build();
    }
}