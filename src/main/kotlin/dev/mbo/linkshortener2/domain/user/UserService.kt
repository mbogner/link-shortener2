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

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import dev.mbo.linkshortener2.config.WebSecurityConfig

@Service
class UserService(
    private val repo: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    @Value("\${app.redis.user.admin.username}")
    private val adminUsername: String,
    @Value("\${app.redis.user.admin.password}")
    private val adminPassword: String,
) {

    fun find(username: String): Mono<User> {
        return repo.find(username)
    }

    fun save(
        username: String,
        password: String
    ): Mono<Boolean> {
        return repo.save(
            User(
                username = username,
                password = passwordEncoder.encode(password),
                authorities = listOf(WebSecurityConfig.ROLE_URL_CREATE)
            )
        )
    }

    fun save(user: User): Mono<Boolean> {
        return repo.save(user)
    }

    @PostConstruct
    fun initAdmin() {
        find(adminUsername).switchIfEmpty {
            val newUser = User(
                username = adminUsername,
                password = passwordEncoder.encode(adminPassword),
                authorities = listOf(WebSecurityConfig.ROLE_ADMIN)
            )
            save(newUser).then(Mono.just(newUser))
        }.subscribe()
    }

}