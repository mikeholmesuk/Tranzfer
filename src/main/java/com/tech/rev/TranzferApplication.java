package com.tech.rev;

import com.tech.rev.dao.AccountDAO;
import com.tech.rev.dao.TransferDAO;
import com.tech.rev.exception.WebExceptionMapper;
import com.tech.rev.resource.AccountResource;
import com.tech.rev.resource.SystemInfoResource;
import com.tech.rev.resource.TransferResource;
import com.tech.rev.service.AccountService;
import com.tech.rev.service.MoneyTransferService;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.flyway.FlywayBundle;
import io.dropwizard.flyway.FlywayFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.flywaydb.core.Flyway;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TranzferApplication extends Application<TranzferConfig> {

    private Logger logger = LoggerFactory.getLogger(TranzferApplication.class);

    public static void main(String[] args) throws Exception {
        new TranzferApplication().run(args);
    }

    public void inititlize(Bootstrap<TranzferConfig> bootstrap) {
        bootstrap.addBundle(new FlywayBundle<TranzferConfig>() {

            public DataSourceFactory getDataSourceFactory(TranzferConfig configuration) {
                return configuration.getDataSourceFactory();
            }

            @Override
            public FlywayFactory getFlywayFactory(TranzferConfig configuration) {
                return configuration.getFlywayFactory();
            }
        });
    }

    public void run(TranzferConfig tranzferConfig, Environment environment) {
        logger.info("Welcome to Tranzfer");

        // Database
        final DBIFactory dbiFactory = new DBIFactory();
        final DBI jdbi = dbiFactory.build(environment, tranzferConfig.getDataSourceFactory(), "h2");

        // Database migrations
        Flyway flyway = new Flyway();
        flyway.setDataSource(
                tranzferConfig.getDataSourceFactory().getUrl(),
                tranzferConfig.getDataSourceFactory().getUser(),
                tranzferConfig.getDataSourceFactory().getPassword());
        flyway.migrate();

        // Add the custom exception handler
        environment.jersey().register(new WebExceptionMapper());

        // Set up the DAO & service classes
        TransferDAO transferDAO = jdbi.onDemand(TransferDAO.class);
        AccountService accountService = new AccountService(jdbi.onDemand(AccountDAO.class));
        MoneyTransferService moneyTransferSvc = new MoneyTransferService(jdbi, accountService, transferDAO);


        // Set up the resource classes
        environment.jersey().register(new SystemInfoResource(tranzferConfig.getSystemInfo()));
        environment.jersey().register(new AccountResource(accountService));
        environment.jersey().register(new TransferResource(transferDAO, moneyTransferSvc));
    }
}
