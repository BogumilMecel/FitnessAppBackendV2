package com.gmail.bogumilmecel2.user.device.domain.repository

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.DeviceDto

interface DeviceRepository {
    suspend fun insertDevice(userId: String, device: DeviceDto): Resource<Unit>
    suspend fun getDevice(userId: String, deviceId: String): Resource<DeviceDto?>
    suspend fun updateLatestDiaryEntryTimestamp(userId: String, deviceId: String, timestamp: Long): Resource<Unit>
    suspend fun updateLastLoggedInTimestamp(userId: String, deviceId: String, timestamp: Long): Resource<Unit>
}