package com.sanuvi.ferova.apirest.iam.infrastructure.email.resend.services;

import com.sanuvi.ferova.apirest.iam.application.internal.outboundservices.email.EmailService;
import com.sanuvi.ferova.apirest.iam.infrastructure.email.resend.ResendClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Implementación del servicio de email utilizando Resend
 * <p>
 *     Esta clase implementa la interfaz {@link EmailService} utilizando
 *     el cliente de Resend para enviar correos electrónicos.
 * </p>
 *
 * <p>
 *     Resend es un servicio de envío de emails moderno que proporciona:
 *     - API REST fácil de usar
 *     - Alta capacidad de entrega
 *     - Seguimiento de emails
 *     - Plantillas HTML y texto plano
 * </p>
 *
 * @author Equipo de Desarrollo
 * @version 1.0
 * @see EmailService
 * @see ResendClient
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    /**
     * Cliente de Resend para enviar emails
     */
    private final ResendClient resendClient;

    /**
     * Email del remitente
     */
    @Value("${email.from}")
    private String emailFrom;

    /**
     * Nombre del remitente
     */
    @Value("${email.from-name:Ferova}")
    private String fromName;

    /**
     * Envía un código de verificación para restablecer la contraseña
     *
     * <p>
     *     Este método construye un email HTML con formato profesional
     *     que incluye el código de verificación de 6 dígitos y las
     *     instrucciones para restablecer la contraseña.
     * </p>
     *
     * @param email la dirección de correo electrónico del destinatario
     * @param code el código de verificación de 6 dígitos
     * @throws RuntimeException si ocurre un error al enviar el correo
     */
    @Override
    public void sendResetCode(String email, String code) {
        log.info("Enviando código de restablecimiento a: {}", email);

        try {
            // Construir el contenido HTML del email
            String htmlContent = buildResetCodeEmailHtml(code);
            String textContent = buildResetCodeEmailText(code);

            // Enviar el email usando Resend
            resendClient.sendEmail(
                    fromName + " <" + emailFrom + ">",
                    email,
                    "Ferova - Código de Restablecimiento de Contraseña",
                    htmlContent,
                    textContent
            );

            log.info("Email enviado exitosamente a: {}", email);

        } catch (Exception e) {
            log.error("Error al enviar email a: {} - {}", email, e.getMessage());
            throw new RuntimeException("No se pudo enviar el código de restablecimiento", e);
        }
    }

    /**
     * Construye el contenido HTML del email
     *
     * @param code el código de verificación
     * @return el contenido HTML del email
     */
    private String buildResetCodeEmailHtml(String code) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
            </head>
            <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px;">
                <div style="max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; padding: 40px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);">
                    <div style="text-align: center; margin-bottom: 30px;">
                        <h1 style="color: #4F46E5; font-size: 28px; margin: 0;">Ferova</h1>
                        <p style="color: #6B7280; font-size: 16px; margin: 5px 0 0;">Recuperación de Contraseña</p>
                    </div>
                    
                    <div style="background-color: #F9FAFB; border-radius: 6px; padding: 20px; margin-bottom: 25px;">
                        <p style="color: #374151; font-size: 16px; margin: 0 0 10px 0;">
                            Hemos recibido una solicitud para restablecer la contraseña de tu cuenta.
                        </p>
                        <p style="color: #374151; font-size: 16px; margin: 0;">
                            Tu código de verificación es:
                        </p>
                    </div>
                    
                    <div style="text-align: center; margin: 30px 0;">
                        <div style="display: inline-block; background-color: #EEF2FF; padding: 15px 40px; border-radius: 8px; border: 2px solid #4F46E5;">
                            <span style="color: #4F46E5; font-size: 36px; font-weight: bold; letter-spacing: 8px;">
                                %s
                            </span>
                        </div>
                    </div>
                    
                    <div style="background-color: #FEF3C7; border-left: 4px solid #F59E0B; padding: 15px; margin-bottom: 25px; border-radius: 4px;">
                        <p style="color: #92400E; font-size: 14px; margin: 0;">
                            ⚠️ Este código expirará en <strong>10 minutos</strong>.
                        </p>
                    </div>
                    
                    <div style="border-top: 1px solid #E5E7EB; padding-top: 20px; margin-top: 20px;">
                        <p style="color: #6B7280; font-size: 12px; margin: 0 0 5px 0;">
                            Si no solicitaste este cambio, ignora este mensaje.
                        </p>
                        <p style="color: #9CA3AF; font-size: 12px; margin: 0;">
                            Este es un mensaje automático, por favor no respondas a este correo.
                        </p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(code);
    }

    /**
     * Construye el contenido en texto plano del email
     *
     * @param code el código de verificación
     * @return el contenido en texto plano del email
     */
    private String buildResetCodeEmailText(String code) {
        return """
            Ferova - Recuperación de Contraseña
            
            Hemos recibido una solicitud para restablecer la contraseña de tu cuenta.
            
            Tu código de verificación es: %s
            
            Este código expirará en 10 minutos.
            
            Si no solicitaste este cambio, ignora este mensaje.
            
            ---
            Este es un mensaje automático, por favor no respondas a este correo.
            """.formatted(code);
    }
}
