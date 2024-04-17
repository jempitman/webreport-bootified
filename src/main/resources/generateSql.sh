tail -n +2 E0.csv |  \
awk -F, '{print "insert into result ( id, division, date, homeTeam, awayTeam, homeGoalsFullTime,awayGoalsFullTime, homeGoalsHalfTime, awayGoalsHalfTime, homeGoalsShots,awayGoalsShots) values (\"" $1 "\", parsedatetime(\"" $2 "\",\"dd/MM/yyyy\"), \"" $4 "\", \"" $5 "\",", $6, ",", $7, ",", $9, ",", $10, ",\"" $12 "\",", $13, ",", $14, ")" }'
