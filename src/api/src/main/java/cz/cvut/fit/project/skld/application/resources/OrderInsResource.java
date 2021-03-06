package cz.cvut.fit.project.skld.application.resources;


import cz.cvut.fit.project.skld.application.core.User;
import cz.cvut.fit.project.skld.application.operations.OrderInOperations;
import cz.cvut.fit.project.skld.application.operations.exceptions.InvalidStateException;
import cz.cvut.fit.project.skld.representations.OrderInChange;
import cz.cvut.fit.project.skld.representations.OrderInRepresentation;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementuje REST koncove body aplikace, ktere umoznuji uzivatelum tvorit a vyhledavat logisticke objednavky.
 */
@Path("/orders/in")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderInsResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductsResource.class);

    private final OrderInOperations orderInOps;

    /**
     * Konstruktor.
     * @param orderInOps OrderInOperations s business logikou logistickych objednavek
     */
    public OrderInsResource(OrderInOperations orderInOps) {
        this.orderInOps = orderInOps;
    }

    /**
     * Vytvori novou logistickou objednavku.
     * @param user Uzivatel, ktery operaci provedl
     * @param request Informace o nove objednavce
     * @return Logisticka objednavka
     */
    @POST
    @UnitOfWork
    @RolesAllowed({"admin"})
    public OrderInRepresentation create(@Auth User user, @Valid OrderInChange request) {
        try {
            return RepresentationConverter.representOrderIn(orderInOps.create(user, request));
        } catch (InvalidStateException e) {
            throw new WebApplicationException("Order with the given ID already exists", Response.Status.BAD_REQUEST);
        }
    }

    /**
     * Vrati seznam vsech logistickych objednavek v systemu.
     * @return Seznam logistickych objednavek
     */
    @GET
    @UnitOfWork
    public List<OrderInRepresentation> getAll() {
        return orderInOps.getAll().stream().map(RepresentationConverter::representOrderIn).collect(Collectors.toList());
    }
}
