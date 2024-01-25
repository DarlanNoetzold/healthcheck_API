package tech.noetzold.healthcheckgate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.noetzold.healthcheckgate.service.RecordService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/gate")
public class RecordController {

    @Autowired
    RecordService recordService;
}
