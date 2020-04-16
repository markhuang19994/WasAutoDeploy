#! /bin/bash

DIR=$1
HOST=$2
PORT=$3
USER=$4
PASSWORD=$5
DATABASE=$6

eval "/opt/mssql-tools/bin/sqlcmd -S $HOST,$PORT -U $USER -P $PASSWORD -d $DATABASE -Q 'SELECT 1' -r1 2> $DIR/err_log.txt"
test_err_log=$(cat "$DIR/test_err_log.txt")
if [[ -f "$test_err_log" ]] && [[ $(echo "$test_err_log" | wc -m ) -gt 0 ]]
then
  exit 1
fi

INSTRCTION_FILE=$(cat "$DIR/instruct.txt")
for (( i = 1; i <= $(echo "$INSTRCTION_FILE" | wc -l); i++ ))
do
  sqlFileName="$(echo "$INSTRCTION_FILE" | sed -n "${i}p")"
  if [[ -z $sqlFileName ]]
  then
    continue
  fi
  sqlFilePath="$DIR/$sqlFileName"
  cmd="/opt/mssql-tools/bin/sqlcmd -S $HOST,$PORT -U $USER -P $PASSWORD -d $DATABASE -i '$sqlFilePath' -r1 2> $DIR/err_log.txt"
  echo "cmd:$cmd"
  eval "$cmd"
  cat  "$DIR/err_log.txt"
  echo "run $sqlFilePath success."
done
