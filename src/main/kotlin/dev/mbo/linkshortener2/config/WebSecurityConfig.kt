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

package dev.mbo.linkshortener2.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain

@EnableWebFluxSecurity
@Configuration
class WebSecurityConfig {

    companion object {
        const val ROLE_ADMIN = "admin"
        const val ROLE_URL_CREATE = "url_create"
    }

    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http.cors { cors ->
            cors.disable()
        }.csrf { csrf ->
            csrf.disable()
        }.authorizeExchange { exchange ->
            exchange.pathMatchers(
                HttpMethod.POST,
                "/u"
            ).hasAnyRole(
                ROLE_ADMIN,
                ROLE_URL_CREATE,
            )
            exchange.pathMatchers(
                HttpMethod.GET,
                "/u/*",
                "/",
                "/css/*",
                "/js/*",
                "/img/*",
                "/index.html",
                "/error.html"
            ).permitAll()

            multiMatcher(
                exchange,
                arrayOf(
                    HttpMethod.GET,
                    HttpMethod.POST
                ),
                arrayOf(
                    "/c",
                    "/c/*",
                ),
                arrayOf(ROLE_ADMIN)
            )
        }.httpBasic(Customizer.withDefaults()).build()
    }

    private fun multiMatcher(
        exchange: ServerHttpSecurity.AuthorizeExchangeSpec,
        httpMethods: Array<HttpMethod>,
        antPatterns: Array<String>,
        requiredRoles: Array<String>,
    ) {
        httpMethods.forEach { httpMethod ->
            exchange.pathMatchers(
                httpMethod,
                *antPatterns
            ).hasAnyRole(*requiredRoles)
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

}