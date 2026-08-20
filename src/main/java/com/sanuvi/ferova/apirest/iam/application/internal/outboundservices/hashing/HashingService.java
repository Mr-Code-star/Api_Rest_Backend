package com.sanuvi.ferova.apirest.iam.application.internal.outboundservices.hashing;

/**
 * Servicio para el hashing y verificación de contraseñas
 * <p>
 *     Esta interfaz define las operaciones necesarias para codificar
 *     (hashear) contraseñas y verificar si una contraseña en texto plano
 *     coincide con su versión hasheada almacenada.
 * </p>
 */
public interface HashingService {

    /**
     * Codifica (hashea) una contraseña en texto plano
     *
     *     Este método toma una contraseña en texto plano y la convierte
     *     en un hash seguro utilizando un algoritmo de hashing con sal.
     *     El resultado es una cadena que contiene el hash y la sal utilizada.
     * @param rawPassword la contraseña en texto plano a codificar
     * @return la contraseña hasheada como String
     */
    String encode (CharSequence rawPassword);

    /**
     * Verifica si una contraseña en texto plano coincide con su hash almacenado
     *
     * <p>
     *     Este método compara una contraseña en texto plano con un hash
     *     almacenado previamente, extrayendo la sal y el factor de costo
     *     del hash para realizar la verificación.
     * </p>
     *
     * @param rawPassword la contraseña en texto plano a verificar
     * @param encodedPassword el hash almacenado contra el cual verificar
     * @return true si la contraseña coincide con el hash, false en caso contrario
     */
    boolean matches(CharSequence rawPassword, String encodedPassword);
}
