package ar.com.ada.api.aladas.sistema.payments;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ar.com.ada.api.aladas.entities.Reserva;
import ar.com.ada.api.aladas.models.response.ReservaGenerationResponse;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

@Service
public class MercadoPagoService {

    @Value("${mercadoPago.publicKey}")
    private String publicKey;

    @Value("${mercadoPago.accessToken}")
    private String accessToken;

    public ReservaGenerationResponse generarPreferenciaParaReserva(Reserva reserva) {
        
        
        //tenemos que crear el CreatePreference Object
        JSONObject preferencia = generarPreferencia(reserva);
        
        //pegarle a la API con la preferencia
        String urlAPI = "https://api.mercadopago.com/checkout/preferences?access_token="+accessToken;

        //aqui llamamos a la API
        HttpResponse<JsonNode> response = Unirest.request("POST", urlAPI)
            .header("content-type", "application/json")
            .body(preferencia).asJson();

        //leer el Init_point
        String initPoint = response.getBody().getObject().get("init_point").toString();

        ReservaGenerationResponse rta = new ReservaGenerationResponse();

        rta.isOk = response.isSuccess();
        rta.reservaId = reserva.getReservaId();
        rta.message = "Preferencia creada";
        rta.urlPago = initPoint;

        return rta;

    }

    public JSONObject generarPreferencia(Reserva reserva){
/*
        {
"items": [
    {
      "title": "Dummy Title",
      "description": "Dummy description",
      "picture_url": "http://www.myapp.com/myimage.jpg",
      "category_id": "cat123",
      "quantity": 1,
      "currency_id": "U$",
      "unit_price": 10
    }
  ],
  "payer": {
    "phone": {},
    "identification": {},
    "address": {}
  },
  "payment_methods": {
    "excluded_payment_methods": [
      {}
    ],
    "excluded_payment_types": [
      {}
    ]
  },
  "shipments": {
    "free_methods": [
      {}
    ],
    "receiver_address": {}
  },
  "back_urls": {},
  "differential_pricing": {},
  "tracks": [
    {
      "type": "google_ad"
    }
  ]
}

        */

        JSONObject joPreference = new JSONObject();
        JSONArray joItems = new JSONArray();
        JSONObject joItem = new JSONObject();
        
        //jo["title"] = "Reserva nro...";
        // joItem = { title: "Reserva nro "}
        
        joItem.put("title", "Reserva #"+ reserva.getReservaId());
        joItem.put("descripcion", "Reserva generada desde Aladas");
        joItem.put("quantity",1);
        joItem.put("unit_price", reserva.getVuelo().getPrecio());
        joItem.put("currency_id", reserva.getVuelo().getCodigoMoneda());

        //agrego el Item a la coleccion de items
        joItems.put(joItem);

        //pongo en el nodo Items, el array de items.
        joPreference.put("transaction_amount", reserva.getVuelo().getPrecio());
        joPreference.put("currency_id", reserva.getVuelo().getCodigoMoneda());
        //cuotas
        joPreference.put("installments",1);
        
        joPreference.put("items", joItems);

        JSONObject joPayer = new JSONObject();
        joPayer.put("first_name", reserva.getPasajero().getNombre());
        joPayer.put("email", reserva.getPasajero().getUsuario().getEmail());

        joPreference.put("payer", joPayer);

        joPreference.put("external_id", reserva.getEstadoReservaId()); //, "RESERVA" +reserva.getReservaId());
        //joPreference.put("auto_return", "all"); //hace que redirija al sitio del cliente, en cualquier estado de pago(rechazo, pendiente, aprobado)

        return joPreference;
    }
    
}
