package com.tf.translator.controller;

import com.tf.translator.service.TranslatorService;
import com.tf.translator.service.TranslatorServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "translate", description = "Rest API", tags = "Translator")
@Slf4j
public class MainController {

    private TranslatorService translatorService;

    @Autowired
    public MainController(TranslatorServiceImpl service) {
        this.translatorService = service;
    }

    @GetMapping(value = "/translate")
    @ApiOperation("Returns translated text.")
    public ResponseEntity translate(@ApiParam("Text for translating.")
                                    @RequestParam("text") String text,
                                    @ApiParam("'From' what language translate")
                                    @RequestParam("from") String from,
                                    @ApiParam("'To' what language translate")
                                    @RequestParam("to") String to) {
        log.info(String.format("Translate (%s => %s): %s", from, to, text));
        String translatedString = translatorService.translate(text,
                from,
                to);
        log.info(String.format("Translated result: %s", translatedString));
        return ResponseEntity.ok(translatedString);
    }

    @GetMapping(value = "/changeKey")
    @ApiOperation("Change key, which used for yandex translator's API.")
    public ResponseEntity changeKey(@ApiParam("New key (Value 'default' for default key")
                                    @RequestParam("key") String key) {
        log.info("New key: " + key);
        translatorService.changeKey(key);
        return ResponseEntity.ok().build();
    }
}
