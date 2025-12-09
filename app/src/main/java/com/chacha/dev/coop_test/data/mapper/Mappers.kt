package com.chacha.dev.coop_test.data.mapper

import com.chacha.dev.coop_test.data.local.entity.CardEntity
import com.chacha.dev.coop_test.data.local.entity.TransactionEntity
import com.chacha.dev.coop_test.data.local.entity.UserEntity
import com.chacha.dev.coop_test.data.local.entity.WalletEntity
import com.chacha.dev.coop_test.data.remote.responses.CardDto
import com.chacha.dev.coop_test.data.remote.responses.TransactionDto
import com.chacha.dev.coop_test.data.remote.responses.UserDto
import com.chacha.dev.coop_test.data.remote.responses.WalletDto
import com.chacha.dev.coop_test.domain.model.CardModel
import com.chacha.dev.coop_test.domain.model.CardType
import com.chacha.dev.coop_test.domain.model.TransactionModel
import com.chacha.dev.coop_test.domain.model.UserModel
import com.chacha.dev.coop_test.domain.model.WalletModel
import java.time.Instant

fun CardDto.toEntity(): CardEntity = CardEntity(
    id = id,
    userId = userId,
    type = type,
    name = name,
    cardNumber = cardNumber,
    holderName = holderName,
    expiryDate = expiryDate,
    status = status,
    balance = balance,
    currency = currency,
    currentSpend = currentSpend,
    creditLimit = creditLimit,
    dueDate = dueDate,
    linkedAccountName = linkedAccountName
)

fun WalletDto.toEntity(cardId: String): WalletEntity = WalletEntity(
    cardId = cardId,
    currency = currency,
    flag = flag,
    balance = balance
)

fun TransactionDto.toEntity(): TransactionEntity = TransactionEntity(
    id = id,
    cardId = cardId,
    amount = amount,
    currency = currency,
    date = date,
    merchant = merchant,
    type = type
)

fun UserDto.toEntity(): UserEntity = UserEntity(
    id = id,
    firstName = firstName,
    lastName = lastName,
    email = email,
    phone = phone,
    avatarUrl = avatarUrl,
    street = address.street,
    city = address.city,
    country = address.country,
    postalCode = address.postalCode
)

fun CardEntity.toDomain(wallets: List<WalletEntity>): CardModel = CardModel(
    id = id,
    userId = userId,
    type = CardType.from(type),
    name = name,
    cardNumber = cardNumber,
    holderName = holderName,
    expiryDate = expiryDate,
    status = status,
    balance = balance,
    currency = currency,
    currentSpend = currentSpend,
    creditLimit = creditLimit,
    dueDate = dueDate,
    linkedAccountName = linkedAccountName,
    wallets = wallets.map { it.toDomain() }
)

fun WalletEntity.toDomain(): WalletModel = WalletModel(
    currency = currency,
    flag = flag,
    balance = balance
)

fun TransactionEntity.toDomain(): TransactionModel = TransactionModel(
    id = id,
    cardId = cardId,
    amount = amount,
    currency = currency,
    date = Instant.parse(date),
    merchant = merchant,
    type = type
)

fun UserEntity.toDomain(): UserModel = UserModel(
    id = id,
    firstName = firstName,
    lastName = lastName,
    email = email,
    phone = phone,
    avatarUrl = avatarUrl,
    street = street,
    city = city,
    country = country,
    postalCode = postalCode
)

fun CardModel.toEntity(): CardEntity = CardEntity(
    id = id,
    userId = userId,
    type = type.name,
    name = name,
    cardNumber = cardNumber,
    holderName = holderName,
    expiryDate = expiryDate,
    status = status,
    balance = balance,
    currency = currency,
    currentSpend = currentSpend,
    creditLimit = creditLimit,
    dueDate = dueDate,
    linkedAccountName = linkedAccountName
)

fun WalletModel.toEntity(cardId: String): WalletEntity = WalletEntity(
    cardId = cardId,
    currency = currency,
    flag = flag,
    balance = balance
)

fun TransactionModel.toEntity(): TransactionEntity = TransactionEntity(
    id = id,
    cardId = cardId,
    amount = amount,
    currency = currency,
    date = date.toString(),
    merchant = merchant,
    type = type
)

fun UserModel.toEntity(): UserEntity = UserEntity(
    id = id,
    firstName = firstName,
    lastName = lastName,
    email = email,
    phone = phone,
    avatarUrl = avatarUrl,
    street = street,
    city = city,
    country = country,
    postalCode = postalCode
)

fun CardDto.toDomain(): CardModel = CardModel(
    id = id,
    userId = userId,
    type = CardType.from(type),
    name = name,
    cardNumber = cardNumber,
    holderName = holderName,
    expiryDate = expiryDate,
    status = status,
    balance = balance,
    currency = currency,
    currentSpend = currentSpend,
    creditLimit = creditLimit,
    dueDate = dueDate,
    linkedAccountName = linkedAccountName,
    wallets = wallets.map { it.toDomain() }
)

fun WalletDto.toDomain(): WalletModel = WalletModel(
    currency = currency,
    flag = flag,
    balance = balance
)

fun TransactionDto.toDomain(): TransactionModel = TransactionModel(
    id = id,
    cardId = cardId,
    amount = amount,
    currency = currency,
    date = Instant.parse(date),
    merchant = merchant,
    type = type
)

fun UserDto.toDomain(): UserModel = UserModel(
    id = id,
    firstName = firstName,
    lastName = lastName,
    email = email,
    phone = phone,
    avatarUrl = avatarUrl,
    street = address.street,
    city = address.city,
    country = address.country,
    postalCode = address.postalCode
)

