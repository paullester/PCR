import json
import os
import re
import string
import sys

from pyparsing import Word, nums, Literal, opAssoc, operatorPrecedence


cwd = os.getcwd()

courseFile = "PennCourseReview/courses.json"
courseMap = {}
deptNameMap = {"psychology": "PSYC",
               "psych": "PSYC",
               "music": "MUSC",
               "dutch": "DTCH",
               "latin": "LATN",
               "spanish": "SPAN",
               }


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

            # Replace Department names with DEPT sign
            for key, val in deptNameMap.iteritems():
                if key in preReqs.lower():
                    preReqs = re.sub(key, val, preReqs, flags=re.I)

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
                comma_separator.setParseAction(lambda t: "&&")

                and_separator = Literal('AND') | Literal(", AND")
                and_separator.setParseAction(lambda t: "&&")

                or_separator = Literal('OR') | Literal(", OR")
                or_separator.setParseAction(lambda t: "||")

                course_line = operatorPrecedence(course_name, [
                        (and_separator, 2, opAssoc.LEFT,),
                        (or_separator, 2, opAssoc.LEFT),
                        (comma_separator, 2, opAssoc.LEFT,),
                    ])

                results = course_line.parseString(preReqs)
                x = results.asList()[0]

                if len(x):
                    results_list = []
                    results_list.extend(x if type(x) == list else [x])
                    results_list = fillDeptIds(results_list)
                    courseMap[courseId]["prerequisites"] = results_list
                    fillSatisCourse(courseId, results_list)


def fillDeptIds(results_list):
    dept = ""

    for x in xrange(0, len(results_list)):
        i = results_list[x]
        if i == "&&":
            continue
        elif i == "||":
            continue
        elif isinstance(i, list):
            i = fillDeptIds(i)
        else:
            if i[0].isdigit():
                i = dept + i
            else:
                deptSearch = re.search("([A-Za-z]{2,4})", i)
                dept = deptSearch.group(0)
        results_list[x] = i
    return results_list


def fillSatisCourse(satis_course_id, prereq_list):
    deptId = ""

    for i in prereq_list:
        if i == "&&":
            continue
        elif i == "||":
            continue
        elif isinstance(i, list):
            fillSatisCourse(satis_course_id, i)
        else:
            j = i

            if i[0].isdigit():
                j = deptId + i
            else:
                deptSearch = re.search("([A-Za-z]{2,4})", i)
                deptId = deptSearch.group(0)

            if j not in courseMap:
                courseMap[j] = {}
            # satisfies list already exists
            if "satisfyReqsFor" in courseMap[j]:
                courseMap[j]["satisfyReqsFor"].append(satis_course_id)
                courseMap[j]["satisfyReqsFor"] = \
                    list(set(courseMap[j]["satisfyReqsFor"]))
            # the current course doesn't have courses it satisfies list
            else:
                courseMap[j]["satisfyReqsFor"] = [satis_course_id]


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
        json.dump(courseMap, outfile, indent=4)
