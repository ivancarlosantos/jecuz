package ao.tcc.projetofinal.jecuz.test;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
 @Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ConnectionTest implements Serializable {

    private String owner;
    private InetAddress address;
    private String dateCreateAt;

    public static ConnectionTest test() throws UnknownHostException {

        ConnectionTest.log.debug("OUT-ConnectionTest-gerarConnectionTest()");
        return ConnectionTest.builder()
                             .owner(InetAddress.getLocalHost().getHostName())
                             .address(InetAddress.getByName(InetAddress.getLocalHost().getHostAddress()))
                             .dateCreateAt(new Date().toString())
                             .build();
    }
}
