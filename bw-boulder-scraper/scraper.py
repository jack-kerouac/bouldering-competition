import urllib
import re
import json
import requests

from pyquery import PyQuery as pq

# SCRAPING BOULDERS FROM http://boulderwelt.tcwebworks.de/dev/index.php

BASE_URL = 'http://boulderwelt.tcwebworks.de/dev'

# IGNORE THESE PARCOURS (boulders in these parcours reappear in others)
IGNORED_PARCOURS = ['TOP 20', 'TOP 20-50', 'NEU']

# CHALKUP BOULDERWELT GYM ID
GYM_ID = 2
# CHALKUP BOULDERWELT FLOORPLAN ID
FLOORPLAN_ID = 2
FLOORPLAN_SIZE = [2000, 1393]

# http://boulderwelt.tcwebworks.de/dev/img/colors/XX.jpg
COLOR_MAP = {
    '2.jpg': 'GREEN',
    '29.jpg': 'PINK',
    '26.jpg': 'WHITE',
    '24.jpg': 'BLACK',
    '27.jpg': 'BLUE/WHITE/RED',
    '1.jpg': 'RED',
    '23.jpg': 'PURPLE',
    '21.jpg': 'ORANGE',
    '22.jpg': 'YELLOW',
    '3.jpg': 'BLUE',
    '31.jpg': 'BLACK/BLUE',
}


def pq_load(url):
    return pq(url, opener=lambda u, **kw: urllib.request.urlopen(u).read().decode('utf-8'), parser='html')


def scrape_parcours(html):
    parcours_html = html.find('select[name="parcour"] option')

    parcours = []

    for parcour_html in parcours_html.items():
        if parcour_html.text() in IGNORED_PARCOURS:
            continue
        parcour = {}

        parcour['id'] = parcour_html.attr.value
        parcour['title'] = parcour_html.text()
        # grade
        r = re.compile("\((.*) bis (.*)\)").search(parcour_html.text())
        if r:
            parcour['grade'] = [r.groups()[0], r.groups()[1]]

        parcours.append(parcour)

    return parcours


def scrape_parcour(parcour):
    r = requests.post("{}/index.php".format(BASE_URL), {'action': 'setparcour', 'parcour': parcour['id']})
    html = pq(r.content)
    links = html.find('a[rel="overlay"]')

    boulders = []

    for link in links.items():
        boulder = {}
        boulder['id'] = int(re.compile("boulder=(.*)").search(link.attr.href).groups()[0])
        style = re.compile("left:(.*)px;top:(.*)px;").search(link.attr.style)
        boulder['location'] = {'floorPlan': {'id': FLOORPLAN_ID},
                               'x': int(style.groups()[0]) / FLOORPLAN_SIZE[0],
                               'y': int(style.groups()[1]) / FLOORPLAN_SIZE[1]}

        if 'grade' in parcour and len(parcour['grade']) == 2:
            boulder['initialGrade'] = {'certainty': 'RANGE',
                                       'gradeLow': {'font': parcour['grade'][0]},
                                       'gradeHigh': {'font': parcour['grade'][1]}}
        else:
            boulder['initialGrade'] = {'certainty': 'ASSIGNED',
                                       'grade': {
                                           'font': re.compile("(.*) .*").search(link.find('div.text').text()).groups()[
                                               0]}}

        dot_div_style = link.find('div.dot_big').attr.style
        bg_image = re.compile("background:url\('img/colors/(.*)'\);").search(dot_div_style).groups()[0]
        boulder['color'] = {'name': COLOR_MAP[bg_image]}

        boulder['gym'] = GYM_ID

        boulders.append(boulder)

    return boulders


def scrape_boulders():
    html = pq_load("{}/index.php".format(BASE_URL))
    parcours = scrape_parcours(html)

    boulders = []
    for parcour in parcours:
        print('parsing parcour {}'.format(parcour['title']))
        boulders.extend(scrape_parcour(parcour))

    return boulders


boulders = scrape_boulders()
print(json.dumps(boulders))