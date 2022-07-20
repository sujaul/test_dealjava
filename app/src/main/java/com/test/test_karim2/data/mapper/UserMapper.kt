package com.test.test_karim2.data.mapper

import com.chareem.core.data.BaseMapperResponseToEntity
import com.test.test_karim2.data.model.Users

open class UserMapper : BaseMapperResponseToEntity<Users, Users>{
    override fun mapResponseToEntity(response: Users): Users {
        return response
    }
}