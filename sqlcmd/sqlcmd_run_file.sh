#! /bin/bash

DIR=$1
HOST=$2
PORT=$3
USER=$4
PASSWORD=$5
DATABASE=$6

function is_file_has_content() {
  FILE_PATH=$1
  if [[ -f "$FILE_PATH" ]]
  then
    content=$(cat "$FILE_PATH")
    if [[ $(echo "$content" | wc -m ) -gt 1 ]]
    then
      return 1
    fi
  fi
  return 0
}

echo 'Test connection'
TEST_ERROR_LOG_PATH="$DIR/test_err_log.txt"
eval "/opt/mssql-tools/bin/sqlcmd -S $HOST,$PORT -U $USER -P $PASSWORD -d $DATABASE -Q 'SELECT 1' -r1 2> $TEST_ERROR_LOG_PATH"

is_file_has_content "$TEST_ERROR_LOG_PATH"
if [[ "$?" -eq 1 ]]
then
  echo 'Test error:'
  cat "$TEST_ERROR_LOG_PATH"
  exit 1
fi

INSTRUCTION_FILE=$(cat "$DIR/instruct.txt")
for (( i = 1; i <= $(echo "$INSTRUCTION_FILE" | wc -l); i++ ))
do
  sqlFileName="$(echo "$INSTRUCTION_FILE" | sed -n "${i}p")"
  if [[ -z $sqlFileName ]]
  then
    continue
  fi

  sqlFilePath="$DIR/$sqlFileName"
  ERROR_LOG_PATH="$DIR/err_log_$i.txt"
  cmd="/opt/mssql-tools/bin/sqlcmd -S $HOST,$PORT -U $USER -P $PASSWORD -d $DATABASE -i '$sqlFilePath' -r1 2> $ERROR_LOG_PATH"
  echo "cmd:$cmd"
  eval "$cmd"

  is_file_has_content "$ERROR_LOG_PATH"
  if [[ "$?" -eq 1 ]]
  then
      echo "Run $sqlFilePath error:"
    cat "$ERROR_LOG_PATH"
    exit 1
  fi
  echo "Run $sqlFilePath success."
done
exit 0
