package guru.bonacci.bepositive;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class MessageStore {

    private final ConcurrentHashMap<String, Message> messages = new ConcurrentHashMap<>();

    public void save(Message message) {
        messages.put(message.id(), message);
    }

    public Collection<Message> findAll() {
        return messages.values();
    }
}
