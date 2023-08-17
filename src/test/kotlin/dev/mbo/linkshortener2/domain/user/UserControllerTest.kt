package dev.mbo.linkshortener2.domain.user

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import dev.mbo.linkshortener2.TestApplication
import dev.mbo.linkshortener2.config.WebSecurityConfig

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = [TestApplication::class])
class UserControllerTest @Autowired constructor(
    private val webClient: WebTestClient
) {

    private val dto = UserCreateDto(
        username = "testUser",
        password = "test123!"
    )

    @Test
    @WithMockUser(
        username = "admin",
        roles = [WebSecurityConfig.ROLE_ADMIN]
    )
    fun create() {
        post(dto).expectStatus().isOk
    }

    @Test
    @WithMockUser(
        username = "user",
        roles = [WebSecurityConfig.ROLE_URL_CREATE]
    )
    fun createAsUser() {
        post(dto).expectStatus().isForbidden
    }

    @Test
    fun createWithoutLogin() {
        post(dto).expectStatus().isUnauthorized
    }

    private fun post(body: UserCreateDto): WebTestClient.ResponseSpec {
        return webClient.post().uri("/c").contentType(MediaType.APPLICATION_JSON).body(
            BodyInserters.fromValue(body)
        ).exchange()
    }

}