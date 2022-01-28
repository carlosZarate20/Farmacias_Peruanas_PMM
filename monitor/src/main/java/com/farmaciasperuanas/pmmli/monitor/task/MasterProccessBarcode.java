package com.farmaciasperuanas.pmmli.monitor.task;

import com.farmaciasperuanas.pmmli.monitor.service.EndpointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MasterProccessBarcode implements Runnable{
    @Autowired
    private EndpointService providerService;

    @Override
    public void run() {
        providerService.ejecutarProceso("MB");
    }
}
