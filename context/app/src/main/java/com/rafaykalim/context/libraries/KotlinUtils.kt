package com.rafaykalim.context.libraries

class KotlinUtils {
    var sInfo = SharedInfo()
    fun genDirMsg(from : String, to : String, mode: Int): String
    {
        var useDelim = getDelim(from, to)

        // Message has format [REQ TYPE][DELIM INDEX][FROM][TO][MODE INT]
        var rMsg = "${sInfo.DIRECTIONS}${sInfo.delimiterList.indexOf(useDelim)}" +
                "${from}${useDelim}${to}${useDelim}${mode}"
        return rMsg
    }

    fun genTransMsg(fromLang : String?, toLang : String, origText : String) : String
    {
        var useDelim = getDelim(origText)
        var fromLang = fromLang ?: "null"

        // Message has format [REQ TYPE][DELIM INDEX][FROM_LANGUAGE][TO_LANGUAGE][TEXT TO TRANSLATE]
        var rMsg = "${sInfo.TRANSLATION}${sInfo.delimiterList.indexOf(useDelim)}" +
                "${fromLang}${useDelim}${toLang}${useDelim}${origText}"
        return rMsg
    }

    fun genTransMsg(toLang: String, origText: String) : String
    {
        return genTransMsg(null, toLang, origText)
    }

    fun genQueryMsg(query : String) : String
    {
        var useDelim = getDelim(query)

        // Message has format [REQ TYPE][DELIM INDEX][QUERY]
        var rMsg = "${sInfo.GOOGLE_SEARCH}${sInfo.delimiterList.indexOf(useDelim)}" +
                "${query}"

        return rMsg
    }

    fun genSportsMsg(sport : String?, homeTeam : String, awayTeam : String) : String
    {
        var useDelim = getDelim(homeTeam, awayTeam)

        // Message has format [REQ TYPE][DELIM INDEX][SPORT][TEAM_1][TEAM_2]
        var rMsg = "${sInfo.SPORTS}${sInfo.delimiterList.indexOf(useDelim)}" +
                "${sport}${useDelim}${homeTeam}${useDelim}${awayTeam}"

        return rMsg
    }


    fun getDelim(str1: String) : String
    {
        var useDelim : String?=null
        for (delim in sInfo.delimiterList)
        {
            var isInFrom = isIn(delim, str1)
            if (isInFrom)
            {
                continue
            }
            else
            {
                useDelim = delim
                break
            }
        }
        return useDelim!!
    }
    fun getDelim(str1: String, str2: String): String
    {
        var useDelim : String?=null

        for (delim in sInfo.delimiterList)
        {
            var isInFrom = isIn(delim, str1)
            var isInTo = isIn(delim, str2)

            if (isInFrom || isInTo)
            {
                continue
            }
            else if (!(isIn(delim, str1)) && !(isIn(delim, str2)))
            {
                useDelim = delim
                break
            }
        }
        return useDelim!!
    }

    fun isIn(delim : String, checkMsg : String) : Boolean
    {
        for (char in checkMsg)
        {
            if(char.equals(delim))
            {
                return true
            }
        }
        return false
    }
}