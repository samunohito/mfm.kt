package com.github.samunohito.mfm.api.node.factory.internal

import com.github.samunohito.mfm.api.node.MfmMention
import com.github.samunohito.mfm.api.parser.SubstringFoundInfo
import com.github.samunohito.mfm.api.parser.core.FoundType
import com.github.samunohito.mfm.api.parser.inline.MentionParser

object MentionNodeFactory : SimpleNodeFactoryBase<MfmMention>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.Mention)

  override fun doCreate(
    input: String,
    foundInfo: SubstringFoundInfo,
    context: INodeFactoryContext
  ): IFactoryResult<MfmMention> {
    val usernameInfo = foundInfo[MentionParser.SubIndex.Username]
    val hostnameInfo = foundInfo[MentionParser.SubIndex.Hostname]

    val username = input.substring(usernameInfo.contentRange)
    val hostname = if (hostnameInfo != SubstringFoundInfo.EMPTY) {
      input.substring(hostnameInfo.contentRange)
    } else {
      null
    }

    return success(MfmMention(username, hostname), foundInfo)
  }
}