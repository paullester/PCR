import json
import os

with open("courseDescription.txt", "w") as outfile

for file in os.listdir("/Users/susangreenberg/Documents/PCR/departmentInfoJSON"):
    if ".json" in file:
        fileName = os.path.join("/Users/susangreenberg/Documents/PCR/departmentInfoJSON", file)
        classMap = (json.loads(open(fileName).read()))
        for i in range(0, len(classMap["result_data"])):
            outfile.write(classMap["course_id"] + " " + classMap["course_description"] + "\n")