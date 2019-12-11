# About
This console application analyzes the log files in the specified directory.
Analysis includes filtering and grouping log file records.
The filtering result is written to the specified file.
The grouping result is displayed on the screen.

# Notes
* Log record format: **time username: message**
* Time format: **yyyy/MM/dd HH:mm:ss**
* Maximum number of threads: **1000**
* Possible extensions for log files: **.log .txt**

# Other details
The contents of all files are loaded into RAM in multi-threaded mode.
Filtering and writing to a file occurs in a single thread.
Grouping is done using a _parallel stream_.
