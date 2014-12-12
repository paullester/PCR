import json
import os
import string

coursesMap = json.loads(open("courses.json").read())

prereqFlags = []
coreqFlags = []

for file in os.listdir("/Users/susangreenberg/Documents/PCR/departmentJSON"):
    if ".json" in file:
        fileName = os.path.join("/Users/susangreenberg/Documents/PCR/departmentJSON", file)
        deptMap = (json.loads(open(fileName).read()))

        for i in range(0, len(deptMap["result_data"])):
            courseName = string.join(deptMap["result_data"][i]["course_id"].split(" "), "")
            
            if courseName not in coursesMap:
                coursesMap[courseName] = {}
            if deptMap["result_data"][i]["prerequisites"] != "":
                prereqFlags.append(courseName)
            coursesMap[courseName]["prerequisites"] = deptMap["result_data"][i]["prerequisites"]
            if deptMap["result_data"][i]["corequisites"] != "":
                coreqFlags.append(courseName)
            coursesMap[courseName]["corequisites"] = deptMap["result_data"][i]["corequisites"]
            coursesMap[courseName]["course_description"] = deptMap["result_data"][i]["course_description"]
    
with open("CoursesWithRegistrar.json", "w") as outfile:
    json.dump(coursesMap, outfile, indent = 4)

with open("EditCoursesPreReq.txt", "w") as outfile:
    for i in prereqFlags:
        outfile.write("%s\n" % i)

with open("EditCoursesCoReq.txt", "w") as outfile:
    for i in coreqFlags:
        outfile.write("%s\n" % i)