package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.MfmSearch
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("検索構文を使用できる")
class SearchParserTest {
  private val parser = SearchParser()

  @Test
  @DisplayName("Search")
  fun test01() {
    val query = "MFM 書き方 123"
    val input = "$query Search"
    assertSearch(MfmSearch(query, input), input)
  }

  @Test
  @DisplayName("[Search]")
  fun test02() {
    val query = "MFM 書き方 123"
    val input = "$query [Search]"
    assertSearch(MfmSearch(query, input), input)
  }

  @Test
  @DisplayName("search")
  fun test03() {
    val query = "MFM 書き方 123"
    val input = "$query search"
    assertSearch(MfmSearch(query, input), input)
  }

  @Test
  @DisplayName("search")
  fun test04() {
    val query = "MFM 書き方 123"
    val input = "$query [search]"
    assertSearch(MfmSearch(query, input), input)
  }

  @Test
  @DisplayName("検索")
  fun test05() {
    val query = "MFM 書き方 123"
    val input = "$query 検索"
    assertSearch(MfmSearch(query, input), input)
  }

  @Test
  @DisplayName("[検索]")
  fun test06() {
    val query = "MFM 書き方 123"
    val input = "$query [検索]"
    assertSearch(MfmSearch(query, input), input)
  }

  private fun assertSearch(mfmSearch: MfmSearch, input: String) {
    val result = parser.parse(input)

    assertEquals(mfmSearch.props.query, result.node.props.query)
    assertEquals(mfmSearch.props.content, result.node.props.content)
  }
}