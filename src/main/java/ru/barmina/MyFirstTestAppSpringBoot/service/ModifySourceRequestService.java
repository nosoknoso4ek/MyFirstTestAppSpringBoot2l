package ru.barmina.MyFirstTestAppSpringBoot.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ru.barmina.MyFirstTestAppSpringBoot.model.Request;

@Service
@Qualifier("ModifySourceRequestService")


public class ModifySourceRequestService implements ModifyRequestService {

    @Override
    public void modify(Request request) {
        request.setSource("Service 1");
        HttpEntity<Request> httpEntity = new HttpEntity<Request>(request);

        new RestTemplate().exchange("http://localhost:8085/feedback",
                HttpMethod.POST,
                httpEntity,
                new ParameterizedTypeReference<Request>() {

                });
    }
}
