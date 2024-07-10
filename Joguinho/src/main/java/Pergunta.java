public class Pergunta {
    private String pergunta;
    private String respostaCorreta;

    public Pergunta(String pergunta, String respostaCorreta) {
        this.pergunta = pergunta;
        this.respostaCorreta = respostaCorreta;
    }

    public String getPergunta() {
        return pergunta;
    }

    public String getRespostaCorreta() {
        return respostaCorreta;
    }
}
