package ar.com.ada.api.aladas;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;

import ar.com.ada.api.aladas.entities.*;
import ar.com.ada.api.aladas.entities.Vuelo.EstadoVueloEnum;
import ar.com.ada.api.aladas.security.Crypto;
import ar.com.ada.api.aladas.services.*;
import ar.com.ada.api.aladas.services.AeropuertoService.ValidacionAeropuertoDataEnum;
import ar.com.ada.api.aladas.services.VueloService.ValidacionVueloDataEnum;


@SpringBootTest
class AladasApplicationTests {

	@Autowired
	VueloService vueloService;

	@Autowired
	AeropuertoService aeropuertoService;

	@Test
	void vueloTestPrecioNegativo() {

		Vuelo vueloConPrecioNegativo = new Vuelo();
		vueloConPrecioNegativo.setPrecio(new BigDecimal(-100));

		// Assert: afirmar
		// afirmar quie sea verdadero: assertFalse
		assertFalse(vueloService.validarPrecio(vueloConPrecioNegativo));

	}

	@Test
	void vueloTestPrecioOk() {

		Vuelo vueloConPrecioOK = new Vuelo();
		vueloConPrecioOK.setPrecio(new BigDecimal(100));

		// Assert: afirmar
		// afirmar quie sea verdadero: assertTrue
		assertTrue(vueloService.validarPrecio(vueloConPrecioOK));

	}

	@Test
	void aeropuertoValidarCodigoIATAOK() {
	
		String codigoIATAOk1 = "EZE";
		String codigoIATAOk2 = "AEP";
		String codigoIATAOk3 = "NQN";
		String codigoIATAOk4 = "N  ";
		String codigoIATAOk5 = "N39";

		assertTrue(aeropuertoService.validarIATA(codigoIATAOk1));
		assertTrue(aeropuertoService.validarIATA(codigoIATAOk2));
		assertTrue(aeropuertoService.validarIATA(codigoIATAOk3));

		assertFalse(aeropuertoService.validarIATA(codigoIATAOk4));
		
	}

	@Test
	void aeropuertoValidarCodigoIATANoOK() {
		// From Florencia Di Felice to Everyone: 07:42 PM
		// el código no debe llevar número y sólo 3 letras, así que habría que limitarlo
		// a eso, no?

	}

	@Test
	void vueloVerificarValidacionAeropuertoOrigenDestino() {
		// En este validar todas las posibilidades de si el aeropuerto
		// origen es igual al de destion o todo lo que se les ocurra
	}

	@Test
	void vueloChequearQueLosPendientesNoTenganVuelosViejos() {

		// Queremos validar que cuando hagamos un metodo que traiga los vuelos actuales
		// para
		// hacer reservas, no haya ningun vuelo en el pasado.
	}

	@Test
	void vueloVerificarCapacidadMinima() {

	}

	@Test
	void vueloVerificarCapadidadMaxima() {

	}

	@Test
	void aeropuertoTestBuscadorIATA() {

	}

	@Test
	void vueloValidarVueloMismoDestionoUsandoGeneral() {
		Vuelo vuelo = new Vuelo();
		vuelo.setPrecio(new BigDecimal(1000));
		vuelo.setEstadoVueloId(EstadoVueloEnum.GENERADO);
		vuelo.setAeropuertoOrigen(116);
		vuelo.setAeropuertoDestino(116);

		assertEquals( ValidacionVueloDataEnum.ERROR_AEROPUERTOS_IGUALES, vueloService.validar(vuelo));
	}

	@Test
	void testearEncriptacion() {

		String contraseñaImaginaria = "pitufosasesinos";
		String contraseñaImaginariaEncriptada = Crypto.encrypt(contraseñaImaginaria, "palabra");

		String contraseñaImaginariaEncriptadaDesencriptada = Crypto.decrypt(contraseñaImaginariaEncriptada, "palabra");

		assertEquals(contraseñaImaginariaEncriptadaDesencriptada, contraseñaImaginaria);
	}

	@Test
	void testearContraseña() {
		Usuario usuario = new Usuario();

		usuario.setUsername("Diana@gmail.com");
		usuario.setPassword("qp5TPhgUtIf7RDylefkIbw==");
		usuario.setEmail("Diana@gmail.com");

		assertFalse(!usuario.getPassword().equals(Crypto.encrypt("AbcdE23", usuario.getUsername().toLowerCase())));

	}

	@Test
	void testearAeropuertoId(){
		Aeropuerto aeropuerto = new Aeropuerto();
		aeropuerto.setAeropuertoId(117);
		aeropuerto.setCodigoIATA("MDZ");
		aeropuerto.setNombre("Mendoza");

		assertEquals(ValidacionAeropuertoDataEnum.ERROR_AEROPUERTO_YA_EXISTE, aeropuertoService.validar(aeropuerto));
	}


	@Test
	void testearAeropuertoCodigoIATA(){
		Aeropuerto aeropuerto = new Aeropuerto();
		aeropuerto.setAeropuertoId(17);
		aeropuerto.setCodigoIATA("  M");
		aeropuerto.setNombre("Mendoza");	

		assertEquals(ValidacionAeropuertoDataEnum.ERROR_CODIGO_IATA, aeropuertoService.validar(aeropuerto));
	}
}