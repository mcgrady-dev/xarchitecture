/*
 * Copyright 2022 mcgrady
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:Suppress("NOTHING_TO_INLINE")

package com.mcgrady.xarch.ext

import android.util.Patterns
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Created by mcgrady on 2022/5/31.
 */

inline fun String?.isNotNullOrEmpty(): Boolean {
    return !this.isNullOrEmpty()
}

inline fun String.isValidPhone(): Boolean {
    return this.isNotNullOrEmpty() && Patterns.PHONE.matcher(this).matches()
}

// inline fun String.formatPhoneNumber(region: String): String? {
//    val phoneNumberUtil = PhoneNumberUtil.getInstance()
//    val number = phoneNumberUtil.parse(this, region)
//    if (!phoneNumberUtil.isValidNumber(number))
//        return null
//    return phoneNumberUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
// }

inline fun String.isValidEmail(): Boolean {
    return this.isNotNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

@Suppress("DEPRECATION")
inline fun String.isIPAddress(): Boolean {
    return this.isNotNullOrEmpty() && Patterns.IP_ADDRESS.matcher(this).matches()
}

inline fun String.isDomainName(): Boolean {
    return this.isNotNullOrEmpty() && Patterns.DOMAIN_NAME.matcher(this).matches()
}

inline fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
}
