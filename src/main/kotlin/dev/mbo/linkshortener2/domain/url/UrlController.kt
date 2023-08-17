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

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.net.URI
import dev.mbo.linkshortener2.domain.RequestLogger

@RestController
@RequestMapping("/u")
internal class UrlController(
    private val service: UrlService,
) {

    @GetMapping(path = ["/{id}", "/{id}/"])
    fun getUrl(
        @PathVariable(required = true)
        id: String,
        exchange: ServerWebExchange,
    ): Mono<Void> {
        RequestLogger.logRequest(exchange)

        return service.getById(id).switchIfEmpty {
            exchange.response.statusCode = HttpStatus.NOT_FOUND
            Mono.empty()
        }.flatMap { url ->
            val response = exchange.response
            response.statusCode = HttpStatus.PERMANENT_REDIRECT
            response.headers.location = URI(url)
            exchange.response.setComplete()
        }
    }

    @PostMapping(
        path = ["", "/"],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createUrl(
        @RequestBody
        @Valid
        dto: UrlCreateDto,
        exchange: ServerWebExchange,
    ): Mono<String> {
        RequestLogger.logRequest(exchange)
        return ReactiveSecurityContextHolder.getContext().map { context -> context.authentication.principal as UserDetails }
            .flatMap { userDetails ->
                val username = userDetails.username
                service.save(
                    dto.url,
                    username
                )
            }
    }

}