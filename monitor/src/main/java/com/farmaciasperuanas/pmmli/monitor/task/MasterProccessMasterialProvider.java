package com.farmaciasperuanas.pmmli.monitor.task;

import com.farmaciasperuanas.pmmli.monitor.service.EndpointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MasterProccessMasterialProvider implements Runnable {

    @Autowired
    private EndpointService providerService;

    private static Logger logger = LoggerFactory.getLogger(MasterProccessMasterialProvider.class);
    @Override
    public void run() {
        logger.info("Proceso del maestro n°2");
        providerService.ejecutarProceso("MMP");
    }
}
