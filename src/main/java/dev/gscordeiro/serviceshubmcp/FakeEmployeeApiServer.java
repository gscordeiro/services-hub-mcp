import io.helidon.http.media.MediaContext;
import io.helidon.http.media.jackson.JacksonSupport;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.http.HttpRouting;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

void main() {

    var logger = LoggerFactory.getLogger(this.getClass());

    var employees = List.of(
            Map.of("registrationNumber", "1000", "name", "Alice Johnson", "department", "Engineering", "role", "Software Engineer"),
            Map.of("registrationNumber", "2000", "name", "Bob Smith", "department", "Marketing", "role", "Marketing Analyst"),
            Map.of("registrationNumber", "3000", "name", "Carol White", "department", "Finance", "role", "Financial Controller")
    );

    WebServer.builder()
            .mediaContext(MediaContext.builder()
                    .addMediaSupport(JacksonSupport.create())
                    .build())
            .routing(HttpRouting.builder()
                    .get("/employees", (req, res) -> {
                        logger.info("Received request to list employees");
                        res.send(employees);
                    })
            )
            .port(8081)
            .build()
            .start();

    IO.println("Fake Employee API (Helidon SE) running at http://localhost:8081");
}
