package com.jffs.trade;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({
      "com.jffs.trade.config",
      "com.jffs.trade.task",
      "com.jffs.trade.oanda"
})
public class Application {
    public static void main(String[] args) {
        new GracefullyTerminatingApplication(Application.class, args).run();
    }
}
