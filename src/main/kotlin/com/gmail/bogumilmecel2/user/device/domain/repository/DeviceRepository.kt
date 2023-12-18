package com.gmail.bogumilmecel2.user.device.domain.repository

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.Device

interface DeviceRepository {
    suspend fun insertDevice(userId: String, device: Device): Resource<Unit>
    suspend fun getDevice(userId: String, deviceId: String): Resource<Device?>
    suspend fun getDevices(userId: String): Resource<List<Device>>
    suspend fun updateLastLoggedInTimestamp(userId: String, deviceId: String, timestamp: Long): Resource<Unit>
}