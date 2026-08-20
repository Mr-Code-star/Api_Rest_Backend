package com.sanuvi.ferova.apirest.iam.application.internal.outboundservices.email;


/**
 * Servicio para el envío de correos electrónicos
 * <p>
 *     Esta interfaz define las operaciones necesarias para enviar
 *     correos electrónicos relacionados con la autenticación y
 *     gestión de usuarios.
 * </p>
 */
public interface EmailService {
    /**
     * Envía un código de verificación para restablecer la contraseña
     *
     * @param email la dirección de correo electrónico del destinatario
     * @param code el código de verificación de 6 dígitos
     * @throws RuntimeException si ocurre un error al enviar el correo
     */
    void sendResetCode(String email, String code);
}