#!/bin/bash

url="https://esb.isc-seo.upenn.edu/8091/open_data/course_info/"
for dpt in $(cat departments.txt); do
    content="$(curl -H "Authorization: Basic VVBFTk5fT0RfZW16MV8xMDAxMDQzOmU1c3U3MGwyZ3NjODk1bzdjMmdsbHFmcXE3" -H "Authorization: Bearer UPENN_OD_emz1_1001043" -H "Authorization: Token e5su70l2gsc895o7c2gllqfqq7" -i "$url/$dpt/?number_of_results_per_page=999")"
    echo "$content" >> $dpt.json
done