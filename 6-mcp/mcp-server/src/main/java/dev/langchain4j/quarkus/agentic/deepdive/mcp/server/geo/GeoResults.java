package dev.langchain4j.quarkus.agentic.deepdive.mcp.server.geo;

import java.util.List;

public record GeoResults(List<GeoResult> results) {

    public GeoResult getFirst() {
        return results.get(0);
    }

}
