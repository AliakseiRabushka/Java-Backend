package recipes.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import recipes.converter.UserConverter;
import recipes.dto.UserDto;
import recipes.repository.UserRepository;

@Service
@RequiredArgsConstructor

public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    public void save(UserDto userDto){
        UserConverter.toEntity(userDto)
                .ifPresent(userRepository::save);
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .flatMap(UserConverter::toDto)
                .orElseThrow(()-> new UsernameNotFoundException("not found"));
    }
}
