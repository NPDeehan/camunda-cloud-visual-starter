package ProcessController;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.Map;

@Component
@RestController
public class ProcessStartController {
    private final RestTemplate restTemplate;

    RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();

    public ProcessStartController() {
        this.restTemplate = restTemplateBuilder.build();
    }

    String clusterId;
    String clientId;
    String clientSecret;
    String region;

    @Value("${zeebe.client.cloud.clusterId}")
    public void setClusterId(String cluster){
        clusterId = cluster;
    }

    @Value("${zeebe.client.cloud.clientId}")
    public void setClientId(String client){
        clientId = client;
    }

    @Value("${zeebe.client.cloud.clientSecret}")
    public void setClientSecret(String secret){
        clientSecret = secret;
    }

    @Value("${zeebe.client.cloud.region}")
    public void setRegion(String regionCode){
        region = regionCode;
    }

    @RequestMapping(value = "/localStartProcess", method = RequestMethod.POST)
    public String sendOrder(@RequestBody ProcessStartRequest processRequest){

        System.out.println("Starting Process for "+ processRequest.processKey );
//        URI uri = URI.create("http://localhost:8080/orderUp/");
//        String response = restTemplate.postForObject(uri, order, String.class);
//        System.out.println("-- All good - the order has been sent -- ");

        ZeebeClient client = ZeebeClient.newCloudClientBuilder()
                .withClusterId(clusterId)
                .withClientId(clientId)
                .withClientSecret(clientSecret)
                .withRegion(region)
                .build();

        final ProcessInstanceEvent event = client.newCreateInstanceCommand()
                .bpmnProcessId(processRequest.processKey)
                .latestVersion()
                .variables(Map.of("message", processRequest.processVars))
                .send()
                .join();

//        LOG.info("Started instance for processDefinitionKey='{}', bpmnProcessId='{}', version='{}' with processInstanceKey='{}'",
//                event.getProcessDefinitionKey(), event.getBpmnProcessId(), event.getVersion(), event.getProcessInstanceKey());
        String response = "<strong>Started your process!! </strong>" + "<br>" +
                "Process Def: " + event.getProcessDefinitionKey() + "<br>" +
                " BPMN Process ID: " +event.getBpmnProcessId() + "<br>" +
                " Process Version " + event.getVersion() + "<br>" +
                "Process Instance Key: "+ event.getProcessInstanceKey();

        return response;
    }


}
