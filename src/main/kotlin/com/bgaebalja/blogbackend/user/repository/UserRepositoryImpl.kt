package com.bgaebalja.blogbackend.user.repository

import com.bgaebalja.blogbackend.user.domain.Users
import com.linecorp.kotlinjdsl.querydsl.expression.column
import com.linecorp.kotlinjdsl.querydsl.from.fetch
import com.linecorp.kotlinjdsl.querydsl.from.join
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import com.linecorp.kotlinjdsl.spring.data.singleQuery

class UserRepositoryImpl(private val queryFactory: SpringDataQueryFactory) : UserRepositoryCustom {

    override fun findUserWithRole(email: String): Users {
        return queryFactory.singleQuery {
            select(entity(Users::class))
            from(entity(Users::class))
            where(column(Users::email).equal(email))
            fetch(Users::roles)
            join(Users::roles)
        }
    }
}