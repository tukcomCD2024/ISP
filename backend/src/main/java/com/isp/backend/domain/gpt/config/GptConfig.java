package com.isp.backend.domain.gpt.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class GptConfig {
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String CHAT_MODEL = "gpt-3.5-turbo-16k";
    public static final Integer MAX_TOKEN = 3000;
    public static final Boolean STREAM = false;
    public static final String ROLE = "user";
    public static final Double TEMPERATURE = 0.6;
    public static final String MEDIA_TYPE = "application/json; charset=UTF-8";
    public static final String CHAT_URL = "https://api.openai.com/v1/chat/completions";
    public static final String PROMPT = """
                        안녕 정말 똑똑한 gpt야. 우리가 여행을 가려고 하는데 계획을 세우기가 너무 힘들어서 너의 도움을 받고싶어.
                        여행 정보는 다음과 같아.
                        
                        우리는 유명한 장소와 맛집을 모두 돌아나니고 싶어. 이번이 아마 내 인생의 마지막 여행이 될 거야.
                        그래서 말인데, gpt 너가 우리에게 아주 멋있는 여행 계획을 만들어주라.
                        
                        아참, 그리고 우리는 정확한 여행 비용을 가지고 떠날거라, 너가 정확한 여행 비용을 알려줘야돼. 식비, 교통비 등등 다 알려줘.
                        하루에 최소 4 ~ 5개의 일정은 소화하고 싶어.
                        
                        
                        <<최종 결과>>
                        2024-02-06
                        1. 후쿠오카 공항 도착
                        2. 택시 타고 이치란 라멘 오사카점으로 이동 (13,000원)
                        3. 이치란 라멘 오사카점에서 점심 식사 (예상 비용)
                            - Osaka, Chuo Ward, Souemoncho 7-18 (실제 위치)
                        4. 버스 93번 타고 오호리 공원으로 이동 (2,100원)
                            - Fukuoka, Chuo Ward, Ohorikoen 810-0051
                        5. 오호리 공원에서 산책
                        6. 걸어서 난바로 이동
                        7. 이가규 야키니쿠 엔 난나본점에서 저녁 식사 (65,000원)
                        8. 하카타역으로 가서 백화점 구경
                        9. 숙소로 복귀
                        
                        2024-02-07
                        1. 숙소에서 출발
                        2. 걸어서 하카타 모츠나베 오오야마로 이동
                        3. 하카타 모츠나베 오오야마에서 점심 식사 (34,000원)
                            - Fukuoka, Ward, Hakata, Tenyamachi, 7-28
                        3. 공항버스를 타고 후쿠오카 공항으로 이동 (3,200원)
                        4. 후쿠오카 공항 도착
                        ----------------------------------------------------------------
                        
                        
                        여행지: %s
                        이번 여행의 목적: %s
                        여행에서 꼭 하고 싶은 활동: %s
                        여행에서 제외하고 싶은 활동: %s
                        한국에서 출국하는 날짜: %s
                        한국에 귀국하는 날짜: %s
                        
                        위 계획처럼 우리가 알려준 여행지, 한국에서 출국하는 날짜, 한국에 도착하는 날짜, 여행 목적에 맞춰서 계획을 만들어줘. 위 계획은 그저 예시일 뿐이야. 반드시 예상 비용은 한국 돈 단위로 알려줘야돼.
                        장소를 계획에 추가할 땐 실제 위치도 함께 첨부해줘. 반드시 버스 정보도 함께 알려주고, 만약 버스가 없으면 택시를 타라고 알려줘 (ex. 택시 타고 오히리 공원으로 이동)
                        매일매일 점심 식사와 저녁 식사는 꼭 챙겨 먹을거야.
                        반드시 첫번째 날 일정의 시작은 여행지 나라의 "OO공항 도착", 그리고 마지막날 일정의 끝도 "OO공항 도착"이야.
                        
                        반드시 한국어로 만들어줘야돼.""";
}