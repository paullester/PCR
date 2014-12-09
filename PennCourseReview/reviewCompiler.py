import json
import os
import sys

cwd = os.getcwd()

def reviewSearch(reviewdir, className):
    reviewName = os.path.join(reviewdir,className) + "_review.json"
    if os.path.isfile(reviewName):
        reviewData = json.loads(open(reviewName).read())

        for i in reviewData:
            ratings = i["ratings"]
            instructor = i["instructor"]["name"]

            tempRateMap = {"count" : 1}
            for key in ratings.keys():
                tempRateMap[key] = float(ratings[key])

            tempInstructorMap = {"name" : instructor, "courses" : {} }
            tempInstructorMap["courses"][className] = (tempRateMap)
            tempInstructorMap["avgRating"] = tempRateMap

            tempRateMap["courseName"] = className

            # Instructor average ratings
            if instructor in instructorMap:
                # average overall ratings
                instructorMap[instructor]["avgRating"]["count"] += 1

                for key in set(instructorMap[instructor]["avgRating"].keys()).\
                               intersection(tempRateMap.keys()):
                    if key not in ["count", "courseName"]:
                        instructorMap[instructor]["avgRating"][key] = \
                            (instructorMap[instructor]["avgRating"][key] + \
                            tempRateMap[key]) / \
                            instructorMap[instructor]["avgRating"]["count"]

                # average course ratings
                if className in instructorMap[instructor]["courses"]:
                    instructorMap[instructor]["courses"][className]["count"] += 1

                    for key in set(\
                        instructorMap[instructor]["courses"][className].keys()).\
                                intersection(tempRateMap.keys()):
                        if key not in ["count", "courseName" ]:
                            instructorMap[instructor]["courses"][className][key] = \
                                (instructorMap[instructor]["courses"][className][key] + \
                                tempRateMap[key]) / \
                                instructorMap[instructor]["courses"][className]["count"]
                else:
                    instructorMap[instructor]["courses"][className] = tempRateMap
            else:
                instructorMap[instructor] = tempInstructorMap

            # Course average rating
            if className in courseMap:
                # If present, we need to aggregate
                courseMap[className]["count"] += 1
                for key in set(courseMap[className].keys())\
                        .intersection(tempRateMap.keys()):
                    if key not in ["count", "courseName" ]:
                        courseMap[className][key] = \
                            (courseMap[className][key] + tempRateMap[key]) / \
                            courseMap[className]["count"]
            else:
                courseMap[className] = tempRateMap

    return 0

def deptSearch(deptdir):
    for i in os.listdir(deptdir):
        if os.path.isdir(os.path.join(deptdir, i)):
            classSearch(os.path.join(deptdir, i), i)

def classSearch(classdir, className):
    for i in os.listdir(classdir):
        reviewSearch(os.path.join(classdir, i), className)


courseFile = "courses.json"
instructorFile = "instructors.json"
instructorMap = {}
courseMap = {}

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print "use: reviewCompiler.py Path_to_Directory_with_Departments"
        quit()

    if os.path.isfile(instructorFile):
        instructorMap = json.loads(open(instructorFile).read())
    if os.path.isfile(courseFile):
        courseMap = json.loads(open(courseFile).read())

    for i in os.listdir(sys.argv[1]):
        deptSearch(os.path.join(sys.argv[1], i))

    deptName = os.path.basename(sys.argv[1])

    with open(instructorFile, "w") as outfile:
        json.dump(instructorMap, outfile)

    with open(courseFile, "w") as outfile:
        json.dump(courseMap, outfile)
