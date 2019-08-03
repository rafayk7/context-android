from flask import Flask, request, jsonify, make_response
from twilio.twiml.messaging_response import MessagingResponse
import Utils
from ResultGeneration import ResultGen as res
import json

app = Flask(__name__)

sInfo = Utils.SharedInfo()
resp = MessagingResponse()
resGen = res()
utils = Utils.Utils()


@app.route('/sms', methods=['POST', 'GET'])
def sms():
    rVal = ""
    msg = request.form['Body']
    try:
        incomingMsgBody = utils.parseIncomingMessage(msg)
    except ValueError:
        incomingMsgBody = Utils.IncomingMessageStruct(reqType=sInfo.ERROR,
                                                      error="Please use fewer symbols in your query. Take out one of these 9: ['<', '>', '@', '#', '%', '^', '&', '*', ';', '/']")
    msgResp = MessagingResponse()
    reqType = incomingMsgBody.reqType
    error = False

    if incomingMsgBody.error is None:
        if incomingMsgBody.reqType == sInfo.GOOGLE_SEARCH:
            try:
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
            except:
                    rVal = "Error made  by me. Please contact me. Error: Search Library not working"
                    error = True
        elif incomingMsgBody.reqType == sInfo.TRANSLATION:
            try:
                if incomingMsgBody.transTo is not None and incomingMsgBody.transText is not None:
                    tRes = resGen.get_translation(_tolang=incomingMsgBody.transTo, _fromlang=incomingMsgBody.transFrom,
                                                  text=incomingMsgBody.transText)
                    if tRes.error is None:
                        rVal = "%s%s%s%s%s%s%s" % (
                        utils.lang(tRes.fromLang), sInfo.resultInnerDelim, utils.lang(tRes.toLang), sInfo.resultInnerDelim,
                        tRes.fromText, sInfo.resultInnerDelim, tRes.toText)
                    else:
                        rVal = tRes.error
                        error = True
                else:
                    rVal = "Error made  by me. Please contact me. Error: TO AND TEXT IN TRANS NONE"
                    error = True
            except:
                    rVal = "Error made  by me. Please contact me. Error: Translation Library not working"
                    error = True
        elif incomingMsgBody.reqType == sInfo.DIRECTIONS:
            try:
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
            except:
                    rVal = "Error made  by me. Please contact me. Error: Directions Library not working"
                    error = True
        elif incomingMsgBody.reqType == sInfo.SPORTS:
            try:
                if incomingMsgBody.sportSport is not None and incomingMsgBody.sportHome is not None and incomingMsgBody.sportAway is not None:
                    sRes = resGen.get_sport_update(incomingMsgBody.sportSport,
                                                   [incomingMsgBody.sportHome, incomingMsgBody.sportAway])
                    if sRes.error is None:
                        rVal = "%s%s%s%s%s%s%s%s" % (
                        sRes.home_team, sInfo.resultInnerDelim, sRes.away_team, sInfo.resultInnerDelim, sRes.home_score,
                        sInfo.resultInnerDelim, sRes.away_score, sInfo.resultOutterDelim)
                    else:
                        rVal = sRes.error
                        error = True
                else:
                    rVal = "Error made  by me. Please contact me. Error: TO AND FROM IN DIR NONE"
                    error = True
            except:
                    rVal = "Error made  by me. Please contact me. Error: Sports Library not working"
                    error = True
    else:
        rVal = incomingMsgBody.error
        error = True

    if isinstance(rVal, list):
        if not error:
            r = "%s%s%s%s" % (
            incomingMsgBody.dirFrom, sInfo.resultInnerDelim, incomingMsgBody.dirTo, sInfo.resultOutterDelim)
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

            msgResp.message(str(reqType) + '' + r)
        else:
            msgResp.message(str(sInfo.ERROR) + '' + rVal)
    else:
        if not error:
            msgResp.message(str(reqType) + '' + str(rVal))
        else:
            msgResp.message(str(sInfo.ERROR) + '' + str(rVal))

    return str(msgResp)

@app.route('/active', methods=['GET'])
def keepActive():
    return make_response(jsonify("{'active' : 'true'}"), 201)

if __name__ == "__main__":
    app.run(debug=True)

# msgQuery = "13will a caterpillar bite kill me"
# msgDir = "333229 steeplechase drive, burlington#bahen center, toronto#2"
# msgTrans = "23English#French#Good morning"
# msgSport = "43Cricket#England#Australia"

# sms(msgTrans)
