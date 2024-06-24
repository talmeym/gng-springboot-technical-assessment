package uk.emarte.jobhunting.gng.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import uk.emarte.jobhunting.gng.entity.ProcessingRecord;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;
import static uk.emarte.jobhunting.gng.service.IpValidationException.ValidationError.*;

@SpringBootTest
@TestPropertySource(properties = "ip.validation.url.prefix=http://localhost:8089/ip-api.com/json/")
public class IpValidationServiceTest {
    public static final String IP_ADDRESS = "127.0.0.1"; // NOT A VALID IP ADDRESS FOR THE API, BUT ONLY USED FOR URL

    @Autowired
    private IpValidationService ipValidationService;

    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void setup() {
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();
    }

    @AfterAll
    public static void teardown() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @BeforeEach
    public void init() {
        wireMockServer.resetAll();
    }

    @Test
    public void testHappyPath() throws IpValidationException {
        mockApiCallToReturn("{\"status\":\"success\",\"countryCode\":\"GB\",\"org\":\"myOrg\",\"isp\":\"myIsp\"}");
        ProcessingRecord record = new ProcessingRecord();

        ipValidationService.validateIp(IP_ADDRESS, record);

        assertEquals("GB", record.getCountryCode());
        assertEquals("myIsp", record.getIpProvider());
    }

    @Test
    public void testForbiddenCountryCode() {
        mockApiCallToReturn("{\"status\":\"success\",\"countryCode\":\"CN\",\"org\":\"chineseOrg\",\"isp\":\"chineseIsp\"}");
        IpValidationException ipValidationException = assertThrows(IpValidationException.class, () -> ipValidationService.validateIp(IP_ADDRESS, new ProcessingRecord()));
        assertSame(BLOCKED_COUNTRY_CODE, ipValidationException.getValidationError());
    }

    @Test
    public void testForbiddenISP() {
        mockApiCallToReturn("{\"status\":\"success\",\"countryCode\":\"GB\",\"org\":\"AWS something\",\"isp\":\"Amazon Web Services\"}");
        IpValidationException ipValidationException = assertThrows(IpValidationException.class, () -> ipValidationService.validateIp(IP_ADDRESS, new ProcessingRecord()));
        assertSame(BLOCKED_ISP, ipValidationException.getValidationError());
    }

    @Test
    public void testForbiddenApiErrorFail() {
        mockApiCallToReturn("{\"status\":\"fail\"}");
        IpValidationException ipValidationException = assertThrows(IpValidationException.class, () -> ipValidationService.validateIp(IP_ADDRESS, new ProcessingRecord()));
        assertSame(API_ERROR, ipValidationException.getValidationError());
    }

    @Test
    public void testForbiddenApiErrorNoResponse() {
        mockApiCallToReturn(null);
        IpValidationException ipValidationException = assertThrows(IpValidationException.class, () -> ipValidationService.validateIp(IP_ADDRESS, new ProcessingRecord()));
        assertSame(API_ERROR, ipValidationException.getValidationError());
    }

    private void mockApiCallToReturn(String body) {
        wireMockServer.stubFor(get(urlEqualTo("/ip-api.com/json/" + IP_ADDRESS))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(body)));
    }
}
