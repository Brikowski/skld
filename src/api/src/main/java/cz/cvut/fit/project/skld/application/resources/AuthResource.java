package cz.cvut.fit.project.skld.application.resources;

import cz.cvut.fit.project.skld.application.core.User;
import cz.cvut.fit.project.skld.application.db.UserDAO;
import cz.cvut.fit.project.skld.application.util.WebAppExceptionSupplier;
import cz.cvut.fit.project.skld.representations.LogInDetails;
import cz.cvut.fit.project.skld.representations.PIN;
import io.dropwizard.hibernate.UnitOfWork;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.jose4j.jws.AlgorithmIdentifiers.HMAC_SHA256;

/**
 * Implementuje REST koncove body aplikace, ktere umoznuji uzivatelum ziskat bezpecnostni tokeny pro provadeni dalsich operaci.
 */
@Path("/log")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductsResource.class);

    private final UserDAO userDAO;
    private final byte[] tokenKey;

    /**
     * Kontruktor.
     * @param tokenKey Klic pouzity v JWT encryption/signature algoritmech
     * @param uDao DAO pro pristup k uzivatelum v databazi
     */
    public AuthResource(byte[] tokenKey, UserDAO uDao) {
        this.tokenKey = tokenKey;
        userDAO = uDao;
    }

    /**
     * Overi zadany PIN a pri souhlasu vrati bezpecostni token a informace o prihlasenem uzivateli.
     * Pri nespravnem PINu vyvola WebAppExceptionSupplier.
     * @param pinObject Deserializovany PIN
     * @return LogInDetails s bezpecostnim tokenem a informacemi o prihlasenem uzivateli
     */
    @POST
    @Path("/in")
    @UnitOfWork
    public LogInDetails logIn(PIN pinObject) {
        User user = userDAO.findByPin(pinObject.getPin()).orElseThrow(new WebAppExceptionSupplier("PIN incorrect or nonexistent", Response.Status.UNAUTHORIZED));
        final JwtClaims claims = new JwtClaims();
        claims.setExpirationTimeMinutesInTheFuture(600);
        claims.setSubject(Long.toString(user.getId()));

        final JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue(HMAC_SHA256);
        jws.setKey(new HmacKey(tokenKey));

        try {
            return new LogInDetails(RepresentationConverter.representUser(user), jws.getCompactSerialization());
        } catch (JoseException e) { throw new RuntimeException(e); }
    }
}
