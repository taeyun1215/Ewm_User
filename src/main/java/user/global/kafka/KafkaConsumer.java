package user.global.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import user.adapter.out.persistence.UserJpaEntity;
import user.adapter.out.persistence.UserJpaRepo;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
    private final UserJpaRepo userJpaRepo;

    @KafkaListener(topics = "example-catalog-topic")
    public UserJpaEntity updateQty(String kafkaMessage) {
        log.info("Kafka Message: ->" + kafkaMessage);

        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});
        } catch (JsonProcessingException exception) {
            exception.printStackTrace();;
        }
        UserJpaEntity userJpaEntity = userJpaRepo.findByUsername((String) map.get("username")).get();
        return userJpaEntity;
    }
}
