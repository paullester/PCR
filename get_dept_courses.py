import json

def get_courses(filename) :
	data = json.load(open(filename))
	courses = {}
	print data[result_data][0]


get_courses("courseInfoJSON/MATH.json")