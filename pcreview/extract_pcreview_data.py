import json
semesters_data = json.load(open('pcreview_semesters.json'))
depts_data = json.load(open('pcreview_depts'))

def get_pcreview_semesters() :
	for item in semesters_data['result']['values']:
		print item['id']

def get_pcreview_depts() :
	for item in depts_data['result']['values']:
		print item['id']

get_pcreview_semesters()
get_pcreview_depts()