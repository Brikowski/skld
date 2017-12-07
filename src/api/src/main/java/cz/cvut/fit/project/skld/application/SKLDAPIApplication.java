package cz.cvut.fit.project.skld.application;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.toastshaman.dropwizard.auth.jwt.JwtAuthFilter;
import cz.cvut.fit.project.skld.application.auth.UserAuthenticator;
import cz.cvut.fit.project.skld.application.auth.UserAuthorizer;
import cz.cvut.fit.project.skld.application.db.*;
import cz.cvut.fit.project.skld.application.db.postgres.*;
import cz.cvut.fit.project.skld.application.resources.*;
import cz.cvut.fit.project.skld.application.core.*;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.hibernate.HibernateBundle;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;

public class SKLDAPIApplication extends Application<SKLDAPIConfiguration> {

    public static void main(final String[] args) throws Exception {
        new SKLDAPIApplication().run(args);
    }

    private final HibernateBundle<SKLDAPIConfiguration> hibernateBundle =
            new HibernateBundle<SKLDAPIConfiguration>(User.class, Product.class, ProductMovement.class, Order.class, OrderIn.class, LineItem.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(SKLDAPIConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    @Override
    public String getName() {
        return "SKLDAPI";
    }

    @Override
    public void initialize(final Bootstrap<SKLDAPIConfiguration> bootstrap) {
        bootstrap.addBundle(hibernateBundle);
    }

    @Override
    public void run(final SKLDAPIConfiguration configuration,
                    final Environment environment) {
        final ProductDAO productDAO = new PostgresProductDAO(hibernateBundle.getSessionFactory());
        final UserDAO userDAO = new PostgresUserDAO(hibernateBundle.getSessionFactory());
        final PositionDAO posDAO = new PostgresPositionDAO(hibernateBundle.getSessionFactory());
        final OrderInDAO orderInDAO = new PostgresOrderInDAO(hibernateBundle.getSessionFactory());
        final MovementDAO movementDAO = new PostgresMovementDAO(hibernateBundle.getSessionFactory());

        final byte[] key = configuration.getJwtSecret();
        final JwtConsumer consumer = new JwtConsumerBuilder()
            .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
            .setRequireExpirationTime() // the JWT must have an expiration time
            .setRequireSubject() // the JWT must have a subject claim
            .setVerificationKey(new HmacKey(key)) // verify the signature with the public key
            .setRelaxVerificationKeyValidation() // relaxes key length requirement
            .build(); // create the JwtConsumer instance
        environment.getObjectMapper().findAndRegisterModules();

        // Ye who removes this, prepare for your ISO formatted dates in JSON to become floats.
        environment.getObjectMapper().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        environment.jersey().register(new AuthDynamicFeature(
                new JwtAuthFilter.Builder<User>()
                        .setJwtConsumer(consumer)
                        .setRealm("realm")
                        .setPrefix("Bearer")
                        .setAuthenticator(new UserAuthenticator(userDAO, hibernateBundle.getSessionFactory()))
                        .setAuthorizer(new UserAuthorizer())
                        .buildAuthFilter()));
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthResource(key, userDAO));
        environment.jersey().register(new ProductsResource(productDAO));
        environment.jersey().register(new ProductResource(productDAO, posDAO));
        environment.jersey().register(new OrderInsResource(orderInDAO, productDAO));
        environment.jersey().register(new OrderInResource(orderInDAO, productDAO, movementDAO));
    }

}