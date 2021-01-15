package nl.ikbierhier.services;

import nl.ikbierhier.models.Invite;
import nl.ikbierhier.repositories.InviteRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InviteService {

    private InviteRepository inviteRepository;

    public InviteService(InviteRepository inviteRepository) {
        this.inviteRepository = inviteRepository;
    }

    public void postInvite(Invite invite) {
        inviteRepository.save(invite);
    }

    public Invite getInvite(UUID inviteUUID) {
        return inviteRepository.findById(inviteUUID).orElse(null);
    }

}