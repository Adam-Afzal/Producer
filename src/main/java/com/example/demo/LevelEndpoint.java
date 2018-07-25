package com.example.demo;

import com.google.gson.Gson;
import com.qa.constants.Constants;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
public class LevelEndpoint {


    @RequestMapping("/test")
    @ResponseBody
    public String sayHello() {

        return "Hi My name is ";
    }

    private final RabbitTemplate rabbitTemplate;
    private final RestTemplate restTemplate;
    private final Gson gson;


    private final Avatar avatar;


    public LevelEndpoint(RabbitTemplate rabbitTemplate, RestTemplate restTemplate, Gson gson, Avatar avatar) {
        this.rabbitTemplate = rabbitTemplate;
        this.restTemplate = restTemplate;
        this.gson = gson;

        this.avatar = avatar;
    }

    @RequestMapping(value = "/getAllLevels")
    @ResponseBody
    public String getAllLevelData() {

        String questions = restTemplate.getForObject(Constants.QUESTIONS_API_ALL, String.class);

        return questions;
    }

    @RequestMapping(value = "/getLevel/{level}", method = RequestMethod.GET)
    @ResponseBody
    public String getSpecificLevel(@PathVariable int level) {

        String question = restTemplate.getForObject(Constants.QUESTIONS_API_ALL + "{" + level + "}", String.class);

        return question;


    }


    @RequestMapping(value = "/sendScore/{player}", method = RequestMethod.POST)
    @ResponseBody
    public String sendScore(@PathVariable String player) {


        rabbitTemplate.convertAndSend(DemoApplication.topicExchangeName, "foo.bar.baz", player);
        return "Score saved to leaderboard!";
    }

    @RequestMapping("/avatar")
    @ResponseBody
    public String getAvatar() {


        //add request header


        BufferedReader in;
        StringBuffer response = null;
        try {
            String url = "https://avatars.dicebear.com/v2/identicon/"+(this.avatar.generateSeed()) + ".svg";

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));

            // TODO Auto-generated catch block


            String inputLine;
            response = new StringBuffer();


            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();


        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        System.out.println(response.toString());

return response.toString();

    }
}
