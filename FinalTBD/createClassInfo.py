import json
import os

classesMap = {}

for file in os.listdir("/Users/susangreenberg/Documents/PCR/courseInfoJSON"):
    if ".json" in file:
        fileName = os.path.join("/Users/susangreenberg/Documents/PCR/courseInfoJSON", file)
        classMap = (json.loads(open(fileName).read()))
        for i in range(0, len(classMap["result_data"])):
            className = classMap["result_data"][i]["course_department"] + classMap["result_data"][i]["course_number"]
            if className not in classesMap:
                classesMap[className] = {}
                classesMap[className]["crosslistings"] = classMap["result_data"][i]["crosslistings"]
                classesMap[className]["start_time"] = classMap["result_data"][i]["meetings"][0]["start_time"]
                classesMap[className]["end_time"] = classMap["result_data"][i]["meetings"][0]["end_time"]
                classesMap[className]["meeting_days"] = classMap["result_data"][i]["meetings"][0]["meeting_days"]
                classesMap[className]["instructor"] = classMap["result_data"][i]["instructors"][0]["name"]
                classesMap[className]["credits"] = classMap["result_data"][i]["credits"]
                classesMap[className]["recitations"] = []
                for j in range(0, len(classMap["result_data"][i]["recitations"])):
                    classesMap[className]["recitations"].append(classMap["result_data"][i]["recitations"][j]["section_id"])


with open("ClassInfo.json", "w") as outfile:
    json.dump(classesMap, outfile, indent=4)



