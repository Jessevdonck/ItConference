package service;

import domain.Gebruiker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import repository.GebruikerRepository;
import utils.GebruikerRol;

import java.util.Collection;
import java.util.Collections;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private GebruikerRepository gebruikerRepository;

    @Override
    public UserDetails loadUserByUsername(String gebruikersnaam) throws UsernameNotFoundException {
        Gebruiker gebruiker = gebruikerRepository.findByGebruikersnaam(gebruikersnaam);
        if (gebruiker == null) {
            throw new UsernameNotFoundException(gebruikersnaam);
        }

        return new User(
                gebruiker.getGebruikersnaam(),
                gebruiker.getWachtwoord(),
                convertAuthorities(gebruiker.getRol())
        );
    }

    private Collection<? extends GrantedAuthority> convertAuthorities(GebruikerRol rol) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }
}
