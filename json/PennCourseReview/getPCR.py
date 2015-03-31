import penncoursereview as pcr
import json
import os

# Get departments
def getDept(department, filedir):
    dept = pcr.Department(department)

    filedir = filedir + department
    filename = filedir + "/" + department + ".json"

    if not os.path.exists(os.path.dirname(filename)):
        os.makedirs(os.path.dirname(filename))

    # Department generic JSON file
    with open(filename, "w") as outfile:
        print "Creating JSON file for department " + department + "..."
        json.dump(dept, outfile, default=lambda o: o.__dict__)

    # Department course histories for each course
    for i in range(0, len(dept.coursehistories)):
        try:
            courseHistory = getCourseHistory(dept.coursehistories[i].aliases[0], filedir)
        except:
            continue

        for j in courseHistory.courses:
            course = getCourse(j.id)
            semester = j.semester
            name = course.primary_alias
            review = course.reviews.values

            if (name, semester) in courseMap:
                continue
            else:
                courseMap.append((name, semester))

            reviewdir = filedir + "/" + name + "/" + semester + "/" + name
            filename = reviewdir + ".json"
            reviewname = reviewdir + "_review.json"

            if not os.path.exists(os.path.dirname(filename)):
                os.makedirs(os.path.dirname(filename))

            with open(filename, "w") as outfile:
                print "Creating JSON file for course " + name + " semester " + semester + "..."
                json.dump(course, outfile, default=lambda o: o.__dict__)
            with open(reviewname, "w") as outfile:
                json.dump(review, outfile, default=lambda o: o.__dict__)

def getAllDepts(filedir):
    depts = pcr.Department("")
    for i in range(1, len(depts.values)):
#        if depts.values[i].id[0] > 'L':
        getDept(depts.values[i].id, filedir)


# Get instructors
def getInstructor(instructor):
    instr = pcr.Instructor(instructor)

def getAllInstructors():
    instrs = pcr.Instructor("")
    for i in range(1, len(instrs.values)):
        getInstructor(instrs.values[i].id)


# Get course
def getCourse(cid):
    course = pcr.Course(cid)
    return course

# Get course history
def getCourseHistory(course, filedir):
    courseHistory = pcr.CourseHistory(course)
    return courseHistory




filedir = "files/"
courseMap = []

if __name__ == "__main__":
    getAllDepts(filedir)
#    getDept("CIS", filedir)
