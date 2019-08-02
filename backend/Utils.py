# This class exists on both client and server
class SharedInfo:
    def __init__(self):
        self.GOOGLE_SEARCH = 1
        self.TRANSLATION = 2
        self.DIRECTIONS = 3
        self.SPORTS = 4
        self.delimiterList = ['<', '>', '@', '#', '%', '^', '&', '*', '<<', '>>']

        self.modes = [
            'driving',
            'walking',
            'bicycling',
            'transit'
        ]

        self.LANGUAGES = {
            'af': 'afrikaans',
            'sq': 'albanian',
            'am': 'amharic',
            'ar': 'arabic',
            'hy': 'armenian',
            'az': 'azerbaijani',
            'eu': 'basque',
            'be': 'belarusian',
            'bn': 'bengali',
            'bs': 'bosnian',
            'bg': 'bulgarian',
            'ca': 'catalan',
            'ceb': 'cebuano',
            'ny': 'chichewa',
            'zh-cn': 'chinese (simplified)',
            'zh-tw': 'chinese (traditional)',
            'co': 'corsican',
            'hr': 'croatian',
            'cs': 'czech',
            'da': 'danish',
            'nl': 'dutch',
            'en': 'english',
            'eo': 'esperanto',
            'et': 'estonian',
            'tl': 'filipino',
            'fi': 'finnish',
            'fr': 'french',
            'fy': 'frisian',
            'gl': 'galician',
            'ka': 'georgian',
            'de': 'german',
            'el': 'greek',
            'gu': 'gujarati',
            'ht': 'haitian creole',
            'ha': 'hausa',
            'haw': 'hawaiian',
            'iw': 'hebrew',
            'hi': 'hindi',
            'hmn': 'hmong',
            'hu': 'hungarian',
            'is': 'icelandic',
            'ig': 'igbo',
            'id': 'indonesian',
            'ga': 'irish',
            'it': 'italian',
            'ja': 'japanese',
            'jw': 'javanese',
            'kn': 'kannada',
            'kk': 'kazakh',
            'km': 'khmer',
            'ko': 'korean',
            'ku': 'kurdish (kurmanji)',
            'ky': 'kyrgyz',
            'lo': 'lao',
            'la': 'latin',
            'lv': 'latvian',
            'lt': 'lithuanian',
            'lb': 'luxembourgish',
            'mk': 'macedonian',
            'mg': 'malagasy',
            'ms': 'malay',
            'ml': 'malayalam',
            'mt': 'maltese',
            'mi': 'maori',
            'mr': 'marathi',
            'mn': 'mongolian',
            'my': 'myanmar (burmese)',
            'ne': 'nepali',
            'no': 'norwegian',
            'ps': 'pashto',
            'fa': 'persian',
            'pl': 'polish',
            'pt': 'portuguese',
            'pa': 'punjabi',
            'ro': 'romanian',
            'ru': 'russian',
            'sm': 'samoan',
            'gd': 'scots gaelic',
            'sr': 'serbian',
            'st': 'sesotho',
            'sn': 'shona',
            'sd': 'sindhi',
            'si': 'sinhala',
            'sk': 'slovak',
            'sl': 'slovenian',
            'so': 'somali',
            'es': 'spanish',
            'su': 'sundanese',
            'sw': 'swahili',
            'sv': 'swedish',
            'tg': 'tajik',
            'ta': 'tamil',
            'te': 'telugu',
            'th': 'thai',
            'tr': 'turkish',
            'uk': 'ukrainian',
            'ur': 'urdu',
            'uz': 'uzbek',
            'vi': 'vietnamese',
            'cy': 'welsh',
            'xh': 'xhosa',
            'yi': 'yiddish',
            'yo': 'yoruba',
            'zu': 'zulu',
            'fil': 'filipino',
            'he': 'hebrew'
        }

        self.LANG_CODES = dict(map(reversed, self.LANGUAGES.items()))


class Utils:
    def __init__(self):
        self.sInfo = SharedInfo()

    '''
        Parses the message and extracts information for processing

        ** params **
        message - Body from the message
        1. The first character in the message is the integer corresponding to the request type.
        2. The next character in the message is the element of array to use as delimiter. This is done to prevent 
        incorrect splicing. ex. if the user input is "<Foo", we will know not to use '<' as a delimiter, and will 
        instead move on to the next element in the delimiter option array.
        3. Rest of body format depends on the reqType

        ** returns ** 
        IncomingMessageStruct type with the needed fields filled in
    '''

    def parseIncomingMessage(self, message):
        reqType = int(message[0])
        delimIndex = int(message[1])

        # Get delimiter used
        if delimIndex > len(self.sInfo.delimiterList):
            return IncomingMessageStruct(
                error="Problem made by me. Please contact me to notfy me. Error: Delimiter List OOB")

        delim = self.sInfo.delimiterList[delimIndex]
        rVal = IncomingMessageStruct(reqType)
        message = message[2:]

        if reqType == self.sInfo.GOOGLE_SEARCH:
            rVal.googleQuery = message

        elif reqType == self.sInfo.TRANSLATION:
            msgObjs = message.split(delim)

            try:
                fromLang = msgObjs[0]
                toLang = msgObjs[1]
                fromText = msgObjs[2]

                rVal.transFrom = self.getLang(fromLang)
                rVal.transTo = self.getLang(toLang)
                rVal.transText = fromText
            except IndexError:
                rVal.error = "Problem made by me. Please contact me to notfy me. Error: Body List OOB"

        elif reqType == self.sInfo.DIRECTIONS:
            msgObjs = message.split(delim)

            try:
                fromDest = msgObjs[0]
                toDest = msgObjs[1]
                mode = self.sInfo.modes[int(msgObjs[2])]

                rVal.dirFrom = fromDest
                rVal.dirTo = toDest
                rVal.dirMode = mode
            except IndexError:
                rVal.error = "Problem made by me. Please contact me to notfy me. Error: Body List OOB"
        elif reqType == self.sInfo.SPORTS:
            msgObjs = message.split(delim)

            try:
                sport = msgObjs[0]
                home_team = msgObjs[1]
                away_team = msgObjs[2]

                rVal.sportSport = sport
                rVal.sportHome = home_team
                rVal.sportAway = away_team
            except IndexError:
                rVal.error = "Problem made by me. Please contact me to notfy me. Error: Body List OOB"

        return rVal

    def getLang(self, lang):
        return self.sInfo.LANG_CODES[lang.lower()]

class IncomingMessageStruct:
    def __init__(self, reqType, dirFrom=None, dirTo=None, dirMode=None, transFrom=None, transTo=None, transText=None,sportSport=None, sportHome=None, sportAway=None, googleQuery=None, error=None):
        self.reqType = reqType
        self.dirFrom = dirFrom
        self.dirTo = dirTo
        self.dirMode = dirMode
        self.transFrom = transFrom
        self.transTo = transTo
        self.transText = transText
        self.sportSport = sportSport
        self.sportHome = sportHome
        self.sportAway = sportAway
        self.googleQuery = googleQuery
        self.error = error

    def print_results(self):
        print(self.reqType)
        print(self.dirFrom)
        print(self.dirTo)
        print(self.dirMode)
        print(self.transFrom)
        print(self.transTo)
        print(self.transText)
        print(self.googleQuery)
        print(self.sportSport)
        print(self.sportHome)
        print(self.sportAway)
        print(self.error)

'''
    Result Structs
'''

class DirectionsResult:
    def __init__(self, dist=None, time=None, inst=None, err=None):
        self.distance = dist
        self.time = time
        self.instruction = inst
        self.error = err

    def __str__(self):
        return str(self.__class__) + ": " + str(self.__dict__)

class TranslationsResult:
    def __init__(self, fromLang=None, toLang=None, fromText=None, toText=None, error=None):
        self.fromLang = fromLang
        self.toLang = toLang
        self.fromText = fromText
        self.toText = toText
        self.error = error

    def __str__(self):
        return str(self.__class__) + ": " + str(self.__dict__)


class SportsResult:
    def __init__(self, matchObj=None, err=None):
        self.home_team = matchObj.home_team
        self.away_team = matchObj.away_team
        self.home_score = matchObj.home_score
        self.away_score = matchObj.away_score
        self.league = matchObj.league
        self.sport = matchObj.sport
        self.error = err

    def __str__(self):
        return str(self.__class__) + ": " + str(self.__dict__)

class sports:
    def __init__(self):
        self.BASEBALL = 'baseball'
        self.BASKETBALL = 'basketball'
        self.CRICKET = 'cricket'
        self.FOOTBALL = 'football'
        self.HANDBALL = 'handball'
        self.HOCKEY = 'hockey'
        self.RUGBY_L = 'rugby-league'
        self.RUGBY_U = 'rugby-union'
        self.SOCCER = 'soccer'
        self.TENNIS = 'tennis'
        self.VOLLEYBALL = 'volleyball'