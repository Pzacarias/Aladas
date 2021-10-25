package ar.com.ada.api.aladas.models.request;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import ar.com.ada.api.aladas.entities.Pais.TipoDocuEnum;
import ar.com.ada.api.aladas.entities.Usuario.TipoUsuarioEnum;

/**
 * RegistrationRequest
 */
public class RegistrationRequest {

    @NotBlank(message = "Nombre no puede ser nulo o vacio")
    //@NotNull //que no venga nulo
    //@NotEmpty //que no venga vacio
    @Min(3)
    public String fullName; // Nombre persona

    @Positive
    //@ValidCountryCustom //crear tu propia notacion
    public int country; // pais del usuario

    //automaticamente valida contra nuestros enums
    public TipoDocuEnum identificationType; // Tipo Documento

    @NotBlank
    public String identification; // nro documento

    @Past(message = "la fecha de nacimiento no puede ser a futuro") //tiene que ser una fecha en el pasado
    public Date birthDate; // fechaNacimiento

    @Email(message = "el mail es invalido")
    public String email; // email

    public TipoUsuarioEnum userType;

    @NotBlank(message =  "la contraseña no puede ser vacia")
    @Size(min=8, max=15)
    public String password; // contraseña elegida por el usuario.

}