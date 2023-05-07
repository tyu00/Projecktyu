package ru.tinkoff.edu.java.scrapper.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.common.exception.IncorrectRequestParamsException;
import ru.tinkoff.edu.java.scrapper.service.api.ChatService;

@RestController
@RequestMapping("/tg-chat")
public class TgChatController {

    private final ChatService chatService;

    public TgChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/{id}/{username}")
    public void registerChat(@PathVariable long id, @PathVariable String username) {
        try {
            chatService.register(id, username);
        } catch (IllegalArgumentException e) {
            throw new IncorrectRequestParamsException(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteChat(@PathVariable long id) {
        try {
            chatService.unregister(id);
        } catch (IllegalArgumentException e) {
            throw new IncorrectRequestParamsException(e.getMessage());
        }
    }
}
