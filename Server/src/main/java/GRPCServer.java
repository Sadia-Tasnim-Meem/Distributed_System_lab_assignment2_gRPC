import User.UserService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class GRPCServer {
    private static final Logger logger = Logger.getLogger(GRPCServer.class.getName());

    public static void main (String[] args) throws Exception {
        Server server = ServerBuilder
                .forPort(8080)
                .addService(new UserService())
                .build();
        server.start();

        logger.info("Server started at port : " + server.getPort());
        server.awaitTermination(60, TimeUnit.SECONDS);
    }
}
