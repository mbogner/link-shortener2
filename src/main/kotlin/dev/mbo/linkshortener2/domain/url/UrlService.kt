/*
 * Copyright (c) 2023.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.mbo.linkshortener2.domain.url

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.net.URL

@Service
class UrlService(
    private val repo: UrlRepository,
) {

    fun save(
        url: URL,
        username: String
    ): Mono<String> {
        return generateUniqueKey(url).flatMap { uniqueKey ->
            repo.save(
                uniqueKey,
                UrlVO(
                    url = url,
                    owner = username
                )
            ).thenReturn(uniqueKey)
        }
    }

    private fun generateUniqueKey(
        url: URL,
    ): Mono<String> {
        val key = UrlShortener.shorten(url)

        return repo.get(key).flatMap { existingUrl ->
            if (existingUrl == null) {
                return@flatMap Mono.just(key)
            } else {
                return@flatMap generateUniqueKey(url)
            }
        }.switchIfEmpty(Mono.just(key))
    }

    fun getById(url: String): Mono<String> {
        if (url.isBlank()) {
            throw IllegalArgumentException("shortened url can't be blank")
        }
        return repo.get(url).map { it.url.toString() }
    }

}