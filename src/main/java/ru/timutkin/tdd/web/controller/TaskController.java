package ru.timutkin.tdd.web.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.timutkin.tdd.web.constant.ApiConstant;

@RequestMapping(
        value = ApiConstant.VERSION_API+"/task",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class TaskController {


    public ResponseEntity<>

}
