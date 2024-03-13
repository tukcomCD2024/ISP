package com.isp.backend.domain.gpt.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class GptConfig {
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String CHAT_MODEL = "gpt-3.5-turbo";
    public static final Integer MAX_TOKEN = 3000;
    public static final Boolean STREAM = false;
    public static final String ROLE = "user";
    public static final Double TEMPERATURE = 0.6;
    public static final String MEDIA_TYPE = "application/json; charset=UTF-8";
    public static final String CHAT_URL = "https://api.openai.com/v1/chat/completions";
    public static final String PROMPT = """
                        I would like you to plan a package tour program to plan your travel itinerary.Destination: %s
                        Departure date: %s
                        Entry date: %s
                        Purpose of travel: %s
                        The flight schedule is as follows.
                        
                        Also, plan your itinerary based on the travel distance and famous tourist destinations.
                        If possible, create the schedule in one-hour intervals.
                        Make sure to plan for at least 10 hours of activity each day.
                        
                        I would like the format to be as follows:
                        For example
                        ---
                        2024-02-06
                        1. Go to location
                        2. See the place
                        3. Eat the Lunch
                        4. Go to location
                        5. See the place
                        6. Eat the dinner
                        7. shopping
                        
                        2024-02-07
                        1. Go to location
                        2. See the place
                        3. Eat the Lunch
                        4. Go to location
                        5. See the place
                        6. Eat the dinner
                        7. shopping
                        
                        2024-02-08
                        1. Go to location
                        2. See the place
                        3. Eat the Lunch
                        4. Go to location
                        5. See the place
                        6. Eat the dinner
                        7. shopping
                        ---
                        No need to say anything else, just plan your schedule right away.
                        Please create the result in Korean.""";
}