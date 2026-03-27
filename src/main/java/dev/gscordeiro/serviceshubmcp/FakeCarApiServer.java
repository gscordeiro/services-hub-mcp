import io.javalin.Javalin;
import org.slf4j.LoggerFactory;

import java.util.Map;

 void main() {

    var logger = LoggerFactory.getLogger(this.getClass());
     var app = Javalin.create(config -> {
         config.routes.get("/cars/{id}", ctx -> {

             String id = ctx.pathParam("id");
             boolean includeDetails = Boolean.parseBoolean(
                     ctx.queryParam("includeDetails")
             );

             logger.info("Received request to fetch car with ID: {}, include details: {}", id, includeDetails);

             Map<String, Object> response;

             if (!includeDetails) {
                 response = Map.of(
                         "id", id,
                         "model", "Civic",
                         "brand", "Honda"
                 );
             } else {
                 response = Map.of(
                         "id", id,
                         "model", "Civic",
                         "brand", "Honda",
                         "year", 2022,
                         "price", 12_0000,
                         "color", "Black"
                 );
             }

             ctx.json(response);

         });
     }).start(8080);

    IO.println("Fake Car API (Javalin) running at http://localhost:8080");
}
