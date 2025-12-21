package com.lyf.compose.core.data.network

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NetworkResponseTest {

    @Test
    fun requireData_success_returnsData() {
        val resp = NetworkResponse(data = "ok", errorCode = 0, errorMsg = "")
        assertEquals("ok", resp.requireData())
    }

    @Test
    fun requireData_errorCode_throwsApiException() {
        val resp = NetworkResponse<String>(data = "x", errorCode = -1, errorMsg = "bad")
        try {
            resp.requireData()
            throw AssertionError("Expected ApiException")
        } catch (e: ApiException) {
            assertEquals(-1, e.code)
            assertTrue(e.message.contains("bad"))
        }
    }

    @Test
    fun requireData_nullData_throwsApiException() {
        val resp = NetworkResponse<String>(data = null, errorCode = 0, errorMsg = "no data")
        try {
            resp.requireData()
            throw AssertionError("Expected ApiException")
        } catch (e: ApiException) {
            assertEquals(0, e.code)
            assertTrue(e.message.contains("no data"))
        }
    }
}
