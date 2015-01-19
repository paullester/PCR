import json
import os
import re
import string
import sys

from pyparsing import Word, nums, Literal, opAssoc, operatorPrecedence


cwd = os.getcwd()

courseFile = "PennCourseReview/courses.json"
courseMap = {}

def dept(deptJson):
    print "Processing" + deptJson + "..."
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
                n = ["".join(i.split()) for sub in m for i in sub]
                preReqs = " ".join(n).upper()
                preReqs = ", ".join(preReqs.split())
                preReqs = preReqs.replace("OR,", "OR")
                preReqs = preReqs.replace("AND,", "AND")
                preReqs = rchop(preReqs, ", OR")
                preReqs = rchop(preReqs, ", AND")

                course_name = Word(string.ascii_uppercase + nums) | Word(nums)

                comma_separator = Literal(',')
                comma_separator.setParseAction(lambda t:"&&")

                and_separator = Literal('AND') | Literal(", AND")
                and_separator.setParseAction(lambda t:"&&")

                or_separator = Literal('OR') | Literal(", OR")
                or_separator.setParseAction(lambda t:"||")

                course_line = operatorPrecedence(course_name,
                    [
                        (and_separator, 2, opAssoc.LEFT,),
                        (or_separator, 2, opAssoc.LEFT),
                        (comma_separator, 2, opAssoc.LEFT,),
                    ])

                results = course_line.parseString(preReqs)
                x = results.asList()[0]

                if len(x):
                    results_list = []
                    results_list.extend(x if type(x) == list else [x])
                    courseMap[courseId]["prerequisites"] = results_list


def rchop(thestring, ending):
    if thestring.endswith(ending):
        return thestring[:-len(ending)]
    return thestring


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
