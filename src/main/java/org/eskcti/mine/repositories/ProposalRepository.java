package org.eskcti.mine.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.eskcti.mine.entities.ProposalEntity;

import java.util.Optional;

@ApplicationScoped
public class ProposalRepository implements PanacheRepository<ProposalEntity> {
    public Optional<ProposalEntity> findByCustomer(String customer) {
        return find("customer", customer).firstResultOptional();
    }
}
