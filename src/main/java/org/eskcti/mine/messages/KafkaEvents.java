package org.eskcti.mine.messages;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eskcti.mine.dto.ProposalDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class KafkaEvents {
    private final Logger LOG = LoggerFactory.getLogger(KafkaEvents.class);

    @Channel("proposal-channel")
    Emitter<ProposalDTO> proposalRequestEmmiter;

    public void sendNewKafkaEvent(ProposalDTO proposalDTO) {
        LOG.info("-- Enviando New Proposta para Tópico Kafka --");
        proposalRequestEmmiter.send(proposalDTO).toCompletableFuture().join();
    }
}
