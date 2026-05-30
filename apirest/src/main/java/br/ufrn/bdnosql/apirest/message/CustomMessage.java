package br.ufrn.bdnosql.apirest.message;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"status", "message", "dado"})
public class CustomMessage {
    private int status;
    private String message;
    private Object dado;

    public CustomMessage(int value, String mensagem, Object resposta) {
        status = value;
        message = mensagem;
        dado = resposta;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getDado() {
        return dado;
    }
}
