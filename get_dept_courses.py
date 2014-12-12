import json

def get_courses(filename) :
	data = json.load(open(filename))
	courses = []
	for course in data["result_data"]:
		courses.append(course["course_department"] + course["course_number"])
	return courses

departments = open('departments.txt')
all_courses = []
for dept in departments.readlines():
	all_courses.append(get_courses("courseInfoJSON/" + dept.strip() + ".json"))

file = open("all_courses.txt", "w")
for dept in all_courses:
	for course in dept:
		file.write(course + '\n')
file.close()