import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import utils.CustomJackson;
import utils.H2Ds;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;

/**
 * EbankServer class.
 *
 */
public class EbankServer {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8080/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in Webservices package.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {

        // create JsonProvider to provide custom ObjectMapper
        JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
        provider.setMapper(CustomJackson.mapper);

        final ResourceConfig rc = new ResourceConfig().packages("webservices");
        rc.register(provider);

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * EbankServer method.
     * @param args
     * The web server start on localhost:8080
     */
    public static void main(String[] args) throws IOException {
        // Running url: http://localhost:8080/bankaccount
        final HttpServer server = startServer();

        System.out.println(String.format("Core eBank Server started available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));

        System.in.read();

        server.shutdownNow();
    }
}

