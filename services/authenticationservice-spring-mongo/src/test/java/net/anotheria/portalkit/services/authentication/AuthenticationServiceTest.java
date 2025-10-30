package net.anotheria.portalkit.services.authentication;

import net.anotheria.portalkit.services.authentication.persistence.AuthTokenEntityRepository;
import net.anotheria.portalkit.services.authentication.persistence.PasswordEntityRepository;
import net.anotheria.portalkit.services.common.AccountId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * AuthenticationServiceTest — TODO.
 *
 * @author ykalapusha
 * @since 29.10.2025
 */
@SpringBootTest(classes = PortalKITAuthenticationServiceConfig.class)
@Testcontainers
public class AuthenticationServiceTest {
//    @Container
//    static MongoDBContainer mongo = new MongoDBContainer("mongo:7.0");
//
//    @DynamicPropertySource
//    static void mongoProps(DynamicPropertyRegistry registry) {
//        registry.add("spring.data.mongodb.uri", mongo::getReplicaSetUrl);
//    }
//
//    @Autowired
//    private PasswordEntityRepository passwordRepo;
//
//    @Autowired
//    private AuthTokenEntityRepository tokenRepo;
//
//    @Autowired
//    private AuthenticationServiceFactory factory;
//
//    @Test
//    void testSetPassword_andRetrieve() throws Exception {
//        AuthenticationService service = factory.create();
//
//        AccountId id = new AccountId("abc");
//        service.setPassword(id, "1234");
//
//        Optional<String> stored = passwordRepo.findPasswordByAccountId(id.getInternalId());
//        assertTrue(stored.isPresent());
//    }
}
