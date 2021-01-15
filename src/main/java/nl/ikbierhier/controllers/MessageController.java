package nl.ikbierhier.controllers;

import nl.ikbierhier.models.Message;
import nl.ikbierhier.repositories.MessageRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;

@Transactional
@RestController
@RequestMapping("/message")
public class MessageController {

    private final EntityManager entityManager;
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public MessageController(EntityManager entityManager, MessageRepository messageRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.entityManager = entityManager;
        this.messageRepository = messageRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @PostMapping("/send")
    public void sendMessage(@Valid @RequestBody Message message) {
        Message savedMessage = this.messageRepository.saveAndFlush(message);
        this.entityManager.refresh(savedMessage);

        String topic = "/topic/" + message.getGroup().getUuid();

        this.simpMessagingTemplate.convertAndSend(topic, message);
    }
}
