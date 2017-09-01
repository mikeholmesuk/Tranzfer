package com.tech.rev;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class TranzferApplication extends Application<TranzferConfig> {

    public static void main(String[] args) throws Exception {
        new TranzferApplication().run(args);
    }


    public void run(TranzferConfig tranzferConfig, Environment environment) {
        System.out.println("Welcome to Tranzfer");
    }
}
