package com.mahlultech.footballclubs3.utils

import org.junit.Test

import org.junit.Assert.*

class UtilsKtTest {

    @Test
    fun testParseDate() {
        assertEquals("Thu, 20 Sep 2018", parseDate("2018-09-20"))
    }

    @Test
    fun testTimeFormatter(){
        assertEquals("21:00", "14:00:00".timeFormatter())
    }
}