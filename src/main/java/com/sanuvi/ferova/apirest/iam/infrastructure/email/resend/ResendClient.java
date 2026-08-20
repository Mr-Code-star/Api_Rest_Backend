package com.sanuvi.ferova.apirest.iam.infrastructure.email.resend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Cliente para la API de Resend
 * <p>
 *     Esta clase proporciona un cliente para interactuar con la API
 *     de Resend y enviar correos electrónicos.
 * </p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ResendClient {

    /**
     * URL base de la API de Resend
     */
    private static final String RESEND_API_URL = "https://api.resend.com/emails";

    /**
     * API Key de Resend
     */
    @Value("${resend.api.key}")
    private String apiKey;

    /**
     * RestTemplate para hacer peticiones HTTP
     */
    private final RestTemplate restTemplate;

    /**
     * ObjectMapper para manejar JSON
     */
    private final ObjectMapper objectMapper;

    /**
     * Envía un correo electrónico usando la API de Resend
     *
     * @param from el remitente del correo (ej: "Ferova <onboarding@resend.dev>")
     * @param to el destinatario del correo
     * @param subject el asunto del correo
     * @param html el contenido HTML del correo
     * @param text el contenido en texto plano del correo
     * @throws RuntimeException si ocurre un error al enviar el correo
     */
    public void sendEmail(String from, String to, String subject, String html, String text) {
        log.debug("Enviando email a: {}", to);

        try {
            // Construir el payload
            EmailPayload payload = new EmailPayload(from, to, subject, html, text);
            String jsonPayload = objectMapper.writeValueAsString(payload);

            // Configurar headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            // Crear la petición
            HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

            // Enviar la petición
            ResponseEntity<String> response = restTemplate.exchange(
                    RESEND_API_URL,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            // Verificar respuesta
            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode responseJson = objectMapper.readTree(response.getBody());
                String id = responseJson.path("id").asText();
            } else {
                throw new RuntimeException("Error al enviar email: " + response.getStatusCode());
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al enviar email", e);
        }
    }

    /**
     * Record para el payload del email
     */
    private record EmailPayload(
            String from,
            String to,
            String subject,
            String html,
            String text
    ) {}
}