from flask import Flask, request
from twilio.twiml.messaging_response import MessagingResponse
import Utils
from ResultGeneration import ResultGen as res

app = Flask(__name__)


sInfo = Utils.SharedInfo()
resp = MessagingResponse()
resGen = res()
utils = Utils.Utils()

@app.route('/sms', methods=['POST', 'GET'])
def sms():
    rVal = ""
    msg = request.form['Body']
    incomingMsgBody = utils.parseIncomingMessage(msg)
    msgResp = MessagingResponse()

    if incomingMsgBody.error is None:
        if incomingMsgBody.reqType == sInfo.GOOGLE_SEARCH:
            gRes = resGen.get_search_result(incomingMsgBody.googleQuery)

            if len(gRes) == 0:
                rVal = "Please refine your query. Use proper english. No results were found."
            else:
                rVal = gRes[0].description
        elif incomingMsgBody.reqType == sInfo.TRANSLATION:
            if incomingMsgBody.transTo is not None and incomingMsgBody.transText is not None:
                tRes = resGen.get_translation(_tolang=incomingMsgBody.transTo, _fromlang=incomingMsgBody.transFrom, text=incomingMsgBody.transText)
                if tRes.error is None:
                    rVal = tRes
                else:
                    rVal = tRes.error
            else:
                rVal = "Error made  by me. Please contact me. Error: TO AND TEXT IN TRANS NONE"
        elif incomingMsgBody.reqType == sInfo.DIRECTIONS:
            if incomingMsgBody.dirTo is not None and incomingMsgBody.dirFrom is not None:
                dRes = resGen.get_directions(_from=incomingMsgBody.dirFrom, _to=incomingMsgBody.dirTo, _mode=incomingMsgBody.dirMode)
                if dRes[0].error is None:
                    rVal = dRes
                else:
                    rVal = dRes[0].error
            else:
                rVal = "Error made  by me. Please contact me. Error: TO AND FROM IN DIR NONE"
        elif incomingMsgBody.reqType == sInfo.SPORTS:
            if incomingMsgBody.sportSport is not None and incomingMsgBody.sportHome is not None and incomingMsgBody.sportAway is not None:
                sRes = resGen.get_sport_update(incomingMsgBody.sportSport, [incomingMsgBody.sportHome, incomingMsgBody.sportAway])
                if sRes.error is None:
                    rVal = sRes
                else:
                    rVal = sRes.error
            else:
                rVal = "Error made  by me. Please contact me. Error: TO AND FROM IN DIR NONE"
    else:
        rVal = incomingMsgBody.error

    if isinstance(rVal, list):
        r = ""
        for i in range(len(rVal)):
            dir = rVal[i]
            currString = "%s<>%s<>%s;" % (dir.distance, dir.time, dir.instruction)
            r = r + currString

        msgResp.message(r)
    else:
        msgResp.message(str(rVal))
    return (str(msgResp))


if __name__=="__main__":
	app.run(debug=True)

# msgQuery = "13will a caterpillar bite kill me"
# msgDir = "333229 steeplechase drive, burlington#bahen center, toronto#2"
# msgTrans = "23English#French#Good morning"
# msgSport = "43Cricket#England#Australia"

# sms(msgTrans)