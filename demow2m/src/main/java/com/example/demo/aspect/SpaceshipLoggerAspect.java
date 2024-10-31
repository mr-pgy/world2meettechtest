package com.example.demo.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
public class SpaceshipLoggerAspect {

    private static final Logger logger = Logger.getLogger(String.valueOf(SpaceshipLoggerAspect.class));

    @Before("execution(* com.example.demo.controller.SpaceshipController.*(..)) && args(id)")
    public void logNegativeId(Long id) {
        if (id < 0) {
            logger.warning("Se solicitó una nave con un ID negativo: " + id);
        }
    }
}
