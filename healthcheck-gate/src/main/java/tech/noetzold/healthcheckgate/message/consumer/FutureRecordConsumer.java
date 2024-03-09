package tech.noetzold.healthcheckgate.message.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.noetzold.healthcheckgate.controller.RecordController;
import tech.noetzold.healthcheckgate.message.config.RabbitmqQueues;
import tech.noetzold.healthcheckgate.model.FutureRecord;
import tech.noetzold.healthcheckgate.service.RecordService;

@Component
public class FutureRecordConsumer {

    @Autowired
    RecordService recordService;

    private static final Logger logger = LoggerFactory.getLogger(RecordController.class);

    @Transactional
    @RabbitListener(queues = RabbitmqQueues.RECORD_QUEUE)
    public void consumerRecord(String message) throws JsonProcessingException {
        FutureRecord record = new ObjectMapper().readValue(message, FutureRecord.class);
        try {
            recordService.createRecord(record);
            logger.info("Consume record - " + record);
        }catch (Exception ex){
            logger.error("Error to consume create message for record - " + record.toString(), ex);
            throw new AmqpRejectAndDontRequeueException("Ops, an error! Message should go to DLQ");
        }

    }
}
