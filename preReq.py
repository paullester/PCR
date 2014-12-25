import json
import os
import re
import string
import sys

cwd = os.getcwd()

courseFile = "PennCourseReview/courses.json"
courseMap = {}

def dept(deptJson):
    print deptJson
    deptCourses = json.loads(open(deptJson).read())

    for i in deptCourses["result_data"]:
        courseId = i["course_id"].replace(" ", "")
        preReqs = i["prerequisites"]
        prereqList = []

        if preReqs == "":
            continue

        if re.search("\w{3,4}\s{0,1}\d{3}", preReqs):
            if courseId not in courseMap:
                courseMap[courseId] = {}

            if "/" in preReqs:
                preReqs = preReqs.replace("/", " or ")

            # Instructor Permission
            if "instructor" in (preReqs.lower()) and \
                    "permission" in (preReqs.lower()):
                courseMap[courseId]["instructorPermission"] = True

            m = re.findall("((?:\w{2,4}){0,1}\s{0,1}\d{3}),{0,1}\s{0,1}(,|or|and|AND|OR){0,1}", preReqs)

            if m:
                orFlag = False
                andFlag = False
                deptName = ""

                for i in m:
                    cid = i[0].replace(" ", "").upper()
                    deptSearch = re.search("([A-Za-z]{2,4})", cid)

                    if not deptSearch:
                        cid = deptName + cid
                    else:
                        deptName = deptSearch.group(0)
                    prereqList.append(cid)

                    if i[1].lower() == "or":
                        orFlag = True
                    elif i[1].lower() == "and":
                        andFlag = True

                courseMap[courseId]["prerequisites"] = prereqList

                if orFlag:
                    courseMap[courseId]["orFlag"] = orFlag
                if andFlag:
                    courseMap[courseId]["andFlag"] = andFlag

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print "use: preReq.py Path_to_Directory_with_Departments"
        quit()

    if os.path.isfile(courseFile):
        courseMap = json.loads(open(courseFile).read())

    for i in os.listdir(sys.argv[1]):
        if i.endswith(".json"):
            dept(os.path.join(sys.argv[1], i))

    with open(courseFile, "w") as outfile:
        json.dump(courseMap, outfile, indent = 4)
