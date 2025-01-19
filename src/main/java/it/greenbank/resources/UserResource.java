package it.greenbank.resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.logging.Logger;

import it.greenbank.entities.User;
import it.greenbank.entities.User.Create;
import it.greenbank.entities.User.Update;
import it.greenbank.services.UserService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/users")
public class UserResource {

    @Inject
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    UserService userService;

    private static final Logger LOG = Logger.getLogger(UserResource.class);

    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Get user",
        description = "Get user by given ID"
    )
    public User getUser(@PathParam("userId") Long userId) {
        LOG.infov("Get user by ID {0}", userId);
        return userService.getUser(userId);
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Create user",
        description = "Create a new user"
    )
    public User createUser(@Valid @ConvertGroup(to = Create.class) User user) {
        LOG.infov("Creating new user for firstname [{0}] and lastname [{1}]", user.firstname, user.lastname);
        return userService.createUser(user);
    }

    @PUT
    @Path("/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Update user",
        description = "Update an existing user"
    )
    public User updateUser(@PathParam("userId") Long userId, @Valid @ConvertGroup(to = Update.class) User user) {
        LOG.infov("Updating user with ID {0}", userId);
        return userService.updateUser(userId, user);
    }

    @DELETE
    @Path("/{userId}")
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(
        summary = "Delete user",
        description = "Delete an existing user"
    )
    public void deleteUser(@PathParam("userId") Long userId) {
        LOG.infov("Deleting user with ID {0}", userId);
        userService.deleteUser(userId);
    }

    @PUT
    @Path("/{userId}/disable")
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(
        summary = "Disable user",
        description = "Disable an existing user"
    )
    public void disableUser(@PathParam("userId") Long userId) {
        LOG.infov("Disabling user with ID {0}, new status --> {1}", userId, false);
        userService.changeStatus(userId, false);
    }

    @PUT
    @Path("/{userId}/enable")
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(
        summary = "Enable user",
        description = "Enable an existing user"
    )
    public void enableUser(@PathParam("userId") Long userId) {
        LOG.infov("Enabling user with ID {0}, new status --> {1}", userId, true);
        userService.changeStatus(userId, true);
    }
    
}
