package com.farmaciasperuanas.pmmli.monitor.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MasterProccessTask2 implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(MasterProccessTask2.class);
    @Override
    public void run() {
        logger.info("Proceso del maestro n°2");
    }
}
