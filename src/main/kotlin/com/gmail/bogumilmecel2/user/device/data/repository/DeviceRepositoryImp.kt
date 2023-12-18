package com.gmail.bogumilmecel2.user.device.data.repository

import com.gmail.bogumilmecel2.common.domain.util.BaseRepository
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.common.util.extensions.toObjectId
import com.gmail.bogumilmecel2.diary_feature.domain.model.Device
import com.gmail.bogumilmecel2.diary_feature.domain.model.DeviceDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.toDevice
import com.gmail.bogumilmecel2.diary_feature.domain.model.toDto
import com.gmail.bogumilmecel2.user.device.domain.repository.DeviceRepository
import com.mongodb.client.model.UpdateOptions
import kotlinx.datetime.LocalDateTime
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

class DeviceRepositoryImp(private val deviceCollection: CoroutineCollection<DeviceDto>): DeviceRepository, BaseRepository() {
    override suspend fun insertDevice(userId: String, device: Device): Resource<Unit> {
        return handleRequest {
            deviceCollection.insertOne(
                document = device.toDto(userId = userId)
            )
        }
    }

    override suspend fun getDevice(userId: String, deviceId: String): Resource<Device?> {
        return handleRequest {
            deviceCollection.findOne(
                filter = and(
                    DeviceDto::_id eq deviceId.toObjectId(),
                    DeviceDto::userId eq userId
                )
            )?.toDevice()
        }
    }

    override suspend fun getDevices(userId: String): Resource<List<Device>> {
        return handleRequest {
            deviceCollection.find(DeviceDto::userId eq userId)
                .toList()
                .map { it.toDevice() }
        }
    }

    override suspend fun updateLastLoggedInDateTime(userId: String, deviceId: String, date: LocalDateTime): Resource<Unit> {
        return handleRequest {
            deviceCollection.updateOne(
                filter = and(
                    DeviceDto::_id eq deviceId.toObjectId(),
                    DeviceDto::userId eq userId
                ),
                update = setValue(DeviceDto::lastLoggedInDate, date),
                options = UpdateOptions()
            )
        }
    }
}