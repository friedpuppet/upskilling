package ua.yelisieiev;

public class Starter {
    public static void main(String[] args) {
        int port = 0;
        String contentPath = null;
        try {
//            System.out.println(args[0]);
            port = Integer.parseInt(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {

        }
        try {
//            System.out.println(args[1]);
            contentPath = args[1];
        } catch (ArrayIndexOutOfBoundsException e) {

        }

        FileServer fileServer = new FileServer();

        if (port != 0) {
            fileServer.setPort(port);
        }
        if (contentPath != null) {
            fileServer.setContentPath(contentPath);
        }

        fileServer.start();

//        try {
//            fileServer.start();
//            Thread.sleep(300_000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            fileServer.stop();
//        }
    }

    private static void printUsage() {
        String usage = """
                asdsadasd
                """;
        System.out.println(usage);
    }

}
