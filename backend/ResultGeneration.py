#!/usr/bin/python
# -*- coding: iso_8859_15 -*-

from googletrans import Translator
import googlemaps
from google import google
import sports
from Utils import DirectionsResult, TranslationsResult, SportsResult
from datetime import datetime
import re

class ResultGen:
    def __init__(self):
        self.result = ""
        self.translator = None
        self.gmapskey = None
        self.regex_to_remove_html = '<[^>]+>'

        self.get_keys()

    '''
        Gets translations
        ** params **
        _tolang - Language to which it is to be translated
        _fromlang - (Optional) Language from which it is to be translated. Autodetect is on, so optional.
        text - Text to be translated
        
        ** returns **
        self.result - TranslationsResult class
            TranslationsResult:
                self.fromLang # Original language
                self.toLang # Translated language
                self.fromText # Original message
                self.toText # Translated message
                self.error # If there is an error
    '''

    def get_translation(self, _tolang, _fromlang=None, text=""):
        error_str = "Please try again."
        self.translator = Translator()
        try:
            if _fromlang is not None:
                res = self.translator.translate(text, dest=_tolang, src=_fromlang)
                rVal = TranslationsResult(fromLang=res.src, toLang=_tolang, fromText=text, toText=res.text)
            else:
                res = self.translator.translate(text, dest=_tolang)
                rVal = TranslationsResult(fromLang=res.src, toLang=_tolang, fromText=text, toText=res.text)
                # rVal = Translations(fromLang=toLang=_tolang, origText=text, toText=toText)
        except ValueError:
            rVal = TranslationsResult(error=error_str)

        self.result = rVal
        return self.result

    '''
        Gets keys from environment file in .gitignore, and loads them to self variable
    '''

    def get_keys(self):
        with open('keys.txt', 'r') as f:
            lines = f.readlines()

        for key in lines:
            keys = key.split("=")
            if keys[0] == "GOOGLE_MAPS_API_KEY":
                self.gmapskey = keys[1].replace('"', '')

    '''
        Gets Directions
        ** params **
        _from - origin point for directions
        _to - destination point for directions
        _mode - One of [driving, walking, bicycling, transit] - how to get there. Default - driving

        ** returns **
        self.result - A list of DirectionsResult class
            DirectionsResult:
                self.distance # String Distance for this step
                self.time # String Time for this step
                self.instruction # String instruction for this step
                self.error # If error
    '''

    def get_directions(self, _from, _to, _mode="driving"):
        error_str = "Please try again. Make sure to enter the city, state/province or any other identifying information to make it accurate."
        gmaps = googlemaps.Client(key=self.gmapskey)

        now = datetime.now()


        rVal = []
        try:
            dir_res = gmaps.directions(_from, _to, mode=_mode, departure_time=now, optimize_waypoints=True)
            steps = dir_res[0]['legs'][0]['steps']
            for step in steps:
                dist = step['distance']['text']
                dur = step['duration']['text']
                inst = re.sub(self.regex_to_remove_html, '', step['html_instructions'])

                mStep = DirectionsResult(dist=dist, time=dur, inst=inst)
                rVal.append(mStep)
        except:
            rVal = [DirectionsResult(err=error_str)]

        self.result = rVal
        return self.result

    '''
        Gets Google search results
        ** params **
        query - Search query

        ** returns **
        self.result - A list of GoogleResult
        GoogleResult:
            self.name # The title of the link
            self.link # The external link
            self.google_link # The google link
            self.description # The description of the link
            self.thumb # The link to a thumbnail of the website (NOT implemented yet)
            self.cached # A link to the cached version of the page
            self.page # What page this result was on (When searching more than one page)
            self.index # What index on this page it was on
            self.number_of_results # The total number of results the query returned
    '''

    def get_search_result(self, query):
        rVal = google.search(query)

        self.result = rVal
        return rVal

    '''
        Gets Sports Score Right Now
        ** params **
        _type - The name of sport. Look at Sports class to get supported sport types
        teams - 2-List. teams[0] has home team, teams[1] should have away team
        
        ** returns **
        self.result - One Match class
            Match -
                self.league # League of the match
                self.home_team # Home team
                self.away_team # Away team
                self.home_score # Home team score
                self.away_score # Away team score
                self.sport # Sport
    '''

    def get_sport_update(self, _type, teams):
        try:
            res = sports.get_match(_type, teams[0], teams[1])
            rVal = SportsResult(res)
        except:
            rVal = SportsResult(err="Error made by me. Please contact me to report. Error: SPORT UPDATE ERROR")

        self.result = rVal
        return self.result

    '''
        Test function to understand Gmaps API response
    '''
    def play_with_maps_response(self, dir_res):
        print(len(dir_res))
        print(dir_res[0].keys())
        print(len(dir_res[0]['legs']))
        print(dir_res[0]['legs'][0].keys())
        steps = dir_res[0]['legs'][0]['steps']
        print(len(steps))
        print
        for step in steps:
            print("In " + step['distance']['text'] + ' after ' + step['duration']['text'] + ' ' + step[
                'html_instructions'])

    '''
        Returns the current result
    '''
    def get_result(self):
        return self.result

