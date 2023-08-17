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

package dev.mbo.linkshortener2.domain.user

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import dev.mbo.linkshortener2.domain.RequestLogger

@RestController
@RequestMapping("/c")
class UserController(
    private val service: UserService,
) {

    @PostMapping(
        path = ["/", ""],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createUser(
        @RequestBody
        @Valid
        dto: UserCreateDto,
        exchange: ServerWebExchange,
    ): Mono<Boolean> {
        RequestLogger.logRequest(exchange)
        return service.save(
            dto.username,
            dto.password
        )
    }

    @GetMapping(
        path = ["/{username}", "/{username}/"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getUser(
        @PathVariable
        @NotBlank
        username: String,
        exchange: ServerWebExchange,
    ): Mono<User> {
        RequestLogger.logRequest(exchange)
        return service.find(username).switchIfEmpty {
            exchange.response.statusCode = HttpStatus.NOT_FOUND
            Mono.empty()
        }
    }

}