package uk.emarte.jobhunting.gng.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uk.emarte.jobhunting.gng.entity.ProcessingRecord;

import java.util.List;

import static uk.emarte.jobhunting.gng.service.IpValidationException.ValidationError.*;

@Service
public class IpValidationService implements IIpValidationService {
    private final RestTemplate restTemplate;
    private final String validationApiPrefix;
    private final List<String> forbiddenCountryCodes;
    private final List<String> forbiddenOrgTokens;

    public IpValidationService(RestTemplate restTemplate, @Value("${ip.validation.url.prefix}") String validationApiPrefix, @Value("${ip.validation.forbidden.country.codes}") String forbiddenCountryCodes, @Value("${ip.validation.forbidden.org.tokens}") String forbiddenOrgTokens) {
        this.restTemplate = restTemplate;
        this.validationApiPrefix = validationApiPrefix;
        this.forbiddenCountryCodes = List.of(forbiddenCountryCodes.split(","));
        this.forbiddenOrgTokens = List.of(forbiddenOrgTokens.split(","));
    }

    @Override
    public void validateIp(String ipAddress, ProcessingRecord record) throws IpValidationException {
        IpValidationResponse response = restTemplate.getForObject(validationApiPrefix + ipAddress, IpValidationResponse.class);
        String status = null;

        if (response != null) {
            record.setCountryCode(response.countryCode());
            record.setIpProvider(response.isp());
            status = response.status();
        }

        if ("success".equals(status)) {
            if (invalidCountryCode(response)) {
                throw new IpValidationException(BLOCKED_COUNTRY_CODE, response.countryCode());
            }

            if (!validIspOrg(response)) {
                throw new IpValidationException(BLOCKED_ISP, response.org());
            }
        } else {
            throw new IpValidationException(API_ERROR, response != null ? response.status().toUpperCase() : "NO RESPONSE"); // TODO GIVE BETTER CONTEXT THAN THIS
        }
    }

    private boolean invalidCountryCode(IpValidationResponse response) {
        return forbiddenCountryCodes.contains(response.countryCode());
    }

    private boolean validIspOrg(IpValidationResponse response) {
        return response.org() == null || forbiddenOrgTokens.stream().noneMatch(t -> response.org().contains(t)); // TODO EXPLORE USING FIELDS 'ISP' OR 'ASNAME'
    }
}

