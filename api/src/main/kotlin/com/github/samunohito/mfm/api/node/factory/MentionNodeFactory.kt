package com.github.samunohito.mfm.api.node.factory

import com.github.samunohito.mfm.api.finder.MentionFinder
import com.github.samunohito.mfm.api.finder.SubstringFoundInfo
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.node.MfmMention

class MentionNodeFactory : SimpleNodeFactoryBase<MfmMention>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.Mention)

  override fun doCreate(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmMention> {
    val usernameInfo = foundInfo[MentionFinder.SubIndex.Username]
    val hostnameInfo = foundInfo[MentionFinder.SubIndex.Hostname]

    val username = input.substring(usernameInfo.range)
    val hostname = if (hostnameInfo != SubstringFoundInfo.EMPTY) {
      input.substring(hostnameInfo.range)
    } else {
      null
    }

    val acct = if (hostname != null) {
      "@$username@$hostname"
    } else {
      "@$username"
    }

    return success(MfmMention(username, hostname), foundInfo)
  }
}