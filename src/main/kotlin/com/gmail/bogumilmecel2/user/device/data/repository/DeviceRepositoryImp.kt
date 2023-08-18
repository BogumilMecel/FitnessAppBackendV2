package com.gmail.bogumilmecel2.user.device.data.repository

import com.gmail.bogumilmecel2.common.domain.util.BaseRepository
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.DeviceDto
import com.gmail.bogumilmecel2.user.device.domain.repository.DeviceRepository
import com.mongodb.client.model.UpdateOptions
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

class DeviceRepositoryImp(private val deviceCollection: CoroutineCollection<DeviceDto>): DeviceRepository, BaseRepository() {
    override suspend fun insertDevice(userId: String, device: DeviceDto): Resource<Unit> {
        return handleRequest {
            deviceCollection.insertOne(document = device)
        }
    }

    override suspend fun getDevice(userId: String, deviceId: String): Resource<DeviceDto?> {
        return handleRequest {
            deviceCollection.findOne(
                filter = and(
                    DeviceDto::_id eq deviceId,
                    DeviceDto::userId eq userId
                )
            )
        }
    }

    override suspend fun updateLatestDiaryEntryTimestamp(userId: String, deviceId: String, timestamp: Long): Resource<Unit> {
        return handleRequest {
            deviceCollection.updateOne(
                filter = and(
                    DeviceDto::_id eq deviceId,
                    DeviceDto::userId eq userId
                ),
                update = setValue(DeviceDto::latestDiaryEntryUtcTimestamp, timestamp)
            )
        }
    }

    override suspend fun updateLastLoggedInTimestamp(userId: String, deviceId: String, timestamp: Long): Resource<Unit> {
        return handleRequest {
            deviceCollection.updateOne(
                filter = and(
                    DeviceDto::_id eq deviceId,
                    DeviceDto::userId eq userId
                ),
                update = setValue(DeviceDto::lastLoggedInUtcTimestamp, timestamp),
                options = UpdateOptions()
            )
        }
    }
}