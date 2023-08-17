package dev.mbo.linkshortener2.domain.user

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import java.time.Instant
import dev.mbo.linkshortener2.TestApplication

@SpringBootTest
@ContextConfiguration(classes = [TestApplication::class])
class UserTest @Autowired constructor(
    private val objectMapper: ObjectMapper,
) {

    private val tsFuture = Instant.now().plusMillis(100_000)
    private val tsPast = Instant.ofEpochMilli(0)

    private val enabledUser = User(
        authorities = listOf(
            "a",
            "b"
        ),
        password = "pass",
        username = "user",
        createdAt = tsPast
    )

    private val disabledUser = User(
        authorities = listOf(
            "a",
            "b"
        ),
        password = enabledUser.password,
        username = enabledUser.username,
        accountExpiresAt = tsPast,
        accountLockedUntil = tsFuture,
        credentialsExpiresAt = tsPast,
        enabled = false,
    )

    private val disabledUser2 = User(
        authorities = listOf(
            "a",
            "b"
        ),
        password = enabledUser.password,
        username = enabledUser.username,
        accountExpiresAt = tsFuture,
        accountLockedUntil = tsPast,
        credentialsExpiresAt = tsFuture,
        enabled = false,
    )

    @Test
    fun testEnabled() {
        assertThat(enabledUser.authorities).hasSize(2)
        assertThat(enabledUser.password).isNotBlank()
        assertThat(enabledUser.username).isNotBlank()

        assertThat(enabledUser.isAccountNonExpired).isTrue()
        assertThat(enabledUser.isAccountNonLocked).isTrue()
        assertThat(enabledUser.isCredentialsNonExpired).isTrue()
        assertThat(enabledUser.isEnabled).isTrue()
    }

    @Test
    fun testDisabled() {
        assertThat(disabledUser.authorities).hasSize(2)
        assertThat(disabledUser.password).isNotBlank()
        assertThat(disabledUser.username).isNotBlank()

        assertThat(disabledUser.isAccountNonExpired).isFalse()
        assertThat(disabledUser.isAccountNonLocked).isFalse()
        assertThat(disabledUser.isCredentialsNonExpired).isFalse()
        assertThat(disabledUser.isEnabled).isFalse()
    }

    @Test
    fun testDisabled2() {
        assertThat(disabledUser2.authorities).hasSize(2)
        assertThat(disabledUser2.password).isNotBlank()
        assertThat(disabledUser2.username).isNotBlank()

        assertThat(disabledUser2.isAccountNonExpired).isTrue()
        assertThat(disabledUser2.isAccountNonLocked).isTrue()
        assertThat(disabledUser2.isCredentialsNonExpired).isTrue()
        assertThat(disabledUser2.isEnabled).isFalse()
    }

    @Test
    fun testJson() {
        val result = objectMapper.writeValueAsString(enabledUser)
        assertThat(result).isEqualTo("{\"authorities\":[\"a\",\"b\"],\"password\":\"pass\",\"username\":\"user\",\"account_expires_at\":null,\"account_locked_until\":null,\"credentials_expires_at\":null,\"enabled\":true,\"created_at\":\"1970-01-01T00:00:00Z\"}")
    }

}