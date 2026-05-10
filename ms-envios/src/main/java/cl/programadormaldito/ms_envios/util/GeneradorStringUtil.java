package cl.programadormaldito.ms_envios.util;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// clase utilitaria — no se instancia, solo tiene métodos estáticos
public final class GeneradorStringUtil {

    private static final String CARACTERES_ALFANUMERICOS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int LARGO_ALEATORIO = 32;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("ddMMyyyy");
    private static final SecureRandom RANDOM = new SecureRandom(); // SecureRandom para mayor aleatoriedad

    private GeneradorStringUtil() {}

    // genera un ID de 40 caracteres: 32 aleatorios + fecha actual (ddMMyyyy)
    public static String generarID() {
        StringBuilder stringGenerado = new StringBuilder(40);

        for (int i = 0; i < LARGO_ALEATORIO; i++) {
            int indice = RANDOM.nextInt(CARACTERES_ALFANUMERICOS.length());
            stringGenerado.append(CARACTERES_ALFANUMERICOS.charAt(indice));
        }

        // el sufijo de fecha ayuda a saber cuándo fue creado el registro
        stringGenerado.append(LocalDate.now().format(FORMATO_FECHA));

        return stringGenerado.toString();
    }

    // genera un código legible para el cliente: ENV-[10chars]-[yyyyMMdd]
    public static String generarCodigoEnvio() {
        StringBuilder codigo = new StringBuilder("ENV-");

        for (int i = 0; i < 10; i++) {
            int indice = RANDOM.nextInt(CARACTERES_ALFANUMERICOS.length());
            codigo.append(CARACTERES_ALFANUMERICOS.charAt(indice));
        }

        codigo.append("-").append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        return codigo.toString();
    }
}
