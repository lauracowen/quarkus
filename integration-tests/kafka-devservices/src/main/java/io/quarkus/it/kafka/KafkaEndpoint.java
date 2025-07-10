package io.quarkus.it.kafka;

import java.util.concurrent.ExecutionException;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@Path("/kafka")
public class KafkaEndpoint {

    @Inject
    KafkaAdminManager admin;

    @GET
    @Path("/partitions/{topic}")
    public Integer partitions(@PathParam("topic") String topic) {
        return admin.partitions(topic);
    }

    @GET
    @Path("/port")
    public Integer partitions() throws ExecutionException, InterruptedException {
        return admin.port();
    }

    @GET
    @Path("/image")
    public String image() throws ExecutionException, InterruptedException {
        return admin.image();
    }
}
