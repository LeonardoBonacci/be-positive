package guru.bonacci.bepositive;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final OllamaService ollamaService;
    private final MessageStore messageStore;

    public MessageController(OllamaService ollamaService, MessageStore messageStore) {
        this.ollamaService = ollamaService;
        this.messageStore = messageStore;
    }

    @PostMapping
    public Map<String, Object> submitMessage(@RequestBody Map<String, String> body) {
        String content = body.get("content");
        AnalysisResult result = ollamaService.analyze(content);

        if (result.accepted()) {
            Message message = new Message(UUID.randomUUID().toString(), content, LocalDateTime.now());
            messageStore.save(message);
            return Map.of("accepted", true, "reason", result.reason(), "message", message);
        }

        return Map.of(
                "accepted", false,
                "reason", result.reason(),
                "suggestedRewrite", result.suggestedRewrite()
        );
    }

    @GetMapping
    public Collection<Message> getAllMessages() {
        return messageStore.findAll();
    }
}
