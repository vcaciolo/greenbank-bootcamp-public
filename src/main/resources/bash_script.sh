#!/bin/bash

# Initial checks
type curl >/dev/null 2>&1 || { echo >&2 "curl command is required but not found!"; exit 1; }
type pidstat >/dev/null 2>&1 || { echo >&2 "pidstat command is required but not found!"; exit 1; }
type timeout >/dev/null 2>&1 || { echo >&2 "timeout command is required but not found!"; exit 1; }

# Reading input from user
read -p "Enter the application port (default to 8080): " application_port
application_port=${application_port:-8080}
echo "--> Using port: $application_port"

read -p "Enter the measurement execution time (default to 60 seconds): " application_measurement_time
application_measurement_time=${application_measurement_time:-60}
echo "--> Using measurement time: $application_measurement_time"

read -p "Enter how many runs you want to issue (default to 10): " application_measurement_runs
application_measurement_runs=${application_measurement_runs:-10}
echo "--> Using measurement runs: $application_measurement_runs"

read -p "Enter the application PID (otherwise I will try to find it by my own): " application_pid
application_pid=${application_pid:-`pgrep -f greenbank-bootcamp-1.0.0-runner.jar`}
[ "$application_pid" ] || { echo >&2 "ERROR - Cannot find process PID, is the application running?"; exit 1; }
echo "--> Using PID: $application_pid"

# Starting measurement API
status_code=$(curl -X 'POST' -d '' -H 'accept: */*' --write-out '%{http_code}' --silent --connect-timeout 1 --output /dev/null "http://localhost:$application_port/test/measurement-async?howManyRuns=$application_measurement_runs")
echo "--> HTTP Status code received: $status_code"
if [ "$status_code" -ne "204" ]
then
    echo >&2 "ERROR - The API returned a status code that is not 204, please check the application!"; exit 1;
fi

# Starting application measurement
echo -n "\n-----> STARTING MEASUREMENT <-----\n\n"
timeout --signal=SIGINT $application_measurement_time pidstat -u -p $application_pid 1
