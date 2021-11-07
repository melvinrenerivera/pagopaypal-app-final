package pe.todotic.taller_sba.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pe.todotic.taller_sba.model.Usuario;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AppUserDetailsModel implements UserDetails {

    private final Integer id;
    private final String name;
    private final String username;
    private final String password;
    private final String rol;
    private final List<GrantedAuthority> grantedAuthorityList;

    public AppUserDetailsModel(Usuario usuario){
        id = usuario.getId();;
        name = usuario.getNombres();
        username = usuario.getEmail();
        password = usuario.getPassword();
        rol = usuario.getRol().name();
        grantedAuthorityList = Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+usuario.getRol())); // pueden ser roles o permisos
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorityList;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRol() {
        return rol;
    }
}
