package org.eskcti.mine.controllers;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eskcti.mine.dto.ProposalDetailDTO;
import org.eskcti.mine.services.ProposalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/api/proposal")
@Authenticated
public class ProposalController {
    private final Logger LOG = LoggerFactory.getLogger(ProposalController.class);

    @Inject
    ProposalService proposalService;

    @Inject
    JsonWebToken jsonWebToken;

    @GET
    @Path("/{id}")
    @RolesAllowed({"user", "manager"})
    public ProposalDetailDTO findDetailsProposal(@PathParam("id") long id) {
        return proposalService.findFullProposal(id);
    }

    @POST
    @RolesAllowed("proposal-customer")
    public Response createProposal(ProposalDetailDTO proposalDetails) {
        LOG.info("--- Recebendo Proposta de Compra ---");

        try {
            proposalService.createNewProposal(proposalDetails);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("manager")
    public Response removeProposal(@PathParam("id") long id) {
        try {
            proposalService.removeProposal(id);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }
}
