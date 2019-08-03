import Utils
from ResultGeneration import ResultGen as res

sInfo = Utils.SharedInfo()
resGen = res()
utils = Utils.Utils()

rVal = ""
msg = ''

try:
    incomingMsgBody = utils.parseIncomingMessage(msg)
except ValueError:
    incomingMsgBody = Utils.IncomingMessageStruct(reqType=sInfo.ERROR, error="Please use fewer symbols in your query. Take out one of these 9: ['<', '>', '@', '#', '%', '^', '&', '*', ';', '/']")

reqType = incomingMsgBody.reqType
error = False

if incomingMsgBody.error is None:
    if incomingMsgBody.reqType == sInfo.GOOGLE_SEARCH:
        gRes = resGen.get_search_result(incomingMsgBody.googleQuery)

        if len(gRes) == 0:
            rVal = "Please refine your query. Use proper english. No results were found."
            error = True
        else:
            r = "%s%s" % (incomingMsgBody.googleQuery, sInfo.resultOutterDelim)
            for result in gRes:
                currString = "%s%s%s" % (result.description, sInfo.resultInnerDelim, sInfo.resultOutterDelim)
                r = r + currString
            rVal = r
    elif incomingMsgBody.reqType == sInfo.TRANSLATION:
        if incomingMsgBody.transTo is not None and incomingMsgBody.transText is not None:
            tRes = resGen.get_translation(_tolang=incomingMsgBody.transTo, _fromlang=incomingMsgBody.transFrom,
                                          text=incomingMsgBody.transText)
            if tRes.error is None:
                rVal = "%s%s%s%s%s%s%s" % (utils.lang(tRes.fromLang), sInfo.resultInnerDelim, utils.lang(tRes.toLang), sInfo.resultInnerDelim, tRes.fromText, sInfo.resultInnerDelim, tRes.toText)
            else:
                rVal = tRes.error
                error = True
        else:
            rVal = "Error made  by me. Please contact me. Error: TO AND TEXT IN TRANS NONE"
            error = True
    elif incomingMsgBody.reqType == sInfo.DIRECTIONS:
        if incomingMsgBody.dirTo is not None and incomingMsgBody.dirFrom is not None:
            dRes = resGen.get_directions(_from=incomingMsgBody.dirFrom, _to=incomingMsgBody.dirTo,
                                         _mode=incomingMsgBody.dirMode)
            if dRes[0].error is None:
                rVal = dRes
            else:
                rVal = dRes[0].error
                error = True
        else:
            rVal = "Error made  by me. Please contact me. Error: TO AND FROM IN DIR NONE"
    elif incomingMsgBody.reqType == sInfo.SPORTS:
        if incomingMsgBody.sportSport is not None and incomingMsgBody.sportHome is not None and incomingMsgBody.sportAway is not None:
            sRes = resGen.get_sport_update(incomingMsgBody.sportSport,
                                           [incomingMsgBody.sportHome, incomingMsgBody.sportAway])
            if sRes.error is None:
                rVal = "%s%s%s%s%s%s%s%s" % (sRes.home_team, sInfo.resultInnerDelim, sRes.away_team, sInfo.resultInnerDelim, sRes.home_score, sInfo.resultInnerDelim, sRes.away_score, sInfo.resultOutterDelim)
            else:
                rVal = sRes.error
                error = True
        else:
            rVal = "Error made  by me. Please contact me. Error: TO AND FROM IN DIR NONE"
            error = True
else:
    rVal = incomingMsgBody.error
    error = True

if isinstance(rVal, list):
    if not error:
        r = "%s%s%s%s" % (incomingMsgBody.dirFrom, sInfo.resultInnerDelim, incomingMsgBody.dirTo, sInfo.resultOutterDelim)
        for i in range(len(rVal)):
            dir = rVal[i]
            # The return string must have at least 1 resultInnerDelim and 1 resultOutterDelim. It will follow the general format of
            # [REQTYPE][TEXT 1][RESULTOUTTERDELIM]...[TEXT N][RESULTOUTTERDELIM]
            # Each TEXT has format [INFO 1][RESULTINNERDELIM]...[INFO N][RESULTINNERDELIM]

            currString = "%s%s%s%s%s%s" % (
                dir.distance, sInfo.resultInnerDelim, dir.time, sInfo.resultInnerDelim, dir.instruction,
                sInfo.resultOutterDelim
            )
            r = r + currString

        print(str(reqType) + '' + r)
    else:
        print(str(sInfo.ERROR) + '' + rVal)
else:
    if not error:
        print(str(reqType) + '' + str(rVal))
    else:
        print(str(sInfo.ERROR) + '' + str(rVal))

