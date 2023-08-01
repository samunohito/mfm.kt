package com.github.samunohito.mfm.api.node.factory

import com.github.samunohito.mfm.api.node.IMfmNode
import com.github.samunohito.mfm.api.node.MfmNest
import com.github.samunohito.mfm.api.node.MfmNodeAttribute
import com.github.samunohito.mfm.api.node.MfmText
import com.github.samunohito.mfm.api.node.factory.internal.*
import com.github.samunohito.mfm.api.parser.SubstringFoundInfo
import com.github.samunohito.mfm.api.parser.core.FoundType

object NodeFactory {
  fun createNodes(
    input: String,
    foundInfos: List<SubstringFoundInfo>,
    context: INodeFactoryContext,
    allowNodeAttribute: Set<MfmNodeAttribute> = MfmNodeAttribute.setOfAll,
  ): List<IMfmNode> {
    return foundInfos.asSequence()
      .map { createNodes(input, it, context, allowNodeAttribute) }
      .filter { it.success }
      .map { it.node }
      .toList()
  }

  private fun createNodes(
    input: String,
    foundInfo: SubstringFoundInfo,
    context: INodeFactoryContext,
    allowNodeAttribute: Set<MfmNodeAttribute> = MfmNodeAttribute.setOfAll,
  ): IFactoryResult<IMfmNode> {
    return deepenNest(context) {
      createNodesImpl(input, foundInfo, context, allowNodeAttribute)
    }
  }

  private fun createNodesImpl(
    input: String,
    foundInfo: SubstringFoundInfo,
    context: INodeFactoryContext,
    allowNodeAttribute: Set<MfmNodeAttribute> = MfmNodeAttribute.setOfAll,
  ): IFactoryResult<IMfmNode> {
    // ネストが上限に達している場合は入力をそのままMfmTextとして扱い、これ以上ネストさせない
    if (context.nestLevel > context.maximumNestLevel) {
      return success(MfmText(input.substring(foundInfo.overallRange)), foundInfo)
    }

    when (val foundType = foundInfo.type) {
      FoundType.Simple, FoundType.Inline, FoundType.Full -> {
        val results = foundInfo.nestedInfos.map {
          createNodes(input, it, context, allowNodeAttribute)
        }
        if (results.any { !it.success }) {
          return failure()
        }

        return success(MfmNest(results.map { it.node }), foundInfo)
      }

      else -> {
        val factory = mappingToFactory(foundType)
        val result = factory.create(input, foundInfo, context)
        if (!result.success) {
          return failure()
        }

        val node = result.node
        if (!allowNodeAttribute.any { node.type.attributes.contains(it) }) {
          return failure()
        }

        return success(node, foundInfo)
      }
    }
  }

  private fun mappingToFactory(foundType: FoundType): INodeFactory<*> {
    return when (foundType) {
      FoundType.Big -> BigNodeFactory
      FoundType.BoldAsta -> BoldAstaNodeFactory
      FoundType.BoldTag -> BoldTagNodeFactory
      FoundType.BoldUnder -> BoldUnderNodeFactory
      FoundType.CenterTag -> CenterTagNodeFactory
      FoundType.CodeBlock -> CodeBlockNodeFactory
      FoundType.EmojiCode -> EmojiCodeNodeFactory
      FoundType.Fn -> FnNodeFactory
      FoundType.Hashtag -> HashtagNodeFactory
      FoundType.InlineCode -> InlineCodeNodeFactory
      FoundType.ItalicAsta -> ItalicAstaNodeFactory
      FoundType.ItalicTag -> ItalicTagNodeFactory
      FoundType.ItalicUnder -> ItalicUnderNodeFactory
      FoundType.Link -> LinkNodeFactory
      FoundType.MathBlock -> MathBlockNodeFactory
      FoundType.MathInline -> MathInlineNodeFactory
      FoundType.Mention -> MentionNodeFactory
      FoundType.PlainTag -> PlainTagNodeFactory
      FoundType.Quote -> QuoteNodeFactory
      FoundType.Search -> SearchNodeFactory
      FoundType.SmallTag -> SmallTagNodeFactory
      FoundType.StrikeTag -> StrikeTagNodeFactory
      FoundType.StrikeWave -> StrikeWaveNodeFactory
      FoundType.UnicodeEmoji -> UnicodeEmojiNodeFactory
      FoundType.Url -> UrlNodeFactory
      FoundType.UrlAlt -> UrlAltNodeFactory
      FoundType.Text -> TextNodeFactory
      else -> throw IllegalArgumentException("Not Support FoundType: $foundType")
    }
  }

  private fun <T> deepenNest(context: INodeFactoryContext, block: () -> T): T {
    context.nestLevel++
    val result = block()
    context.nestLevel--
    return result
  }
}