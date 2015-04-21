import csv

f = open('in.txt', 'w')
with open('Good Survey Results for Adsorption.csv', 'rb') as csvfile:
    reader = csv.reader(csvfile)
    for row in reader:
        username = row[0].replace(" ", "")
        f.write(username + "\t")
        for course in row[1].split("\n"):
            new = course.replace(" ", "")
            f.write(new + " ")
        for course in row[2].split("\n"):
            new = course.replace(" ", "")
            f.write(new + " ")
        f.write("\n")
