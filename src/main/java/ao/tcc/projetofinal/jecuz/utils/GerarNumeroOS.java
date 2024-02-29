package ao.tcc.projetofinal.jecuz.utils;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

public class GerarNumeroOS {

    private GerarNumeroOS() {}

    public static String gerar() {

        Random r = new SecureRandom();
        Integer[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
        String alfa = UUID.randomUUID().toString().toUpperCase().substring(0, 4);
        LocalDate localDate = LocalDate.now();

        int a = r.nextInt(10);
        int b = r.nextInt(10);
        int c = r.nextInt(10);
        int d = r.nextInt(10);

        return alfa + "-" + values[a] + values[b] + values[c] + values[d] + "-" + localDate;
    }
}
