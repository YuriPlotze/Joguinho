import java.io.*;
import java.net.*;

public class Jogador implements Runnable {
    private Socket socket;
    private int id;
    private int posicao;
    private int posicaoAnterior;

    public Jogador(Socket socket, int id) {
        this.socket = socket;
        this.id = id;
        this.posicao = 0;
    }

    public Socket getSocket() {
        return socket;
    }

    public int getId() {
        return id;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicaoAnterior = this.posicao;
        this.posicao = posicao;
    }

    public int getPosicaoAnterior() {
        return posicaoAnterior;
    }

    public void reverterPosicao() {
        this.posicao = this.posicaoAnterior;
    }

    @Override
    public void run() {
        try {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("Bem-vindo, Jogador " + id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
