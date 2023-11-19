package net.lopymine.betteranvil.resourcepacks;

public class ServerResourcePackManager {
    private static String server = "";

    public static String getServer() {
        return ServerResourcePackManager.server;
    }

    public static void setServer(String server) {
        if (server == null) throw new IllegalArgumentException("Trying set null server");
        ServerResourcePackManager.server = server;
    }

}
