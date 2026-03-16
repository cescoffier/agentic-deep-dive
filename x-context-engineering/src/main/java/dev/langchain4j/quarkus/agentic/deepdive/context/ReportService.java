package dev.langchain4j.quarkus.agentic.deepdive.context;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import dev.langchain4j.quarkus.agentic.deepdive.context.Agents.ExpertRouterAgentWithMemory;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static dev.langchain4j.agentic.observability.HtmlReportGenerator.generateReport;

@Path("/report")
public class ReportService {

    @Inject
    ExpertRouterAgentWithMemory expertRouter;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response report() {
        return Response.ok(generateReport(expertRouter.agentMonitor())).build();
    }
}
