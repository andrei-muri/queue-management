import model.Server;
import model.Task;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Server server = new Server(10);
        for(int i = 0; i < 4; i++) {
            server.addTask(new Task(2, 3, 4));
            Thread.sleep(200);
        }
        System.out.println(server);
        Thread thread = new Thread(server);
        thread.start();
        server.endThread();
    }
}