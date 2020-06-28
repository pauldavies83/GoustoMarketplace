package dev.pauldavies.goustomarketplace.util

import androidx.test.platform.app.InstrumentationRegistry
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Assert
import org.junit.Before

open class MockWebServerTestCase {

    protected val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        mockWebServer.dispatcher = ProductListDispatcher()
        mockWebServer.start(8080)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}

class ProductListDispatcher(val fileName: String = "valid_product_list_response.json") : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        return when {
            request.path!!.contains("products") -> validResponse(fileName)
            else -> notFoundResponse
        }
    }
}

object FourOhFourDispatcher : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        return notFoundResponse
    }
}

private val notFoundResponse = MockResponse().setResponseCode(404)
private fun validResponse(filename: String) = MockResponse().setResponseCode(200).setBody(
    getStringFromFile(filename)
)

private fun getStringFromFile(filePath: String): String {
    val stream = InstrumentationRegistry.getInstrumentation().context.resources.assets.open(filePath)
    val stringFromFile = stream.bufferedReader().use { it.readText() }
    stream.close()
    return stringFromFile
}

internal fun MockWebServer.assertRequestParamValue(key: String, value: String) {
    Assert.assertEquals(value, this.takeRequest().requestUrl!!.queryParameter(key))
}