package org.eskcti.mine.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eskcti.mine.dto.ProposalDTO;
import org.eskcti.mine.dto.ProposalDetailDTO;
import org.eskcti.mine.entities.ProposalEntity;
import org.eskcti.mine.messages.KafkaEvents;
import org.eskcti.mine.repositories.ProposalRepository;

import java.util.Date;

@ApplicationScoped
public class ProposalServiceImpl implements ProposalService{
    @Inject
    ProposalRepository proposalRepository;

    @Inject
    KafkaEvents kafkaMessages;

    @Override
    public ProposalDetailDTO findFullProposal(long id) {
        ProposalEntity proposal = proposalRepository.findById(id);

        return ProposalDetailDTO
                .builder()
                .proposalId(proposal.getId())
                .customer(proposal.getCustomer())
                .priceTonne(proposal.getPriceTonne())
                .country(proposal.getCountry())
                .tonnes(proposal.getTonnes())
                .proposalValidityDays(proposal.getProposalValidityDays())
                .build();
    }

    @Override
    @Transactional
    public void createNewProposal(ProposalDetailDTO proposalDetailDTO) {
        ProposalDTO proposalDTO = buildAndSaveNewProposal(proposalDetailDTO);
        kafkaMessages.sendNewKafkaEvent(proposalDTO);
    }

    @Override
    @Transactional
    public void removeProposal(long id) {
        proposalRepository.deleteById(id);
    }

    private ProposalDTO buildAndSaveNewProposal(ProposalDetailDTO proposalDetailDTO) {
        try {
            ProposalEntity proposal = new ProposalEntity();

            proposal.setCreated(new Date());
            proposal.setProposalValidityDays(proposalDetailDTO.getProposalValidityDays());
            proposal.setCountry(proposalDetailDTO.getCountry());
            proposal.setTonnes(proposalDetailDTO.getTonnes());
            proposal.setCustomer(proposalDetailDTO.getCustomer());
            proposal.setPriceTonne(proposalDetailDTO.getPriceTonne());

            proposalRepository.persist(proposal);

            return ProposalDTO.builder()
                    .proposalId(proposalRepository.findByCustomer(proposal.getCustomer()).get().getId())
                    .customer(proposal.getCustomer())
                    .priceTonne(proposal.getPriceTonne())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
