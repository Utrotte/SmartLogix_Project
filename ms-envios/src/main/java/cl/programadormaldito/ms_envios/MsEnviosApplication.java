package cl.programadormaldito.ms_envios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

// Microservicio de envíos — gestiona despacho, transportistas y seguimiento de paquetes
@SpringBootApplication
@EnableFeignClients // habilita clientes Feign para comunicación HTTP
@EnableDiscoveryClient // se registra automáticamente en el servidor Eureka
public class MsEnviosApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsEnviosApplication.class, args);
    }

}
