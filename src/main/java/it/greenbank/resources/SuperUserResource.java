package it.greenbank.resources;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.ResponseStatus;

import it.greenbank.entities.Account;
import it.greenbank.entities.Card;
import it.greenbank.entities.Event;
import it.greenbank.entities.User;
import it.greenbank.services.AccountService;
import it.greenbank.services.CardService;
import it.greenbank.services.EventService;
import it.greenbank.services.ExportService;
import it.greenbank.services.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/su")
public class SuperUserResource {
    
    @Inject
    AccountService accountService;

    @Inject
    UserService userService;

    @Inject
    CardService cardService;

    @Inject
    EventService eventService;

    @Inject
    ExportService exportService;

    private static final Logger LOG = Logger.getLogger(SuperUserResource.class);

    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Get all users",
        description = "Returns all users (no filters applied)"
    )
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GET
    @Path("/accounts")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Get all accounts",
        description = "Returns all accounts (no filters applied)"
    )
    public List<Account> getAllAccounts(@PathParam("userId") Long userId) {
        return accountService.getAllAccounts();
    }

    @GET
    @Path("/cards")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Get all cards",
        description = "Return all cards (no filters applied)"
    )
    public List<Card> getAllCards() {
        return cardService.getAllCards();
    }

    @GET
    @Path("/events")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Get all events",
        description = "Return all events (no filters applied)"
    )
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @DELETE
    @Path("/delete-all-events")
    @Produces(MediaType.TEXT_PLAIN)
    @ResponseStatus(204)
    @Operation(
        summary = "Delete all events",
        description = "Delete all existing events (physical deletion)"
    )
    public void deleteAllEvents() {
        eventService.deleteAllEvents();
    }

    @POST
    @Path("/export-to-file")
    @ResponseStatus(204)
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(
        summary = "Export to file",
        description = "Export the content of the DB to a file"
    )
    public void exportToFile() throws Exception {
        exportService.exportToFile();
    }
    
}
