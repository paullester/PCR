courses = open('all_courses.txt')

def print_courses(dept, exclusions, minimum) :
	for course in courses.readlines():
		if course.startswith(dept) and course[len(dept):].strip().isdigit():
			num = int(course[len(dept):].strip())
			if num >= minimum and not num in exclusions:
				print "\"" + dept + str(num) + "\","

#Engineering Natural Science Requirements:
#print_courses("ENM",[],0)
#print_courses("MATH",[115, 122, 123, 130, 150, 151, 170, 172, 174, 180, 210, 212, 220, 475],104)
#print_courses("BCHE",[],0)
#print_courses("BIBB",[10, 50, 60, 227],0)
#print_courses("BIOL",[130, 446, 544],101)
#print_courses("BMB",[],0)
#print_courses("CAMB",[],0)
#print_courses("CHEM",[11,12,22,23],0)
#print_courses("GCB",[],0)
#print_courses("GEOL",[],200)
#print_courses("PHYS",[500],150)

#CIS Elective:
#print_courses("CIS", [100,101,106,125,160,261], 100)
#print_courses("NETS",[],0)
#print_courses("MKSE",[],0)

#Engineering Courses
#eng_depts = ['BE','CBE','CIS','ESE','MSE','MEAM','NETS','EAS']
#print_courses('BE',[296,297,280,303,503,513],0)
#print_courses('CIS',[296,297,100,101,106,125,160,260,261,262,313,355,590],0)
#print_courses('CSE',[296,297,100,101,106,125,160,260,261,262,313,355,590],0)
#print_courses('EAS',[296,297,9,20,21,22,23,24,25,26,27,28,29,97,111,125,200,203,205,210,280,281,282,285,301,303,305,306,310,320,345,346,348,349,400,401,402,403,445,446,448,449,500,501,502,503,505,506,510,520,545,546,547,548,590,591,595,623],0)
#print_courses('ENM',[296,297],0)
#print_courses('ESE',[296,297,301,302],0)
#print_courses('IPD',[296,297,509,549],0)
#print_courses('MEAM',[296,297,110,147],0)
#print_courses('ENGR',[296,297],0)
#print_courses('CBE',[296,297],0)
#print_courses('MSE',[296,297],0)
print_courses('NETS',[296,297],0)

#Tech Elective
#print_courses("MATH",[],0)
