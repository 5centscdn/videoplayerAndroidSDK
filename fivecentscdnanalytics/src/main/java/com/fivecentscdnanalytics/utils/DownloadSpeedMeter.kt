package com.fivecentscdnanalytics.utils


import com.fivecentscdnanalytics.data.DownloadSpeedInfo
import com.fivecentscdnanalytics.data.SpeedMeasurement
import kotlin.collections.map as map1

class DownloadSpeedMeter {

    private var measures = ArrayList<Measure>()
    private val thresholdBytes = 37500; // 300 Megabit per second in bytes per millisecond

    fun reset() {
        measures.clear()
    }

    fun addMeasurement(measurement: SpeedMeasurement) {
        if (measurement.httpStatus >= 400) {
            return
        }

        val measure = Measure(measurement)

        if (measure.speed >= thresholdBytes) {
            return
        }

        measures.add(measure)
    }

    fun getInfo(): DownloadSpeedInfo {
        var info = DownloadSpeedInfo()
        info.segmentsDownloadCount = measures.size
        info.segmentsDownloadSize = measures.map1 { it.downloadSize }.sum()
        info.segmentsDownloadTime = totalTime()
        info.avgDownloadSpeed = avgSpeed()
        info.minDownloadSpeed = minSpeed()
        info.maxDownloadSpeed = maxSpeed()
        info.avgTimeToFirstByte = avgTimeToFirstByte()
        return info
    }

    private fun avgSpeed(): Float {
        if (measures.size === 0) {
            return 0.0f
        }
        val totalSpeed = measures.map1 { it.speed }.sum()

        return totalSpeed.div(measures.size).times(8); // bytes per millisecond to kbps
    }

    private fun minSpeed(): Float? {
        if (measures.size === 0) {
            return 0.0f
        }
        // the slowest one to download
        return 0.0f;
        //TODO fix this issue
        //return measures.map1 { it.speed }.max() ? times(8) // bytes per millisecond to kbps
    }

    private fun maxSpeed(): Float? {
        if (measures.size === 0) {
            return 0.0f
        }
        // the fastest one to download
        return 0.0f;
        //TODO fix this issue
        //return measures.map1 { it.speed }.min()?.times(8); // bytes per millisecond to kbps
    }

    private fun totalTime(): Long {
        if (this.measures.size === 0) {
            return 0
        }
        return this.measures.map1 { it.duration }.sum()
    }

    private fun avgTimeToFirstByte(): Float {
        if (measures.size === 0) {
            return 0.0f
        }

        return measures.map1 { it.timeToFirstByte }.sum().div(measures.size)
    }
}
