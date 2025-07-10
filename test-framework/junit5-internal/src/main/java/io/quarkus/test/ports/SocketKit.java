package io.quarkus.test.ports;

import java.io.IOException;
import java.net.Socket;

public class SocketKit {

    public static boolean isPortAlreadyUsed(int port) {
        try (Socket ignored = new Socket("localhost", port)) {
            ignored.close();
            return true;
        } catch (IOException ignored) {
            return false;
        }
    }
}
