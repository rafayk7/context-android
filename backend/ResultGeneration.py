#!/usr/bin/python
# -*- coding: iso_8859_15 -*-

from googletrans import Translator
import googlemaps
from google import google
import sports
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
        self.result - String of translated text
    '''

    def get_translation(self, _tolang, _fromlang=None, text=""):
        error_str = "Please try again."
        self.translator = Translator()
        try:
            if _fromlang is not None:
                rVal = self.translator.translate(text, dest=_tolang, src=_fromlang).text
            else:
                rVal = self.translator.translate(text, dest=_tolang).text
        except ValueError:
            rVal = error_str

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
        self.result - A list of DirectionsResult, or a list of size-1 with error message.
    '''

    def get_directions(self, _from, _to, _mode="driving"):
        error_str = "Please try again. Make sure to enter the city, state/province or any other identifying information to make it accurate."
        gmaps = googlemaps.Client(key=self.gmapskey)

        now = datetime.now()

        dir_res = gmaps.directions(_from, _to, mode=_mode, departure_time=now, optimize_waypoints=True)

        rVal = []
        try:
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
    def sub_to_sport(self, _type, teams):
        rVal = sports.get_match(_type, teams[0], teams[1])

        self.result = rVal
        return self.result
        print(rVal)

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

class DirectionsResult:
    def __init__(self, dist=None, time=None, inst=None, err=None):
        self.distance = dist
        self.time = time
        self.instruction = inst
        self.error = err

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

x = ResultGen()

# _from = input("what do u wanna translate?")
# tolang = input("to what lang?")

x.sub_to_sport(sports.TENNIS, ["federer", "nadal"])
