package fr.parisnanterre.greentrip.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class EmailVerificationService {

    // Clé API intégrée directement dans la classe
    private static final String API_KEY = "9b7abc6f7f854d79a8e57ec8095fbff3";

    private final RestTemplate restTemplate;

    public EmailVerificationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean verifyEmail(String email) {
        String url = "https://api.zerobounce.net/v2/validate";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("api_key", API_KEY)
                .queryParam("email", email);

        EmailValidationResponse response = restTemplate.getForObject(builder.toUriString(), EmailValidationResponse.class);
        return response != null && "valid".equals(response.getStatus());
    }

    // Classe interne pour capturer la réponse JSON de l'API ZeroBounce
    static class EmailValidationResponse {
        private String status;
        private String subStatus;
        private Boolean freeEmail;
        private String smtpProvider;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSubStatus() {
            return subStatus;
        }

        public void setSubStatus(String subStatus) {
            this.subStatus = subStatus;
        }

        public Boolean getFreeEmail() {
            return freeEmail;
        }

        public void setFreeEmail(Boolean freeEmail) {
            this.freeEmail = freeEmail;
        }

        public String getSmtpProvider() {
            return smtpProvider;
        }

        public void setSmtpProvider(String smtpProvider) {
            this.smtpProvider = smtpProvider;
        }
    }
}
