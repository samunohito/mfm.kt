package com.github.samunohito.mfm.api.node.factory.internal

import com.github.samunohito.mfm.api.finder.SubstringFoundInfo
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.inline.MentionFinder
import com.github.samunohito.mfm.api.node.MfmMention

object MentionNodeFactory : SimpleNodeFactoryBase<MfmMention>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.Mention)

  override fun doCreate(
    input: String,
    foundInfo: SubstringFoundInfo,
    context: INodeFactoryContext
  ): IFactoryResult<MfmMention> {
    val usernameInfo = foundInfo[MentionFinder.SubIndex.Username]
    val hostnameInfo = foundInfo[MentionFinder.SubIndex.Hostname]

    val username = input.substring(usernameInfo.contentRange)
    val hostname = if (hostnameInfo != SubstringFoundInfo.EMPTY) {
      input.substring(hostnameInfo.contentRange)
    } else {
      null
    }

    return success(MfmMention(username, hostname), foundInfo)
  }
}