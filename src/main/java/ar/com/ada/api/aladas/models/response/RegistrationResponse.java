package ar.com.ada.api.aladas.models.response;

import java.util.*;

import ar.com.ada.api.aladas.models.request.ErrorItemInfo;

/**
 * RegistrationResponse
 */
public class RegistrationResponse {

    public boolean isOk = false;
    public String message = "";
    public Integer userId;
    public List<ErrorItemInfo> errors = new ArrayList<>();
}