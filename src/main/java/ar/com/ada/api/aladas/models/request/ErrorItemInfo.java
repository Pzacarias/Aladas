package ar.com.ada.api.aladas.models.request;

public class ErrorItemInfo {
    public ErrorItemInfo(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String field;
    public String message;
}
