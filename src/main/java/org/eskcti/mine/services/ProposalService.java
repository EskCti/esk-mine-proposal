package org.eskcti.mine.services;

import jakarta.enterprise.context.ApplicationScoped;
import org.eskcti.mine.dto.ProposalDetailDTO;

@ApplicationScoped
public interface ProposalService {
    ProposalDetailDTO findFullProposal(long id);

    void createNewProposal(ProposalDetailDTO proposalDetailDTO);

    void removeProposal(long id);
}
