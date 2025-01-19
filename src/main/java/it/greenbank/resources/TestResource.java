package it.greenbank.resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.logging.Logger;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import it.greenbank.services.TestService;
import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/test")
public class TestResource {

    @Inject
    public TestResource(TestService testService) {
        this.testService = testService;
    }

    TestService testService;

    private static final Logger LOG = Logger.getLogger(TestResource.class);

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(
        summary = "Test API",
        description = "Just a test to check if service is running"
    )
    public String test() {
        LOG.info("--- Reached test controller, service is working! ---");
        return "TEST-OK";
    }

    @POST
    @Path("/measurement")
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(
        summary = "Perform comsumption test",
        description = "Executes the same API repeatedly based on the number of runs provided in input"
    )
    public Long measurement(@QueryParam("howManyRuns") @DefaultValue("1") int howManyRuns) {
        LOG.infov("Performing {0} run(s) for measurement purposes", howManyRuns);
        return testService.measurement(howManyRuns);
    }

    @POST
    @Path("/measurement-async")
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(
        summary = "Perform comsumption test asynchronously",
        description = "Executes the same API repeatedly (asynchronously) based on the number of runs provided in input. Used by the bash script"
    )
    public Response measurementAsync(@QueryParam("howManyRuns") @DefaultValue("1") int howManyRuns) {
        LOG.infov("[Async] - Performing {0} run(s) for mqeasurement purposes", howManyRuns);

        Uni.createFrom()
            .item(howManyRuns)
            .emitOn(Infrastructure.getDefaultWorkerPool())
            .subscribe().with(
                item -> testService.measurement(howManyRuns), Throwable::printStackTrace
            );

        return Response.noContent().build();
    }
    
}
