package info.datahorizons.AuthServer.web.rest;


import info.datahorizons.AuthServer.dto.UserDTO;
import info.datahorizons.AuthServer.model.UserAuth;
import info.datahorizons.AuthServer.repository.UserAuthRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/manager")
public class AuthManagerRest {

    private final UserAuthRepository userAuthRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthManagerRest(UserAuthRepository userAuthRepository, PasswordEncoder passwordEncoder) {
        this.userAuthRepository = userAuthRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> create(@RequestBody UserDTO userDTO) {
        Map<String, Object> result = new HashMap<>();
        
        if (userAuthRepository.existsByLoginIgnoreCase(userDTO.getLogin())) {
            throw new RuntimeException("User already exists");
        }

        UserAuth newUser = new UserAuth();
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        newUser.setEmail(userDTO.getLogin().toLowerCase() + "@sistema.com"); // Email fictício se não vier
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        
        // Converte lista de roles para String separada por virgula
        if (userDTO.getRoles() != null) {
            newUser.setRoles(String.join(",", userDTO.getRoles()));
        }
        
        newUser.setTenant(userDTO.getExtra() != null ? (String) userDTO.getExtra().getOrDefault("tenant", "master") : "master");
        newUser.setExtra(userDTO.getExtra());
        
        userAuthRepository.save(newUser);
        
        result.put("result", "OK");
        result.put("message", "User created successfully");
        return result;
    }
}