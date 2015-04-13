import json
import sys
from sets import Set
data = json.load(open('courses.json'))

def get_descendants(course_of_interest, checked_descendants):
	descendants = Set([])
	for course in data.keys():
		if "prerequisites" in data[course]:
			for prereq in data[course]["prerequisites"]:
				if isinstance(prereq, list):
					for subreq in prereq:
						if subreq == course_of_interest :
							descendants.add(course)
				elif prereq == course_of_interest :
					descendants.add(course)
	new_descendants = Set([])
	num_old_descendants = -1
	while not (num_old_descendants == len(new_descendants)) :
		num_old_descendants = len(new_descendants)
		new_descendants = Set([])
		for x in descendants:
			if not x in checked_descendants:
				checked_descendants.add(x)
				new_descendants = new_descendants | get_descendants(x, checked_descendants)
		descendants = descendants | new_descendants
	return descendants

test = "false"

for course_of_interest in data.keys():
	descendants = get_descendants(course_of_interest, Set([]))
	if not len(descendants) == 0:
		sys.stdout.write(course_of_interest + ": ")
		for descendant in descendants :
			sys.stdout.write(descendant + " ")
		print ""