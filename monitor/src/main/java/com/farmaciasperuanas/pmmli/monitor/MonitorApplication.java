package com.farmaciasperuanas.pmmli.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main class for running Spring Boot framework.<br/>
 * <b>Class</b>: Application<br/>
 * <b>Copyright</b>: &copy; 2022 Inkafarma Digital.<br/>
 * <b>Company</b>: Inkafarma Digital.<br/>
 *

 * <u>Developed by</u>: <br/>
 * <ul>
 * <li>Carlos Zárate Carpio</li>
 * </ul>
 * <u>Changes</u>:<br/>
 * <ul>
 * <li>Jan 19, 2022 Creaci&oacute;n de Clase.</li>
 * </ul>
 * @version 1.0
 */

@SpringBootApplication
@EnableScheduling
public class MonitorApplication {

  /**
   * Main method.
   */
  public static void main(String[] args) {
    new SpringApplication(MonitorApplication.class).run(args);
  }
}
