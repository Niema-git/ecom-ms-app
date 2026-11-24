package ma.enset.customerservice.web;

import ma.enset.customerservice.config.CustomerConfigParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RefreshScope
public class ConfigTestController {
    @Value("${global.params.p1}")
    private int p1;
    @Value("${global.params.p2}")
    private int p2;

    private final CustomerConfigParams customerConfigParams;

    public ConfigTestController(CustomerConfigParams customerConfigParams) {
        this.customerConfigParams = customerConfigParams;
    }

    @GetMapping("/testConfig1")
    public Map<String, Integer> globalParams() {
        return Map.of("p1", p1, "p2", p2);
    }

    @GetMapping("/testConfig2")
    public CustomerConfigParams customerParams() {
        return customerConfigParams;
    }
}
