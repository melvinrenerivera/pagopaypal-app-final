package pe.todotic.taller_sba.config;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//https://github.com/paypal/Checkout-Java-SDK

@Configuration
public class PaypalConfig {

    // creo un bean para que este disponible en todo el proyecto
    @Bean
    PayPalHttpClient payPalHttpClient(){
        // Creating a sandbox environment
        String clientId = "AXylr0Mbc__DRBnhFlIojYbyckap80jO9KR_TBh0cjJEPCP6qE3BdcAYmEwUETz7fKGKgb9fSBP3zKI3";
        String secret ="EC1GlJy0cyNN6q0cwqu7gR7XRpAE_dxZqrOlOe-c3miDP_LunH5x2xnjKmOfgOQ0jyhgwF0izAxEwVfz";
        PayPalEnvironment environment = new PayPalEnvironment.Sandbox(clientId, secret);
        return new PayPalHttpClient(environment);
    }
}
