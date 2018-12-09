package com.tf.translator.service;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TranslatorServiceImpl implements TranslatorService {

    @Autowired
    private Environment env;

    @Value("${api.encoding}")
    private String DEFAULT_ENCODING;

    @Value("${api.key}")
    private String DEFAULT_KEY;

    @Value("${api.url}")
    private String DEFAULT_URL;

    private ExecutorService executor = Executors.newFixedThreadPool(10);

    @Override
    public String translate(String input, String from, String to) {
        StringBuilder stringBuffer = new StringBuilder();

        final CompletionService<String> completionService = new ExecutorCompletionService<>(executor);

        List<Future<String>> futures = Arrays.stream(input.split(" "))
                .map(word -> completionService.submit(() -> translateWord(word, from, to)))
                .collect(Collectors.toList());
        try {
            while (!futures.isEmpty()) {
                Future<String> future = completionService.poll();
                if (future != null) {
                    stringBuffer.append(future.get()).append(" ");
                    futures.remove(future);
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println(e.getMessage());
            log.error("", e);
        }
        return stringBuffer.toString().trim();
    }

    private String translateWord(String word, String from, String to) {
        String separator = "&";
        try {
            String requestUrl = DEFAULT_URL + DEFAULT_KEY
                    + separator + "text=" + URLEncoder.encode(word, DEFAULT_ENCODING)
                    + separator + "lang=" + from + "-" + to;

            URL urlObj = new URL(requestUrl);


            HttpsURLConnection connection = (HttpsURLConnection) urlObj.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            InputStream response = connection.getInputStream();

            String json = new Scanner(response).nextLine();
            JSONObject obj = (JSONObject) new JSONParser().parse(json);
            return (String) ((JSONArray) obj.get("text")).get(0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            log.error("Error in request: ", e);
            return word;
        }
    }

    public void changeKey(String newKey) {
        if (newKey.toLowerCase().contains("default")) {
            DEFAULT_KEY = env.getProperty("api.key");
        } else if (!newKey.toLowerCase().contains("key")) {
            DEFAULT_KEY = "key=" + newKey;
        } else {
            DEFAULT_KEY = newKey;
        }
    }
}
