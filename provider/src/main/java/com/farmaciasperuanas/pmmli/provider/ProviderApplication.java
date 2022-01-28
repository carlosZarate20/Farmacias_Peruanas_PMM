package com.farmaciasperuanas.pmmli.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class for running Spring Boot framework.<br/>
 * <b>Class</b>: Application<br/>
 * <b>Copyright</b>: &copy; 2021 Inkafarma Digital.<br/>
 * <b>Company</b>: Inkafarma Digital.<br/>
 *

 * <u>Developed by</u>: <br/>
 * <ul>
 * <li>Carlos Zárate Carpio</li>
 * </ul>
 * <u>Changes</u>:<br/>
 * <ul>
 * <li>Dec 28, 2021 Creaci&oacute;n de Clase.</li>
 * </ul>
 * @version 1.0
 */
@Slf4j
@SpringBootApplication
public class ProviderApplication{

  /**
   * Main method.
   */
  public static void main(String[] args) {
    new SpringApplication(ProviderApplication.class).run(args);
  }

}
