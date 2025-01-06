FROM ubuntu:latest
LABEL authors="ekaql"

ENTRYPOINT ["top", "-b"]