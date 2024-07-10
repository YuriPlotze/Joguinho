import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {
    private static List<Jogador> jogadores = new ArrayList<>();
    private static final int PORT = 12345;
    private static Map<Integer, Pergunta> perguntas = new HashMap<>();

    public static void main(String[] args) {
        adicionarPerguntas();
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor esperando por conexões...");
            while (jogadores.size() < 2) {
                Socket socket = serverSocket.accept();
                System.out.println("Jogador conectado: " + socket.getInetAddress());
                Jogador jogador = new Jogador(socket, jogadores.size() + 1);
                jogadores.add(jogador);
                new Thread(jogador).start();
            }
            System.out.println("Dois jogadores conectados. Iniciando o jogo...");
            iniciarJogo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void adicionarPerguntas() {
        perguntas.put(1, new Pergunta("O céu é vermelho?", "F"));
        perguntas.put(2, new Pergunta("A água é molhada?", "V"));
        perguntas.put(3, new Pergunta("A Terra é plana?", "F"));
        perguntas.put(4, new Pergunta("Os pássaros podem voar?", "V"));
        perguntas.put(5, new Pergunta("Peixes vivem fora d'água?", "F"));
        perguntas.put(6, new Pergunta("O sol nasce no oeste?", "F"));
        perguntas.put(7, new Pergunta("A lua é feita de queijo?", "F"));
        perguntas.put(8, new Pergunta("2 + 2 é igual a 4?", "V"));
        perguntas.put(9, new Pergunta("A velocidade da luz é constante?", "V"));
        perguntas.put(10, new Pergunta("O fogo é frio?", "F"));
        perguntas.put(11, new Pergunta("As plantas fazem fotossíntese?", "V"));
        perguntas.put(12, new Pergunta("Os humanos têm três pernas?", "F"));
        perguntas.put(13, new Pergunta("Os gatos miam?", "V"));
        perguntas.put(14, new Pergunta("A água ferve a 100°C?", "V"));
        perguntas.put(15, new Pergunta("A Terra tem dois sóis?", "F"));
        perguntas.put(16, new Pergunta("Os computadores podem pensar?", "F"));
        perguntas.put(17, new Pergunta("Os carros têm quatro rodas?", "V"));
        perguntas.put(18, new Pergunta("As baleias são peixes?", "F"));
        perguntas.put(19, new Pergunta("O universo é infinito?", "V"));
        perguntas.put(20, new Pergunta("A gravidade faz os objetos caírem?", "V"));
    }

    private static void iniciarJogo() {
        Random random = new Random();
        boolean jogoEmAndamento = true;

        while (jogoEmAndamento) {
            for (Jogador jogador : jogadores) {
                try {
                    DataOutputStream out = new DataOutputStream(jogador.getSocket().getOutputStream());
                    DataInputStream in = new DataInputStream(jogador.getSocket().getInputStream());

                    System.out.println("Vez do jogador " + jogador.getId());
                    out.writeUTF("Vez do jogador " + jogador.getId());
                    out.writeUTF("Pressione Enter para jogar o dado.");
                    in.readUTF();

                    int dado = random.nextInt(6) + 1;
                    System.out.println("Jogador " + jogador.getId() + " tirou: " + dado);
                    out.writeUTF("Você tirou: " + dado);

                    int novaPosicao = jogador.getPosicao() + dado;
                    if (novaPosicao >= 20) {
                        novaPosicao = 20;
                    }

                    out.writeUTF("Você se moveu para a casa: " + novaPosicao);
                    jogador.setPosicao(novaPosicao);

                    Pergunta pergunta = perguntas.get(novaPosicao);
                    out.writeUTF(pergunta.getPergunta() + " (V/F)");
                    String resposta = in.readUTF();

                    if (resposta.equalsIgnoreCase(pergunta.getRespostaCorreta())) {
                        out.writeUTF("Resposta correta! Você permanece na casa " + novaPosicao);
                    } else {
                        out.writeUTF("Resposta errada! Você volta para a casa " + jogador.getPosicaoAnterior());
                        jogador.reverterPosicao();
                    }

                    if (novaPosicao == 20) {
                        out.writeUTF("Você venceu!");
                        jogoEmAndamento = false;
                        break;
                    }

                    for (Jogador p : jogadores) {
                        DataOutputStream pOut = new DataOutputStream(p.getSocket().getOutputStream());
                        pOut.writeUTF("Jogador " + jogador.getId() + " está na casa " + jogador.getPosicao());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        for (Jogador jogador : jogadores) {
            try {
                DataOutputStream out = new DataOutputStream(jogador.getSocket().getOutputStream());
                out.writeUTF("Servidor encerrado. Obrigado por jogar!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
