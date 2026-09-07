package com.mirandapdx.portlandcycling.services.data

import com.mirandapdx.portlandcycling.data.Event

data class EventData(
    val id: String,
    val title: String,
    val venue: String,
    val address: String,
    val organizer: String,
    val details: String,
    val time: String,
    val hideemail: String,
    val length: Any?,
    val timedetails: Any?,
    val locdetails: Any?,
    val eventduration: String,
    val weburl: Any?,
    val webname: String,
    val image: String,
    val audience: String,
    val tinytitle: String,
    val printdescr: String,
    val datestype: String,
    val area: String,
    val featured: Boolean,
    val printemail: Boolean,
    val printphone: Boolean,
    val printweburl: Boolean,
    val printcontact: Boolean,
    val email: Any?,
    val phone: Any?,
    val contact: Any?,
    val date: String,
    val caldaily_id: String,
    val shareable: String,
    val exportable: String,
    val cancelled: Boolean,
    val newsflash: Any?,
    val status: String,
    val endtime: String,
) {
    val model: Event
        get() = Event(
            id,
            title,
            organizer,
            venue,
            address,
            time,
            date,
            details)

}
data class EventsResponse(
    val events: List<EventData>
)