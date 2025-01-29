package com.jffs.trade;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class SignalController {

    @QueryMapping
    public void signalEG(@Argument String signal, @Argument String signalType) {
        //Signal -> US_EMPLOYMENT
        //SignalType -> Strong (+1), Weak (-1)


    }
}
