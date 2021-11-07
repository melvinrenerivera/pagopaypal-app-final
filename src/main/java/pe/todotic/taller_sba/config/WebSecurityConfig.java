package pe.todotic.taller_sba.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pe.todotic.taller_sba.security.JWTAuthorizationFilter;
import pe.todotic.taller_sba.security.JWTAuthenticationFilter;
import pe.todotic.taller_sba.security.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;

    public WebSecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = (UserDetailsServiceImpl) userDetailsService;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http
                //.formLogin()  // esto que esta comentado es para una arquitectura monolitica mvc por eso no se usa
                //.permitAll()
                //.loginPage()
                .cors() //nosotros ya configuramos cors y se toma automaticamente, por eso llamamos este metodo para que se llame
                .and()
                .csrf().disable() // lo deshabilito xq habra intercambio de informacion con otros dominios
                .authorizeRequests() //hago las reglas
                .antMatchers("/api/admin/**")
                .hasRole("ADMIN")
                .anyRequest().permitAll() //cualquier otra solicitud
                .and()
                .addFilter(jwtAuthenticationFilter()) //esto es necesario para implementar json webtoken, mediante filtros
                .addFilter(jwtAuthorizationFilter())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // segun los principios de res, no maneja estado por eso activamos eso.

    }

    //configurar algoritmo de encryptacion y el servicio para autenticar el usuario contra la base
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncodere());
    }

    @Bean
    public PasswordEncoder passwordEncodere(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter();
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        jwtAuthenticationFilter.setFilterProcessesUrl("/login");
        //podemos agregar otros validarores por si falla
        return jwtAuthenticationFilter;
    }

    @Bean
    public JWTAuthorizationFilter jwtAuthorizationFilter() throws Exception {
        return new JWTAuthorizationFilter(authenticationManager());
    }
}
