  
package ar.com.ada.api.aladas.models.request;

import java.util.Date;

import ar.com.ada.api.aladas.entities.Pais.TipoDocuEnum;
import ar.com.ada.api.aladas.entities.Usuario.TipoUsuarioEnum;

/**
 * RegistrationRequest
 */
public class RegistrationRequest {

    public String fullName; // Nombre persona
    public int country; // pais del usuario
    public TipoDocuEnum identificationType; // Tipo Documento
    public String identification; // nro documento
    public Date birthDate; // fechaNacimiento
    public String email; // email
    public TipoUsuarioEnum userType;
    public String password; // contraseña elegida por el usuario.

}