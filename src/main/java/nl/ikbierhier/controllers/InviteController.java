package nl.ikbierhier.controllers;

import nl.ikbierhier.models.Invite;
import nl.ikbierhier.services.InviteService;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.UUID;

@RequestMapping("/invite")
@RestController
public class InviteController {

    private InviteService service;

    public InviteController(InviteService service) {
        this.service = service;
    }


    @PostMapping
    public Invite postInvite(@Valid @RequestBody Invite invite) {
        long currentTime = System.currentTimeMillis() / 1000L;
        invite.setExpiresAt(currentTime + 86400);
        service.postInvite(invite);
        return invite;
    }

    @GetMapping("/{inviteUUID}")
    public Invite getInvite(@PathVariable UUID inviteUUID) {
        long currentTime = System.currentTimeMillis() / 1000L;
        Invite invite = service.getInvite(inviteUUID);
        if(invite.getExpiresAt() < currentTime){
            return new Invite("0", 0);
        }
        return invite;
    }

}