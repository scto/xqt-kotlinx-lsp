// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.lsp.test.types

import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.lsp.types.TextDocumentItem
import xqt.kotlinx.rpc.json.serialization.UnsupportedKindTypeException
import xqt.kotlinx.rpc.json.serialization.jsonArrayOf
import xqt.kotlinx.rpc.json.serialization.jsonObjectOf
import xqt.kotlinx.test.DisplayName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

@DisplayName("The TextDocumentItem type")
class TheTextDocumentItemType {
    @Test
    @DisplayName("supports the non-optional properties")
    fun supports_the_non_optional_properties() {
        val json = jsonObjectOf(
            "uri" to JsonPrimitive("file:///home/lorem/ipsum.py"),
            "languageId" to JsonPrimitive("python"),
            "version" to JsonPrimitive(12),
            "text" to JsonPrimitive("Lorem ipsum dolor.")
        )

        val tdi = TextDocumentItem.deserialize(json)
        assertEquals("file:///home/lorem/ipsum.py", tdi.uri)
        assertEquals("python", tdi.languageId)
        assertEquals(12, tdi.version)
        assertEquals("Lorem ipsum dolor.", tdi.text)

        assertEquals(json, TextDocumentItem.serializeToJson(tdi))
    }

    @Test
    @DisplayName("throws an error if the kind type is not supported")
    fun throws_an_error_if_the_kind_type_is_not_supported() {
        val e1 = assertFails { TextDocumentItem.deserialize(jsonArrayOf()) }
        assertEquals(UnsupportedKindTypeException::class, e1::class)
        assertEquals("Unsupported kind type 'array'", e1.message)

        val e2 = assertFails { TextDocumentItem.deserialize(JsonNull) }
        assertEquals(UnsupportedKindTypeException::class, e2::class)
        assertEquals("Unsupported kind type 'null'", e2.message)

        val e3 = assertFails { TextDocumentItem.deserialize(JsonPrimitive("test")) }
        assertEquals(UnsupportedKindTypeException::class, e3::class)
        assertEquals("Unsupported kind type 'string'", e3.message)

        val e4 = assertFails { TextDocumentItem.deserialize(JsonPrimitive(true)) }
        assertEquals(UnsupportedKindTypeException::class, e4::class)
        assertEquals("Unsupported kind type 'boolean'", e4.message)

        val e5 = assertFails { TextDocumentItem.deserialize(JsonPrimitive(1)) }
        assertEquals(UnsupportedKindTypeException::class, e5::class)
        assertEquals("Unsupported kind type 'integer'", e5.message)

        val e6 = assertFails { TextDocumentItem.deserialize(JsonPrimitive(1.2)) }
        assertEquals(UnsupportedKindTypeException::class, e6::class)
        assertEquals("Unsupported kind type 'decimal'", e6.message)
    }
}
