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

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.Instant

// tell jackson to only use fields and ignore getters+setters
@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.ANY,
    getterVisibility = JsonAutoDetect.Visibility.NONE,
    setterVisibility = JsonAutoDetect.Visibility.NONE
)
// and for users we also want to have all fields serialised
@JsonInclude(JsonInclude.Include.ALWAYS)
data class User(

    @NotEmpty
    @JsonProperty("authorities")
    private var authorities: List<String>,

    @NotBlank
    @JsonProperty("password")
    private var password: String,

    @NotBlank
    @JsonProperty("username")
    private var username: String,

    @JsonProperty("account_expires_at")
    private var accountExpiresAt: Instant? = null,

    @JsonProperty("account_locked_until")
    private var accountLockedUntil: Instant? = null,

    @JsonProperty("credentials_expires_at")
    private var credentialsExpiresAt: Instant? = null,

    @JsonProperty("enabled")
    private var enabled: Boolean = true,

    @JsonProperty("created_at")
    private var createdAt: Instant = Instant.now(),
) : UserDetails {

    @JsonIgnore
    @Transient
    private val mappedAuthorities = authorities.map { GrantedAuthority { "ROLE_$it" } }.toMutableList()

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mappedAuthorities
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return null == accountExpiresAt || Instant.now().isBefore(accountExpiresAt)
    }

    override fun isAccountNonLocked(): Boolean {
        return null == accountLockedUntil || Instant.now().isAfter(accountLockedUntil)
    }

    override fun isCredentialsNonExpired(): Boolean {
        return null == credentialsExpiresAt || Instant.now().isBefore(credentialsExpiresAt)
    }

    override fun isEnabled(): Boolean {
        return enabled
    }
}