import java.io.*;
import java.net.*;

public class Cliente {
    private static final String SERVER_ADDRESS = "26.240.31.217";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            System.out.println("Conectado ao servidor.");
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String input;

            while (true) {
                String message = in.readUTF();
                System.out.println(message);

                if (message.contains("Pressione Enter para jogar o dado.")) {
                    input = reader.readLine();
                    out.writeUTF(input);
                } else if (message.contains("(V/F)")) {
                    input = reader.readLine();
                    out.writeUTF(input);
                } else if (message.equals("Você venceu!") || message.equals("Servidor encerrado. Obrigado por jogar!")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
