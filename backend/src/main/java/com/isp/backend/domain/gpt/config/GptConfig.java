package com.isp.backend.domain.gpt.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class GptConfig {
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String CHAT_MODEL = "gpt-4o";
    public static final Integer MAX_TOKEN = 4095;
    public static final Boolean STREAM = false;
    public static final String ROLE = "user";
    public static final Double TEMPERATURE = 1.0;
    public static final String MEDIA_TYPE = "application/json; charset=UTF-8";
    public static final String CHAT_URL = "https://api.openai.com/v1/chat/completions";
    public static final String PROMPT = """
                        You're a great travel agency staff member.
                        I would like you to plan a package tour program to plan your travel itinerary.
                        
                        Also, plan your itinerary based on the travel distance and famous tourist destinations.
                        Also, be sure to include the name of the landmark or location.
                        I want to visit a maximum of three attractions per day.
                        Make sure to plan for at least 10 hours of activity each day.
                        Please write the itinerary without using any special characters, only in plain text.
                        
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
                        3. 료기멘에서 점심식사 26.21239133 127.68093520
                        4. 츄라우미 수족관 구경 26.69459393 127.87731365
                        5. 잔파곶에서 일몰 감상 26.43397252 127.71761795
                        6. 나카미 본점에서 저녁식사 26.21306240 127.67969001
                        7. 국제거리 야시장 쇼핑 26.21224439 127.68115884
                        
                        ---
                        
                        여행지: %s
                        이번 여행의 목적: %s
                        여행에서 꼭 하고 싶은 활동: %s
                        여행에서 제외하고 싶은 활동: %s
                        한국에서 출국하는 날짜: %s
                        한국에 귀국하는 날짜: %s
                        
                        No need to say anything else, just plan your schedule right away.
                        Please create the result in Korean. And only include schedule-related content in the schedule you're creating.
                        Do not add any information I haven't provided to you. Under no circumstances should you include any activities other than traveling.
                        Absolutely not. 우리는 매일 점심과 저녁은 항상 식당에 가서 먹을거야. 반드시 실제로 존재하는 맛있고 유명한 식당으로 추천해줘 금액 정보는 알려주지마.
                        신 다양한 경험을 할 수 있게 일정을 만들어줘.
                        그리고 여행지 명소나 장소를 추천할 경우 소수점 여덟자리까지 좌표도 함께 알려줘.
                        모든 일정에 좌표를 꼭 붙여줘. 실제 데이터를 기반으로 정확한 좌표를 부탁해.""";
}