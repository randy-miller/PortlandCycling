package com.mirandapdx.clovercycle.screens.info

import com.mirandapdx.clovercycle.screens.ScreenViewModel

class InfoScreenViewModel: ScreenViewModel<InfoScreenState>(state = InfoScreenState()) {
    val infoBoxes: MutableList<List<String>> = mutableListOf()

    init {
        infoBoxes.addStr("Event info pulled from www.shift2bikes.org public calendar API")
        infoBoxes.addStr("Long press events to save them")
        infoBoxes.add(listOf("This app is open source", "view the source code or contribute at https://github.com/randy-miller/PortlandCycling"))
    }
}

fun MutableList<List<String>>.addStr(str: String) {
    add(listOf(str))
}