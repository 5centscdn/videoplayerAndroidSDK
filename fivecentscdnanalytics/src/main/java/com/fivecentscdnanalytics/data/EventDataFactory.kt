package com.fivecentscdnanalytics.data

import com.fivecentscdnanalytics.config.FCCDNAnalyticsConfig
import com.fivecentscdnanalytics.config.SourceMetadata
import com.fivecentscdnanalytics.data.manipulators.EventDataManipulator
import com.fivecentscdnanalytics.data.manipulators.EventDataManipulatorPipeline

class EventDataFactory(private val config: FCCDNAnalyticsConfig, private val userIdProvider: UserIdProvider) :
    EventDataManipulatorPipeline {
    private val eventDataManipulators = mutableListOf<EventDataManipulator>()

    // TODO DeviceInformationProvider for now is only available after `attachPlayerAdapter`, but can also be moved to the constructor of 5CentsCDNInsights and also in this class
    fun create(impressionId: String, sourceMetadata: SourceMetadata?, deviceInformation: DeviceInformation): EventData {
        val eventData = EventData(
                deviceInformation,
                impressionId,
                userIdProvider.userId(),
//                config.key,
                config.playerKey,
                if (sourceMetadata == null) config.videoId else sourceMetadata.videoId,
                if (sourceMetadata == null) config.title else sourceMetadata.title,
                if(config.userData != null) config.userData.customUserId else "",
                if (sourceMetadata == null) config.path else sourceMetadata.path,
                //if (sourceMetadata == null) config.experimentName else sourceMetadata.experimentName,
                if (sourceMetadata == null) "" else "",
                if (sourceMetadata == null) config.cdnProvider else sourceMetadata.cdnProvider,
                /*TODO This will always be overridden in the adapters, we need a logic like with m3u8 url*/
                config.playerType?.toString())

        for (decorator in eventDataManipulators) {
            decorator.manipulate(eventData)
        }

        return eventData
    }

    override fun clearEventDataManipulators() {
        eventDataManipulators.clear()
    }

    override fun registerEventDataManipulator(manipulator: EventDataManipulator) {
        eventDataManipulators.add(manipulator)
    }
}
