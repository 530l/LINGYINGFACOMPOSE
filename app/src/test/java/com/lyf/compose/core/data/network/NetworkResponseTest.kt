package com.lyf.compose.core.data.network

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NetworkResponseTest {

    @Test
    fun ensureSuccess() {
        val resp = NetworkResponse(data = "ok", errorCode = 0, errorMsg = "")
        assertEquals("ok", resp.ensureSuccess())
    }

    @Test
    fun ensureSuccess_errorCode_throwsApiException() {
        val resp = NetworkResponse<String>(data = "x", errorCode = -1, errorMsg = "bad")
        try {
            resp.ensureSuccess()
            throw AssertionError("Expected ApiException")
        } catch (e: ApiException) {
            assertEquals(-1, e.code)
            assertTrue(e.message.contains("bad"))
        }
    }

    @Test
    fun ensureSuccess_throwsApiException() {
        val resp = NetworkResponse<String>(data = null, errorCode = 0, errorMsg = "no data")
        try {
            resp.ensureSuccess()
            throw AssertionError("Expected ApiException")
        } catch (e: ApiException) {
            assertEquals(0, e.code)
            assertTrue(e.message.contains("no data"))
        }
    }
}
