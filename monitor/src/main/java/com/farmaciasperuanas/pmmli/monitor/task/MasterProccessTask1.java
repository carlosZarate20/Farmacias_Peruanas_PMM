package com.farmaciasperuanas.pmmli.monitor.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MasterProccessTask1 implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(MasterProccessTask1.class);
    @Override
    public void run() {
        logger.info("Proceso del maestro n°1");
    }
}
