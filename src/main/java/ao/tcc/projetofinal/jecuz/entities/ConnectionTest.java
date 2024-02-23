package ao.tcc.projetofinal.jecuz.entities;

import lombok.*;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ConnectionTest implements Serializable{

    private InetAddress address;
    private String dateCreateAt;

    public static ConnectionTest test() throws UnknownHostException {

        return ConnectionTest
                .builder()
                .address(InetAddress.getLocalHost())
                .dateCreateAt(new Date().toString())
                .build();
    }
}
