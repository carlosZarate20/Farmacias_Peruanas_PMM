package com.farmaciasperuanas.pmmli.monitor.service;

import com.farmaciasperuanas.pmmli.monitor.dto.ResponseDto;
import com.farmaciasperuanas.pmmli.monitor.entity.ConfigMonitor;
import com.farmaciasperuanas.pmmli.monitor.helper.FormatMessageError;
import com.farmaciasperuanas.pmmli.monitor.repository.ConfigMonitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    MailService mailService;

    @Autowired
    ConfigMonitorRepository configMonitorRepository;

    @Override
    public ResponseDto<String> testEmail(String subject, String to, String body) {
        ResponseDto<String> res = new ResponseDto<>();
        try {
            mailService.sendEmail(subject, to, body);
            res.setCode(HttpStatus.OK.value());
            res.setStatus(true);
            res.setBody("Exitoso");
            res.setMessage("Envio de correo exitoso");
        } catch (Exception e) {
            res.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            res.setStatus(false);
            res.setBody(null);
            res.setMessage(FormatMessageError.getErrorMessage(e));
        }

        return res;
    }

    @Override
    public ConfigMonitor getInfo() {
        List<ConfigMonitor> cg = configMonitorRepository.findAll();
        ConfigMonitor response = new ConfigMonitor();
        if (cg.size() > 0) {
            response = cg.get(0);
        }
        return response;
    }

    @Override
    public ResponseDto<String> saveConfig(Long id, String email) {
        ResponseDto<String> res = new ResponseDto<>();
        try {
            if (id == null) {
                ConfigMonitor newConfig = new ConfigMonitor();
                newConfig.setEmail(email);
                configMonitorRepository.save(newConfig);
                res.setCode(HttpStatus.OK.value());
                res.setStatus(true);
                res.setBody("Exitoso");
                res.setMessage("Configuración realizada exitosamente");
            } else {
                Optional<ConfigMonitor> cg = configMonitorRepository.findById(id);
                if (cg.isPresent()) {
                    ConfigMonitor finded = cg.get();
                    finded.setEmail(email);
                    configMonitorRepository.save(finded);
                    res.setCode(HttpStatus.OK.value());
                    res.setStatus(true);
                    res.setBody("Exitoso");
                    res.setMessage("Configuración realizada exitosamente");
                } else {
                    res.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    res.setStatus(false);
                    res.setBody("Error");
                    res.setMessage("No se encontró la configuración enviada");
                }
            }

        } catch (Exception e) {
            res.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            res.setStatus(false);
            res.setBody(null);
            res.setMessage(FormatMessageError.getErrorMessage(e));
        }

        return res;
    }
}
