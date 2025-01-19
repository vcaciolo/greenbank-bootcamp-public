package it.greenbank.resources;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.logging.Logger;

import it.greenbank.entities.Event;
import it.greenbank.entities.EventType;
import it.greenbank.services.EventService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/events")
public class EventResource {

    @Inject
    public EventResource(EventService eventService) {
        this.eventService = eventService;
    }

    EventService eventService;

    private static final Logger LOG = Logger.getLogger(EventResource.class);

    @GET
    @Path("/by-user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Get all user events",
        description = "Get all events of the given user ID"
    )
    public List<Event> geteAllUserEvents(Long userId) {
        LOG.infov("Searching for events of user with ID {0}", userId);
        return eventService.getEventsForUserId(userId);
    }

    @GET
    @Path("/by-account/{accountId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Get all account events",
        description = "Get all events of the given account ID"
    )
    public List<Event> geteAllAccountEvents(Long accountId) {
        LOG.infov("Searching for events of accouunt with ID {0}", accountId);
        return eventService.getEventsForAccountId(accountId);
    }

    @GET
    @Path("/by-type")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Get events by type",
        description = "Get all events of the provided type"
    )
    public List<Event> getEventsByType(@QueryParam("eventType") EventType eventType) {
        LOG.infov("Searching events by type {0}", eventType);
        return eventService.getEventsByType(eventType);
    }
    
}
