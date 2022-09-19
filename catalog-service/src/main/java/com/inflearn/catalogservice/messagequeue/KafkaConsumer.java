package com.inflearn.catalogservice.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inflearn.catalogservice.entity.CatalogEntity;
import com.inflearn.catalogservice.repository.CatalogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class KafkaConsumer { // 리스너로 가지고 온 데이터를 DB를 업데이트 한다.
    CatalogRepository catalogRepository;

    @Autowired
    public KafkaConsumer(CatalogRepository catalogRepository){
        this.catalogRepository = catalogRepository;
    }

    @KafkaListener(topics = "example-catalog-topic")
    public void updateQty(String kafkaMessage){
        // "example-catalog-topic"에 데이터가 전달이 되면 가지고 오면서 메서드가 실행된다.
        log.info("Kafka Message -> "+kafkaMessage);

        Map<Object, Object> map = new HashMap<>(); // 들어오는 건 스트링일 테지만 Object로 받는다.
        ObjectMapper mapper = new ObjectMapper();
        try{
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {}); // Object를 JSON으로 데이터 변경
        } catch(JsonProcessingException ex){
            ex.printStackTrace();
        }

        CatalogEntity entity = catalogRepository.findByProductId((String) map.get("productId"));
        if(entity != null){
            entity.setStock(entity.getStock() - (Integer)map.get("qty"));
            catalogRepository.save(entity);
        }
    }
}
