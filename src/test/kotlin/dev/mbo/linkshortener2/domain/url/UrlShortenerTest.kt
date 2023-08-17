package dev.mbo.linkshortener2.domain.url

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.net.URL

class UrlShortenerTest {

    @Test
    fun shorten() {
        val result = UrlShortener.shorten(URL("http://localhost:8080"))
        assertThat(result.length).isGreaterThan(2)
    }

}