import com.google.gson.Gson;
import commands.CommandsWorker;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

// класс сервера, который при запуске будет обрабатывать запросы клиента и выводить их на экран
public class Server {
    private static CommandsWorker cw;

    static {
        try {
            cw = new CommandsWorker("dragons.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Server() {
    }

    public static void main(String[] args) throws IOException, InterruptedException {
// while (true) {
// ServerSocket ss = new ServerSocket(40003); // создаём класс сервера, который будет работать с получением и обработкой запросов по порту 40001
// Socket s = ss.accept();
// Scanner in = new Scanner(s.getInputStream());
// PrintWriter pw = new PrintWriter(s.getOutputStream());
// String console = serverWork();
// if (console.equals("")) {
// while (in.hasNext()) {
//// System.out.print("Client: ");
// PacketForSend temp = new Gson().fromJson(in.nextLine(), PacketForSend.class);
// String arguments = temp.open();
//// System.out.println(arguments);
//// System.out.print("Answer: ");
// String result = cw.work(arguments);
// pw.println(result);
// }
// } else {
// String result = cw.work(console);
// pw.println(result);
// }
// pw.flush();
// pw.close();
// in.close();
// s.close();
// ss.close();
// }
        ServerSocketChannel ssc = ServerSocketChannel.open(); // открываем канал
        ssc.bind(new InetSocketAddress(40003)); // приделываем канал к порту
        ssc.configureBlocking(false); // делаем канал неблокирующим
        Selector selector = Selector.open(); // открываем работу селектора
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("Selector server is ready");
        String console = "";
        while (true) {
            console = consoleEntering();
            if (console.equals("save")) {
                System.out.println(cw.work(console));
                console = "";
            }
            if (selector.selectNow() == 0) { // если селектор не получил никаких соединений -
                continue; // - переходим к следующему шагу итерации
            }
            Set<SelectionKey> keys = selector.selectedKeys(); // получаем сет с ключами
            Iterator<SelectionKey> iterator = keys.iterator(); // вытаскиваем итератор
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next(); // вытаскиваем ключ из множества ключей
                if (key.isAcceptable()) { // проверяем, что тип соединения "соединяющий"
                    SocketChannel s = ssc.accept(); // получаем сокетный канал
                    System.out.println("Client connected: " + s);
                    s.configureBlocking(false); // делаем канал неблокирующим
                    s.register(selector, SelectionKey.OP_READ); // регистрируем канал как канал для чтения
                }
                if (key.isReadable()) { // проверяем, что тип соединения "читающий"
                    SocketChannel s = (SocketChannel) key.channel(); // получаем сокетный канал самого ключа
                    ByteBuffer buffer = ByteBuffer.allocate(1024); // создаём буфер
                    int readResult = s.read(buffer);
                    if (readResult > 0) {// если результат чтения больше ноля (ноль - читать нечего, -1 - мы достигли конца)
                        String messageFromClient = new String(buffer.array(), 0, readResult, StandardCharsets.UTF_8); // считываем содержимое буфера
                        PacketForSend temp = new Gson().fromJson(messageFromClient, PacketForSend.class);
                        String arguments = temp.open();
                        System.out.println("Info from client: " + arguments);
                        String messageToClientFromDragons = cw.work(arguments);
                        s.write(buffer.wrap(messageToClientFromDragons.getBytes(StandardCharsets.UTF_8)));
                        s.close();
                    }
                }
                iterator.remove();
            }
        }
    }

    private static String consoleEntering() throws InterruptedException, IOException {
        Thread.sleep(10);
        int i;
        String s = "";
        while (System.in.available() != 0 && (i = System.in.read()) != 10) {
            s += (char) i;
        }
        return s;
    }
}