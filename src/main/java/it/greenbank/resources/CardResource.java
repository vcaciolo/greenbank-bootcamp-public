package it.greenbank.resources;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.ResponseStatus;

import it.greenbank.entities.Card;
import it.greenbank.entities.Card.Create;
import it.greenbank.services.CardService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.ConvertGroup;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/users/{userId}/accounts/{accountId}/cards")
public class CardResource {

    @Inject
    public CardResource(CardService cardService) {
        this.cardService = cardService;
    }

    CardService cardService;

    private static final Logger LOG = Logger.getLogger(AccountResource.class);

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Get all cards by account ID",
        description = "Return all cards registered with the given account ID"
    )
    public List<Card> getAllAccountCards(@PathParam("accountId") Long accountId, @PathParam("userId") Long userId) {
        LOG.infov("Getting all card of user {0} and account {1}", userId, accountId);
        return cardService.getAllAccountCards(accountId);
    }

    @GET
    @Path("/{cardId}/check-pin")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Operation(
        summary = "Check card PIN",
        description = "Check if the PIN associated to given card is valid or not"
    )
    public Boolean checkPin(@PathParam("cardId") Long cardId, @NotBlank @Size(min = 4, max = 5) @QueryParam(value = "pin") String pin, @PathParam("accountId") Long accountId, @PathParam("userId") Long userId) {
        LOG.infov("Checking PIN of card with ID {0} linked to account {1} of user {2}", cardId, accountId, userId);
        return cardService.checkPin(cardId, accountId, userId, pin);
    }

    @GET
    @Path("/{cardId}/expired")
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(
        summary = "Check card expiry date",
        description = "Check if the card is expired or not"
    )
    public Boolean isExpired(@PathParam("accountId") Long accountId, @PathParam("cardId") Long cardId, @PathParam("userId") Long userId) {
        LOG.infov("Checking if card with ID {0} linked to account {1} of user {2} is expired", cardId, accountId, userId);
        //TODO: to be completed
        return false;
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Create a new card",
        description = "Create (emit) a new card"
    )
    public Card emitNewCard(@PathParam("accountId") Long accountId, @Valid @ConvertGroup(to = Create.class) Card card, @PathParam("userId") Long userId) {
        LOG.infov("Creating new card associated to account {0} of user {1}", accountId, userId);
        return cardService.emitNewCard(card, accountId, userId);
    }

    @DELETE
    @Path("/{cardId}")
    @Produces(MediaType.TEXT_PLAIN)
    @ResponseStatus(204)
    @Operation(
        summary = "Delete card",
        description = "Delete an existing card"
    )
    public void destroyCard(@PathParam("accountId") Long accountId, @PathParam("cardId") Long cardId, @PathParam("userId") Long userId) {
        LOG.infov("Destroying card with ID {0} of account {1} of user {2}", cardId, accountId, userId);
        cardService.destroyCard(cardId, accountId, userId);
    }
    
}
