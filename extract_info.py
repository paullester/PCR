import json
data = json.load(open('basic_info.json'))

def get_departments() :
	for key in data['result_data'][0]['departments_map'].keys():
		print key

get_departments()