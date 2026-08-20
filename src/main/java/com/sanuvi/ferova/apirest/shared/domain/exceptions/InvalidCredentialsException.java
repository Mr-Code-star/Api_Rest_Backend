package com.sanuvi.ferova.apirest.shared.domain.exceptions;

/**
 * Excepción lanzada cuando las credenciales de autenticación son inválidas
 *
 * <p>
 *     Casos de uso típicos:
 *     <ul>
 *         <li>Contraseña incorrecta durante el inicio de sesión</li>
 *         <li>Usuario no encontrado en la base de datos</li>
 *         <li>Credenciales expiradas o inválidas</li>
 *         <li>Token JWT inválido o expirado</li>
 *     </ul>
 * </p>
 *
 * <p>
 *     Esta excepción extiende {@link RuntimeException}, por lo que no requiere
 *     ser declarada en la firma de los métodos ni manejada con try-catch
 *     de forma obligatoria (excepción no verificada).
 *
 */
public class InvalidCredentialsException extends RuntimeException {
    /**
     * Constructor que recibe un mensaje descriptivo del error
     *
     * <p>
     *     Este constructor permite personalizar el mensaje de error
     *     que se mostrará cuando se lance la excepción.
     * </p>
     *
     * <p>
     *     Ejemplos de mensajes:
     *     <ul>
     *         <li>"Credenciales inválidas"</li>
     *         <li>"Usuario o contraseña incorrectos"</li>
     *         <li>"Token JWT expirado"</li>
     *         <li>"Usuario no encontrado con DNI: 12345678"</li>
     *     </ul>
     * </p>
     *
     * @param message mensaje descriptivo del error
     */
    public InvalidCredentialsException(String message) {
        super(message);
    }
}