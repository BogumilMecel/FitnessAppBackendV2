package com.gmail.bogumilmecel2.user.device.data.repository

import com.gmail.bogumilmecel2.common.domain.util.BaseRepository
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.DeviceDto
import com.gmail.bogumilmecel2.user.device.domain.repository.DeviceRepository
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.firstOrNull

class DeviceRepositoryImp(private val deviceCollection: MongoCollection<DeviceDto>): DeviceRepository, BaseRepository() {
    override suspend fun insertDevice(userId: String, device: DeviceDto): Resource<Unit> {
        return handleRequest {
            deviceCollection.insertOne(document = device)
        }
    }

    override suspend fun getDevice(userId: String, deviceId: String): Resource<DeviceDto?> {
        return handleRequest {
            deviceCollection.find(
                filter = and(
                    eq(DeviceDto::_id.name, deviceId),
                    eq(DeviceDto::userId.name, userId)
                )
            ).firstOrNull()
        }
    }

    override suspend fun updateLatestDiaryEntryTimestamp(userId: String, deviceId: String, timestamp: Long): Resource<Unit> {
        return handleRequest {
            deviceCollection.updateOne(
                filter = and(
                    eq(DeviceDto::_id.name, deviceId),
                    eq(DeviceDto::userId.name, userId)
                ),
                update = Updates.set(DeviceDto::latestDiaryEntryUtcTimestamp.name, timestamp)
            )
        }
    }

    override suspend fun updateLastLoggedInTimestamp(userId: String, deviceId: String, timestamp: Long): Resource<Unit> {
        return handleRequest {
            deviceCollection.updateOne(
                filter = and(
                    eq(DeviceDto::_id.name, deviceId),
                    eq(DeviceDto::userId.name, userId)
                ),
                update = Updates.set(DeviceDto::lastLoggedInUtcTimestamp.name, timestamp),
                options = UpdateOptions()
            )
        }
    }
}