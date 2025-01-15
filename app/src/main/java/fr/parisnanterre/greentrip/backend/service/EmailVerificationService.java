package fr.parisnanterre.greentrip.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class EmailVerificationService {

    @Value("a5d1bb9b98f846608fe8f38a65af95f8")
    private String apiKey;

    private final RestTemplate restTemplate;

    public EmailVerificationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean verifyEmail(String email) {
        String url = "https://api.zerobounce.net/v2/validate";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("api_key", apiKey)
                .queryParam("email", email);

        EmailValidationResponse response = restTemplate.getForObject(builder.toUriString(), EmailValidationResponse.class);
        return response != null && "valid".equals(response.getStatus());
    }

    // Add EmailValidationResponse class inside this service to capture the JSON response from ZeroBounce
    static class EmailValidationResponse {
        private String status;
        private String subStatus;
        private Boolean freeEmail;
        private String smtpProvider;
        // include other fields as needed

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        // getters and setters for other fields
    }
}

