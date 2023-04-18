package mil.decea.mentorpgapi.domain.user;

import mil.decea.mentorpgapi.domain.DTOValidator;
import mil.decea.mentorpgapi.domain.user.dto.UserBasicDTO;
import mil.decea.mentorpgapi.domain.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private List<DTOValidator<UserDTO>> validators;

    public UserBasicDTO save(UserDTO userDTO){

        validators.forEach(v -> v.validate(userDTO));

        User user = userRepository.save(new User(userDTO));

        return new UserBasicDTO(user);
    }
    @Override
    public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
        return userRepository.findByCpf(cpf);
    }
}
