package dev.langchain4j.quarkus.agentic.deepdive.mcp.server.geo;

import io.quarkus.rest.client.reactive.ClientQueryParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestQuery;

@RegisterRestClient(configKey = "geocoding")
@Path("/v1")
public interface GeoCodingService {

    @GET
    @Path("/search")
    @ClientQueryParam(name = "count", value = "1")
    GeoResults findCity(@RestQuery String name);

}
