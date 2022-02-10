package com.farmaciasperuanas.pmmli.monitor.service;

import com.farmaciasperuanas.pmmli.monitor.aspect.security.JwtTokenProvider;
import com.farmaciasperuanas.pmmli.monitor.dto.DataResponseDTO;
import com.farmaciasperuanas.pmmli.monitor.dto.ResponseDto;
import com.farmaciasperuanas.pmmli.monitor.dto.user.UserSignInResponseDTO;
import com.farmaciasperuanas.pmmli.monitor.entity.ProfileUser;
import com.farmaciasperuanas.pmmli.monitor.entity.UserAccess;
import com.farmaciasperuanas.pmmli.monitor.helper.FormatMessageError;
import com.farmaciasperuanas.pmmli.monitor.repository.ProfileUserRepository;
import com.farmaciasperuanas.pmmli.monitor.repository.UserAccessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAccessServiceImpl implements UserAccessService {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserAccessRepository userAccessRepository;
    @Autowired
    ProfileUserRepository profileUserRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public ResponseDto<UserAccess> saveUser(String username, String password, String name, String email, Long profileId) {
        ResponseDto<UserAccess> res = new ResponseDto<>();
        try{
            Optional<ProfileUser> prof = profileUserRepository.findById(profileId);

            UserAccess ua = new UserAccess();
            ua.setEmail(email);
            ua.setName(name);
            ua.setUsername(username);
            ua.setPassword(passwordEncoder.encode(password));
            ua.setState(0L);
            if (prof.isPresent()) {
                ua.setProfileUser(prof.get());
            }
            UserAccess saved = userAccessRepository.save(ua);
            res.setCode(HttpStatus.OK.value());
            res.setStatus(true);
            res.setBody(saved);
            res.setMessage("Usuario registrado exitosamente");
        } catch (Exception e){
            res.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            res.setStatus(false);
            res.setBody(null);
            res.setMessage(FormatMessageError.getErrorMessage(e));
        }

        return res;
    }

    @Override
    public DataResponseDTO<UserSignInResponseDTO> login(String username, String password) {
        DataResponseDTO<UserSignInResponseDTO> response = new DataResponseDTO<>();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            String token = jwtTokenProvider.createToken(username, userAccessRepository.findByUsername(username).getProfileUser());
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Inicio de sesión exitoso");
            response.setData(new UserSignInResponseDTO(token));
        } catch (AuthenticationException e) {
            response.setStatus(HttpStatus.NON_AUTHORITATIVE_INFORMATION.value());
            response.setMessage("Usuario y/o contraseña incorrectos");
            response.setData(new UserSignInResponseDTO());
        }

        return response;
    }
}
